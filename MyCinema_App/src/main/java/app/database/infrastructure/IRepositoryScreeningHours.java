package app.database.infrastructure;

import app.database.entities.ScreeningHours;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IRepositoryScreeningHours extends MongoRepository<ScreeningHours, String> {

    Optional<ScreeningHours> findById(String id);

    ScreeningHours findByMovieId(String movieId);

    ScreeningHours findByRoomId(String roomId);

}

