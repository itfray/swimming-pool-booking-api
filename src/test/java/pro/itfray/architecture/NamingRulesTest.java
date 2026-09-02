package pro.itfray.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "pro.itfray", importOptions = DoNotIncludeTests.class)
class NamingRulesTest {

  @ArchTest
  static final ArchRule interfaces_should_not_have_names_ending_with_the_word_interface =
      noClasses().that().areInterfaces().should().haveNameMatching(".*Interface");

  @ArchTest
  static final ArchRule
      interfaces_should_not_have_simple_class_names_containing_the_word_interface =
          noClasses().that().areInterfaces().should().haveSimpleNameContaining("Interface");

  @ArchTest
  static final ArchRule controllers_should_be_suffixed =
      classes()
          .that()
          .resideInAPackage("..controller..")
          .or()
          .areAnnotatedWith(RestController.class)
          .or()
          .areAnnotatedWith(Controller.class)
          .should()
          .haveSimpleNameEndingWith("Controller")
          .orShould()
          .haveSimpleNameEndingWith("ControllerImpl");

  @ArchTest
  static final ArchRule controllers_should_not_have_rest_in_name =
      classes()
          .that()
          .resideInAPackage("..controller..")
          .should()
          .haveSimpleNameNotContaining("Rest");

  @ArchTest
  static final ArchRule services_should_be_suffixed =
      classes()
          .that()
          .resideInAPackage("..service..")
          .or()
          .areAnnotatedWith(Service.class)
          .should()
          .haveSimpleNameEndingWith("Service")
          .orShould()
          .haveSimpleNameEndingWith("ServiceImpl");

  @ArchTest
  static final ArchRule repositories_should_be_suffixed =
      classes()
          .that()
          .resideInAPackage("..repository..")
          .or()
          .areAnnotatedWith(Repository.class)
          .should()
          .haveSimpleNameEndingWith("Repository")
          .orShould()
          .haveSimpleNameEndingWith("RepositoryImpl");

  @ArchTest
  static final ArchRule mappers_should_be_suffixed =
      classes()
          .that()
          .resideInAPackage("..mapper..")
          .or()
          .areAnnotatedWith(Mapper.class)
          .should()
          .haveSimpleNameEndingWith("Mapper")
          .orShould()
          .haveSimpleNameEndingWith("MapperImpl");
}
