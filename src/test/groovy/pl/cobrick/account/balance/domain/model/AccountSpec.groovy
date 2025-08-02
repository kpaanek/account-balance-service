package pl.cobrick.account.balance.domain.model

import pl.cobrick.account.balance.domain.PlnToUsdTransferStrategy
import pl.cobrick.account.balance.domain.UsdToPlnTransferStrategy
import pl.cobrick.account.balance.domain.command.CreateAccountCommand
import pl.cobrick.account.balance.domain.command.TransferBalanceCommand
import pl.cobrick.account.balance.domain.exception.InvalidDomainStateException
import pl.cobrick.account.balance.domain.ports.ExchangeRateClient
import spock.lang.Specification

class AccountSpec extends Specification {

    def exchangeRateService = Mock(ExchangeRateClient.class)
    def plnToUsdTransferStrategy = new PlnToUsdTransferStrategy(exchangeRateService)
    def usdToPlnTransferStrategy = new UsdToPlnTransferStrategy(exchangeRateService)

    def setup() {
        exchangeRateService.getForCurrency(Currency.USD) >> new BigDecimal('3.70')
    }

    def "Should create an Account and transfer PLN to USD"() {
        given:
        def account = Account.of(createAccountCommand())
        def transferBalanceCommand = transferBalanceCommand(Currency.PLN, Currency.USD, BigDecimal.TEN)

        when:
        account.transfer([plnToUsdTransferStrategy, usdToPlnTransferStrategy], transferBalanceCommand)

        then:
        account.id
        account.firstName == 'Anna'
        account.lastName == 'Testowa'
        account.usdBalance.currency() == Currency.USD
        account.usdBalance.amount() == new BigDecimal('202.70')
        account.plnBalance.currency() == Currency.PLN
        account.plnBalance.amount() == new BigDecimal('290.00')
    }

    def "Should create an Account and transfer USD to PLN"() {
        given:
        def account = Account.of(createAccountCommand())
        def transferBalanceCommand = transferBalanceCommand(Currency.USD, Currency.PLN, BigDecimal.TEN)

        when:
        account.transfer([plnToUsdTransferStrategy, usdToPlnTransferStrategy], transferBalanceCommand)

        then:
        account.id
        account.firstName == 'Anna'
        account.lastName == 'Testowa'
        account.usdBalance.currency() == Currency.USD
        account.usdBalance.amount() == new BigDecimal('190.00')
        account.plnBalance.currency() == Currency.PLN
        account.plnBalance.amount() == new BigDecimal('337.00')
    }

    def "Should create an Account and throw exception when trying transfer USD to USD"() {
        given:
        def account = Account.of(createAccountCommand())
        def transferBalanceCommand = transferBalanceCommand(Currency.USD, Currency.USD, BigDecimal.ZERO)

        when:
        account.transfer([plnToUsdTransferStrategy, usdToPlnTransferStrategy], transferBalanceCommand)

        then:
        thrown(InvalidDomainStateException.class)
    }

    def "Should create an Account and throw exception when trying transfer PLN to PLN"() {
        given:
        def account = Account.of(createAccountCommand())
        def transferBalanceCommand = transferBalanceCommand(Currency.PLN, Currency.PLN, BigDecimal.ZERO)

        when:
        account.transfer([plnToUsdTransferStrategy, usdToPlnTransferStrategy], transferBalanceCommand)

        then:
        thrown(InvalidDomainStateException.class)
    }

    def "Should create an Account and throw exception when trying to transfer USD to PLN but the requested amount is greater than the source"() {
        given:
        def account = Account.of(createAccountCommand())
        def transferBalanceCommand = transferBalanceCommand(Currency.USD, Currency.PLN, new BigDecimal('2000000.00'))

        when:
        account.transfer([plnToUsdTransferStrategy, usdToPlnTransferStrategy], transferBalanceCommand)

        then:
        thrown(InvalidDomainStateException.class)
    }

    def "Should create an Account and throw exception when trying to transfer PLN to USD but the requested amount is greater than the source"() {
        given:
        def account = Account.of(createAccountCommand())
        def transferBalanceCommand = transferBalanceCommand(Currency.PLN, Currency.USD, new BigDecimal('3000000.00'))

        when:
        account.transfer([plnToUsdTransferStrategy, usdToPlnTransferStrategy], transferBalanceCommand)

        then:
        thrown(InvalidDomainStateException.class)
    }

    def createAccountCommand() {
        [
                firstName: { 'Anna' },
                lastName : { 'Testowa' },
                usdAmount: { new BigDecimal('200.00') },
                plnAmount: { new BigDecimal('300.00') }
        ] as CreateAccountCommand
    }

    def transferBalanceCommand(def sourceCurrency, def targetCurrency, def amount) {
        [
                id    : { UUID.randomUUID().toString() },
                source: { sourceCurrency },
                target: { targetCurrency },
                amount: { amount }

        ] as TransferBalanceCommand
    }
}
