package pl.cobrick.account.balance.archunit

import com.tngtech.archunit.core.importer.ClassFileImporter
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

class ClassesAccessibilityRulesSpec extends Specification {

    def "Domain ports should be interfaces and public"() {
        given:
        def domainPortsPackage = 'pl.cobrick.account.balance.domain.ports'
        def domainClasses = new ClassFileImporter().importPackages(domainPortsPackage)

        when:
        def archRule = classes().that().resideInAPackage(domainPortsPackage)
                .should().beInterfaces()
                .andShould().bePublic()

        then:
        archRule.check(domainClasses)
    }
}
