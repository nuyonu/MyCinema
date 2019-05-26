package app.database.infrastructure;

import app.database.entities.Reservation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IRepositoryReservation extends MongoRepository<Reservation, String> {
    List<Reservation> findAllByUserId(ObjectId userId);
}
