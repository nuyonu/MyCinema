package app.database.infrastructure;

import app.database.entities.Statistics;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IStatisticsRepository extends MongoRepository<Statistics,String> {
    List<Statistics> findAll();
}
