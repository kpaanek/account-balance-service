package pl.cobrick.account.balance.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.cobrick.account.balance.domain.model.Balance;
import pl.cobrick.account.balance.domain.model.Currency;
import pl.cobrick.account.balance.domain.model.CurrencyTransferStrategy;
import pl.cobrick.account.balance.domain.ports.ExchangeRateClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlnToUsdTransferStrategy implements CurrencyTransferStrategy {

    private final ExchangeRateClient exchangeRateClient;

    @Override
    public boolean supports(Currency source, Currency target) {
        return source == Currency.PLN && target == Currency.USD;
    }

    @Override
    public List<Balance> transfer(List<Balance> balances, BigDecimal amount) {
        var plnBalance = Balance.findBalanceByCurrency(balances, Currency.PLN);
        var usdBalance = Balance.findBalanceByCurrency(balances, Currency.USD);
        validate(plnBalance, amount);
        var exchangeRate = calculatePlnToUsdExchangeRate();
        var updatedPlnBalance = plnBalance.subtract(amount);
        var updatedUsdBalance = usdBalance.add(amount.multiply(exchangeRate));
        return List.of(updatedPlnBalance, updatedUsdBalance);
    }

    private BigDecimal calculatePlnToUsdExchangeRate() {
        return BigDecimal.ONE.divide(exchangeRateClient.getForCurrency(Currency.USD), 2, RoundingMode.HALF_UP);
    }
}
