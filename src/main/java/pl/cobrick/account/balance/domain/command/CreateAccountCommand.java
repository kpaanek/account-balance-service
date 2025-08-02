package pl.cobrick.account.balance.domain.command;

import java.math.BigDecimal;

public interface CreateAccountCommand {
    String firstName();
    String lastName();
    BigDecimal usdAmount();
    BigDecimal plnAmount();
}
