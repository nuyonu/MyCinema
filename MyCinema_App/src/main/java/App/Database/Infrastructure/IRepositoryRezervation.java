package App.Database.Infrastructure;

import App.Database.Entities.Rezervetion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalTime;

public interface IRepositoryRezervation extends MongoRepository<Rezervetion, String> {

    Rezervetion findByMovieId(String movieId);

    Rezervetion findByRoomId(String roomId);

    Rezervetion findByDay(Integer day);

    Rezervetion findByTime(LocalTime time);
}
