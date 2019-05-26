package app.database.infrastructure;

import app.database.entities.Statistics;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IRepositoryStatistics extends MongoRepository<Statistics, String> {
    List<Statistics> findAll();

    @Override
    <S extends Statistics> S save(S entity);
}
