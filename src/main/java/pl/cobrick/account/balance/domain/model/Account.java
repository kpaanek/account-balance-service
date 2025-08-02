package pl.cobrick.account.balance.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.cobrick.account.balance.domain.command.CreateAccountCommand;
import pl.cobrick.account.balance.domain.command.TransferBalanceCommand;
import pl.cobrick.account.balance.domain.exception.InvalidDomainStateException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document("accounts")
@Builder(access = AccessLevel.PRIVATE)
public class Account {

    @Id
    private String id;
    @Version
    private Long version;
    private String firstName;
    private String lastName;
    private Balance usdBalance;
    private Balance plnBalance;

    public static Account of(CreateAccountCommand createAccountCommand) {
        return Account.builder()
                .id(UUID.randomUUID().toString())
                .firstName(createAccountCommand.firstName())
                .lastName(createAccountCommand.lastName())
                .usdBalance(Optional.ofNullable(createAccountCommand.usdAmount())
                        .map(Balance::usdBalanceOf)
                        .orElse(Balance.emptyUsdBalance()))
                .plnBalance(Optional.ofNullable(createAccountCommand.plnAmount())
                        .map(Balance::plnBalanceOf)
                        .orElse(Balance.emptyPlnBalance()))
                .build();
    }

    public void transfer(List<CurrencyTransferStrategy> currencyTransferStrategies, TransferBalanceCommand command) {
        var currencyTransferStrategy = currencyTransferStrategies.stream()
                .filter(strategy -> strategy.supports(command.source(), command.target()))
                .findFirst()
                .orElseThrow(() -> new InvalidDomainStateException("Currency transfer strategy not found."));
        var balances = currencyTransferStrategy.transfer(List.of(usdBalance, plnBalance), command.amount());
        usdBalance = Balance.findBalanceByCurrency(balances, Currency.USD);
        plnBalance = Balance.findBalanceByCurrency(balances, Currency.PLN);
    }
}
