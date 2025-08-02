package pl.cobrick.account.balance.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cobrick.account.balance.domain.command.CreateAccountCommand;
import pl.cobrick.account.balance.domain.command.TransferBalanceCommand;
import pl.cobrick.account.balance.domain.exception.InvalidDomainStateException;
import pl.cobrick.account.balance.domain.model.Account;
import pl.cobrick.account.balance.domain.ports.AccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountCommandHandler {

    private final AccountRepository accountRepository;
    private final List<CurrencyTransferStrategy> currencyTransferStrategies;

    public String handle(CreateAccountCommand command) {
        var account = Account.of(command);
        accountRepository.save(account);
        return account.getId();
    }

    public void handle(TransferBalanceCommand command) {
        var account = accountRepository.findById(command.id())
                .orElseThrow(() -> new InvalidDomainStateException("Cannot find account with id: %s".formatted(command.id())));
        account.transfer(currencyTransferStrategies, command);
        accountRepository.save(account);
    }
}
