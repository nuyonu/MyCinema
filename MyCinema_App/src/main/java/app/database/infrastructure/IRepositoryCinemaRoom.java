package app.database.infrastructure;

import app.database.entities.CinemaRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IRepositoryCinemaRoom extends MongoRepository<CinemaRoom, String>
{
    Optional<CinemaRoom> findById(String id);
    CinemaRoom findByName(String name);

    List<CinemaRoom> findByNameLike(String name);


    List<CinemaRoom> findAllByNameContainingOrderByNameAsc(String roomName);

    CinemaRoom findFirstByOrderByCreatedDateDesc();

}
