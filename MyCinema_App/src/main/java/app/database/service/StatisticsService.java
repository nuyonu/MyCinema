package app.database.service;

import app.database.entities.Statistics;
import app.database.infrastructure.IRepositoryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private IRepositoryStatistics repositoryStatistics;

    public StatisticsService() {
        //Do nothing because you need for functions.
    }

    private List<Statistics> statistics;

    public void increaseTotalTickets() {
        try {
            statistics = repositoryStatistics.findAll();
            Statistics statistics1 = statistics.get(0);
            statistics1.setTotalTickets(statistics1.getTotalTickets() + 1);
            repositoryStatistics.save(statistics1);
        } catch (IndexOutOfBoundsException e) {
            repositoryStatistics.save(new Statistics(1, 0 , 0.0));
        }
    }

    public void increaseTotalMovies() {
        try {
            statistics = repositoryStatistics.findAll();
            Statistics statistics1 = statistics.get(0);
            statistics1.setMoviePlayed(statistics1.getMoviePlayed() + 1);
            repositoryStatistics.save(statistics1);
        } catch (IndexOutOfBoundsException e) {
            repositoryStatistics.save(new Statistics(0, 1 , 0.0));
        }
    }

    public void increaseTotalEarnings(double number) {
        try {
            statistics = repositoryStatistics.findAll();
            Statistics statistics1 = statistics.get(0);
            statistics1.setTotalEarnings(statistics1.getTotalEarnings() + number);
            repositoryStatistics.save(statistics1);
        } catch (IndexOutOfBoundsException e) {
            repositoryStatistics.save(new Statistics(0, 0 , number));
        }
    }
}
