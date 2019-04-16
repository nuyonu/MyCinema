package app.database.infrastructure;

import app.database.entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IRepositoryRoom extends MongoRepository<Room, String> {
    Optional<Room> findById(String id);
    Room findByName(String name);

    Room findByIdMovieAndTimeAndDay(String id, String time, Integer day);
}
