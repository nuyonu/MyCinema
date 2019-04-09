package App.Database.Infrastructure;

import App.Database.Entities.GenreMovie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepositoryGenreMovie extends MongoRepository<GenreMovie, String> {
    GenreMovie findByMovieId(String movieId);

    List<GenreMovie> findByGenre(String genre);


}
