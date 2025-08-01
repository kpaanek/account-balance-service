package pl.cobrick.account.balance.domain;

import lombok.RequiredArgsConstructor;
import pl.cobrick.account.balance.domain.model.Balance;
import pl.cobrick.account.balance.domain.model.Currency;
import pl.cobrick.account.balance.domain.model.CurrencyTransferStrategy;
import pl.cobrick.account.balance.domain.ports.ExchangeRateService;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class UsdToPlnTransferStrategy implements CurrencyTransferStrategy {

    private final ExchangeRateService exchangeRateService;

    @Override
    public boolean supports(Currency source, Currency target) {
        return source == Currency.USD && target== Currency.PLN;
    }

    @Override
    public List<Balance> transfer(List<Balance> balances, BigDecimal amount) {
        var usdBalance = Balance.findBalanceByCurrency(balances, Currency.USD);
        var plnBalance = Balance.findBalanceByCurrency(balances, Currency.PLN);
        validate(usdBalance, amount);
        var exchangeRate = exchangeRateService.getForCurrency(Currency.USD);
        var updatedUsdBalance = usdBalance.subtract(amount);
        var updatedPlnBalance = plnBalance.add(amount.multiply(exchangeRate));
        return List.of(updatedUsdBalance, updatedPlnBalance);
    }
}
