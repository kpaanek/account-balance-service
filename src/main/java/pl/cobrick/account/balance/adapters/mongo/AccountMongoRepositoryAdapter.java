package pl.cobrick.account.balance.adapters.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.cobrick.account.balance.domain.model.Account;
import pl.cobrick.account.balance.domain.ports.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class AccountMongoRepositoryAdapter implements AccountRepository {

    private final AccountMongoRepository accountMongoRepository;

    @Override
    public Optional<Account> findById(String id) {
        return accountMongoRepository.findById(id);
    }

    @Override
    public void save(Account account) {
        accountMongoRepository.save(account);
    }
}
