package app.database.infrastructure;

import app.database.entities.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalTime;

public interface IRepositoryReservation extends MongoRepository<Reservation, String> {

    Reservation findByMovieId(String movieId);

    Reservation findByRoomId(String roomId);

    Reservation findByDay(Integer day);

    Reservation findByTime(LocalTime time);
}
