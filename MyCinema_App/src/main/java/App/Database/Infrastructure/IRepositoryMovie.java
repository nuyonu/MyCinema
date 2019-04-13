package App.Database.Infrastructure;

import App.Database.Entities.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepositoryMovie extends MongoRepository<Movie, String> {
    //    Movie findById(String id);
    Movie findByTitle(String Title);

    List<Movie> findByDuration(Integer duration);

    List<Movie> findByDurationLessThan(Integer duration);

    List<Movie> findByDurationGreaterThan(Integer duration);

    List<Movie> findByTitleContaining(String title);

    List<Movie> findByDurationBetween(Integer from, Integer to);


}
