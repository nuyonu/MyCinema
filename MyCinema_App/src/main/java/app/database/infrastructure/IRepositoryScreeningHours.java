package app.database.infrastructure;

import app.database.entities.ScreeningHours;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IRepositoryScreeningHours extends MongoRepository<ScreeningHours, String> {

    Optional<ScreeningHours> findById(String id);

    ScreeningHours findByMovieId(ObjectId movieId);

    ScreeningHours findByRoomId(ObjectId roomId);

}

