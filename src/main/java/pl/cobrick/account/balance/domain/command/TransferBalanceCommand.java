package pl.cobrick.account.balance.domain.command;

import pl.cobrick.account.balance.domain.model.Currency;

import java.math.BigDecimal;

public interface TransferBalanceCommand {
    String id();
    Currency source();
    Currency target();
    BigDecimal amount();
}
