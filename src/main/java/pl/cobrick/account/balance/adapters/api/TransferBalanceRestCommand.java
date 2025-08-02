package pl.cobrick.account.balance.adapters.api;

import lombok.AccessLevel;
import lombok.Builder;
import pl.cobrick.account.balance.domain.command.TransferBalanceCommand;
import pl.cobrick.account.balance.domain.model.Currency;

import java.math.BigDecimal;

@Builder(access = AccessLevel.PACKAGE)
record TransferBalanceRestCommand(String id,
                                  Currency source,
                                  Currency target,
                                  BigDecimal amount) implements TransferBalanceCommand {
}
