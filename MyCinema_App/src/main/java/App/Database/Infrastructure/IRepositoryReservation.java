package App.Database.Infrastructure;

import App.Database.Entities.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalTime;

public interface IRepositoryReservation extends MongoRepository<Reservation, String> {

    Reservation findByMovieId(String movieId);

    Reservation findByRoomId(String roomId);

    Reservation findByDay(Integer day);

    Reservation findByTime(LocalTime time);
}
