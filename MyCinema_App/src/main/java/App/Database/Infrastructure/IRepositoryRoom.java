package App.Database.Infrastructure;

import App.Database.Entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IRepositoryRoom extends MongoRepository<Room, String> {
    Optional<Room> findById(String id);
    Room findByName(String name);
}
