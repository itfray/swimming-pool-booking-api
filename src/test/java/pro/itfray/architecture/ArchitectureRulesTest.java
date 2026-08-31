package pro.itfray.architecture;

import static com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;
import static com.tngtech.archunit.library.ProxyRules.no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.mapstruct.Mapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "pro.itfray", importOptions = DoNotIncludeTests.class)
class ArchitectureRulesTest {

  @ArchTest
  static final ArchRule layer_dependencies_are_respected =
      layeredArchitecture().consideringAllDependencies()
          .layer("Controllers").definedBy("..controller..")
          .layer("Services").definedBy("..service..")
          .layer("Persistence").definedBy("..repository..")

          .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
          .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
          .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services");

  @ArchTest
  static final ArchRule no_accesses_to_upper_package = NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

  @ArchTest
  static final ArchRule interfaces_must_not_be_placed_in_implementation_packages =
      noClasses().that().resideInAPackage("..impl..")
          .should().beInterfaces()
          .allowEmptyShould(true);

  @ArchTest
  static ArchRule controllers_should_be_in_a_controller_package =
      classes()
          .that().haveSimpleNameContaining("Controller")
          .or().areAnnotatedWith(RestController.class)
          .or().areAnnotatedWith(Controller.class)
          .should().resideInAPackage("..controller..");

  @ArchTest
  static ArchRule services_should_be_in_a_service_package =
      classes()
          .that().haveSimpleNameContaining("Service")
          .or().areAnnotatedWith(Service.class)
          .should().resideInAPackage("..service..");

  @ArchTest
  static ArchRule repositories_should_be_in_a_repository_package =
      classes()
          .that().haveSimpleNameContaining("Repository")
          .or().areAnnotatedWith(Repository.class)
          .should().resideInAPackage("..repository..");

  @ArchTest
  static ArchRule mappers_should_be_in_a_repository_package =
      classes()
          .that().haveSimpleNameContaining("Mapper")
          .or().areAnnotatedWith(Mapper.class)
          .should().resideInAPackage("..mapper..");

  @ArchTest
  static final ArchRule no_cycles_by_method_calls_between_services =
      slices()
          .matching("..(service).(*)..")
          .namingSlices("$2 of $1").should().beFreeOfCycles()
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule no_cycles_by_method_calls_between_mappers =
      slices()
          .matching("..(mapper).(*)..")
          .namingSlices("$2 of $1").should().beFreeOfCycles()
          .allowEmptyShould(true);

  @ArchTest
  static ArchRule no_bypass_of_proxy_logic_for_async_methods =
      no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(
          Async.class);

  @ArchTest
  static ArchRule no_bypass_of_proxy_logic_for_transactional_methods =
      no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(
          Transactional.class);

  @ArchTest
  static final ArchRule controllers_should_only_use_their_own_slice =
      slices().matching("..controller.(*)..").namingSlices("Controller $1")
          .as("Controllers").should().notDependOnEachOther()
          .allowEmptyShould(true);
}
