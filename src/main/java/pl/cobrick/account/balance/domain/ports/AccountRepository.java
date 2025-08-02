package pl.cobrick.account.balance.domain.ports;

import pl.cobrick.account.balance.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findById(String id);
    void save(Account account);
}
