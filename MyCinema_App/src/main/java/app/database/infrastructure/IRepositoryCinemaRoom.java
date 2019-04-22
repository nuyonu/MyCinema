package app.database.infrastructure;

import app.database.entities.CinemaRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IRepositoryCinemaRoom extends MongoRepository<CinemaRoom, String>
{
    Optional<CinemaRoom> findById(String id);
    CinemaRoom findByName(String name);
}
