package community.n1ce.blog;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("community.n1ce.blog");

        noClasses()
            .that()
            .resideInAnyPackage("community.n1ce.blog.service..")
            .or()
            .resideInAnyPackage("community.n1ce.blog.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..community.n1ce.blog.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
