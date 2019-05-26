package app.database.infrastructure;

import app.database.entities.Activation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IRepositoryActivation extends MongoRepository<Activation, String> {
    Activation findByCode(String code);

    Activation findByEmail(String username);

    void deleteByCode(String code);

    void deleteByEmail(String email);
}
