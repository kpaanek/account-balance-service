package pl.cobrick.account.balance.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cobrick.account.balance.domain.exception.InvalidDomainStateException;
import pl.cobrick.account.balance.domain.model.Account;
import pl.cobrick.account.balance.domain.ports.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountQueryHandler {

    private final AccountRepository accountRepository;

    public Account findById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new InvalidDomainStateException("Cannot find account with id: %s".formatted(id)));
    }
}
