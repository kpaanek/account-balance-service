package pl.cobrick.account.balance.archunit

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

class PackagesDependencyRulesSpec extends Specification {

    def "Domain should not have dependency on adapters"() {
        given:
        def domainPackage = 'pl.cobrick.account.balance.domain..'
        def adaptersPackage = 'pl.cobrick.account.balance.adapters..'
        def classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(domainPackage, adaptersPackage)

        when:
        def archRule = noClasses().that().resideInAPackage(domainPackage)
                .should().dependOnClassesThat().resideInAPackage(adaptersPackage)

        then:
        archRule.check(classes)
    }
}
