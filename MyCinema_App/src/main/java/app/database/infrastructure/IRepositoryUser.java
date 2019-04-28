package app.database.infrastructure;

import app.database.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IRepositoryUser extends MongoRepository<User, String> {
    User findByFirstName(String firstName);

    List<User> findAllByLastName(String lastName);

    List<User> findAllByUsernameContainingOrderByUsernameAsc(String username);

    User findByEmail(String email);

    Optional<User> findById(String id);

    User findByUsername(String username);

    @Override
    <S extends User> S save(S entity);
}
