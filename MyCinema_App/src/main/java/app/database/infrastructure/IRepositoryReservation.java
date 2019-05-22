package app.database.infrastructure;

import app.database.entities.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IRepositoryReservation extends MongoRepository<Reservation, String> {
}
