package app.database.infrastructure;

import app.database.entities.GenreMovie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepositoryGenreMovie extends MongoRepository<GenreMovie, String> {
    GenreMovie findByMovieId(String movieId);

    List<GenreMovie> findByGenre(String genre);


}
