package pl.cobrick.account.balance.domain.command;

import java.math.BigDecimal;
import java.util.Optional;

public interface  CreateAccountCommand {
    String firstName();
    String lastName();
    Optional<BigDecimal> usdAmount();
    Optional<BigDecimal> plnAmount();
}
