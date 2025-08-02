package pl.cobrick.account.balance.adapters.api;

import lombok.AccessLevel;
import lombok.Builder;
import pl.cobrick.account.balance.domain.command.CreateAccountCommand;

import java.math.BigDecimal;

@Builder(access = AccessLevel.PACKAGE)
record CreateAccountRestCommand(String firstName,
                                String lastName,
                                BigDecimal usdAmount,
                                BigDecimal plnAmount) implements CreateAccountCommand {
}
