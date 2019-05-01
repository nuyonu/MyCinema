package app.database.infrastructure;

import app.database.entities.ResetAccount;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface IRepositoryReset extends MongoRepository<ResetAccount, String> {

    ResetAccount findByCode(String code);
    ResetAccount findByEmail(String username);
    ResetAccount deleteByCode(String code);

}
