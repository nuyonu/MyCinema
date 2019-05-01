package app.database.infrastructure;

import app.database.entities.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRepositoryMovie extends MongoRepository<Movie, String> {

    Optional<Movie> findById(String id);

    Movie findByTitle(String title);

    List<Movie> findAll();

}
