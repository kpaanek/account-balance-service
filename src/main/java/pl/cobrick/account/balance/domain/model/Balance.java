package pl.cobrick.account.balance.domain.model;

import pl.cobrick.account.balance.domain.exception.InvalidDomainStateException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public record Balance(Currency currency, BigDecimal amount) {

    public Balance {
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    static Balance emptyUsdBalance() {
        return new Balance(Currency.USD, BigDecimal.ZERO);
    }

    static Balance emptyPlnBalance() {
        return new Balance(Currency.PLN, BigDecimal.ZERO);
    }

    static Balance usdBalanceOf(BigDecimal amount) {
        return new Balance(Currency.USD, amount);
    }

    static Balance plnBalanceOf(BigDecimal amount) {
        return new Balance(Currency.PLN, amount);
    }

    public Balance add(BigDecimal amount) {
        return new Balance(currency, this.amount.add(amount));
    }

    public Balance subtract(BigDecimal amount) {
        return new Balance(currency, this.amount.subtract(amount));
    }

    public static Balance findBalanceByCurrency(List<Balance> balances, Currency currency) {
        return balances.stream()
                .filter(balance -> balance.currency() == currency)
                .findFirst()
                .orElseThrow(() -> new InvalidDomainStateException("Balance for currency: %s not found.".formatted(currency)));
    }
}
