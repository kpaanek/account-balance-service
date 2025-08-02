package pl.cobrick.account.balance.domain;

import pl.cobrick.account.balance.domain.exception.InvalidDomainStateException;
import pl.cobrick.account.balance.domain.model.Balance;
import pl.cobrick.account.balance.domain.model.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyTransferStrategy {
    boolean supports(Currency source, Currency target);
    List<Balance> transfer(List<Balance> balances, BigDecimal amount);

    default void validate(Balance source, BigDecimal amount) {
        if (source.amount().compareTo(amount) < 0) {
            throw new InvalidDomainStateException("Amount to transfer can't be greater than source account amount.");
        }
    }
}
