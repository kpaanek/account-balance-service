package pl.cobrick.account.balance.adapters.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.cobrick.account.balance.domain.model.Account;

interface AccountMongoRepository extends MongoRepository<Account, String> {
}
