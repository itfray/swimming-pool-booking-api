package pro.itfray.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import java.sql.SQLException;

@AnalyzeClasses(packages = "pro.itfray", importOptions = DoNotIncludeTests.class)
class RepositoryRulesTest {

  @ArchTest
  static final ArchRule repositories_must_reside_in_a_repository_package =
      classes()
          .that()
          .haveNameMatching(".*Repository")
          .should()
          .resideInAPackage("..repository..")
          .as("repositories should reside in a package '..repository..'");

  @ArchTest
  static final ArchRule entities_must_reside_in_a_domain_package =
      classes()
          .that()
          .areAnnotatedWith(Entity.class)
          .should()
          .resideInAPackage("..domain..")
          .as("Entities should reside in a package '..domain..'");

  @ArchTest
  static final ArchRule only_repositories_may_use_the_entity_manager =
      noClasses()
          .that()
          .resideOutsideOfPackage("..repository..")
          .should()
          .accessClassesThat()
          .areAssignableTo(EntityManager.class)
          .as("Only repositories may use the " + EntityManager.class.getSimpleName());

  @ArchTest
  static final ArchRule repositories_must_not_throw_sql_exception =
      noMethods()
          .that()
          .areDeclaredInClassesThat()
          .haveNameMatching(".*Repository")
          .should()
          .declareThrowableOfType(SQLException.class)
          .allowEmptyShould(true);
}
