package com.example.shopcomponents;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import net.bytebuddy.matcher.SubTypeMatcher;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.scanners.SubTypesScanner;

import java.io.IOException;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Evaluates internal packages and checks that those packages are not accessed
 * from the outside.
 */
class InternalPackageTests {

    private static final String BASE_PACKAGE = "com.example";
    private final JavaClasses analyzedClasses =
            new ClassFileImporter().importPackages(BASE_PACKAGE);

    @Test
    void internalPackagesAreNotAccessedFromOutside() throws IOException {

        // so that the test will break when the base package is re-named
        assertPackageExists(BASE_PACKAGE);

        List<String> internalPackages = internalPackages(BASE_PACKAGE);

        for (String internalPackage : internalPackages) {
            assertPackageExists(internalPackage);
            assertPackageIsNotAccessedFromOutside(internalPackage);
        }
    }

    /**
     * Finds all packages with the name "internal"
     **/
    private List<String> internalPackages(String basePackage) {
        Reflections reflections = new Reflections(basePackage, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class).stream()
                .map(Class::getPackageName)
                .filter(p -> p.startsWith(basePackage))
                .filter(p -> p.endsWith(".internal"))
                .distinct()
                .toList();
    }

    void assertPackageIsNotAccessedFromOutside(String internalPackage) {
        noClasses()
                .that()
                .resideOutsideOfPackage(packageMatcher(internalPackage))
                .should()
                .dependOnClassesThat()
                .resideInAPackage(packageMatcher(internalPackage))
                .check(analyzedClasses);
    }

    void assertPackageExists(String packageName) {
        assertThat(analyzedClasses.containPackage(packageName))
                .as("package %s exists", packageName)
                .isTrue();
    }

    private String packageMatcher(String fullyQualifiedPackage) {
        return fullyQualifiedPackage + "..";
    }
}
