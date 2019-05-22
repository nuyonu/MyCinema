package app.controller.services;

import app.database.entities.Statistics;
import app.database.infrastructure.IStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StatisticsService implements IStatisticService {

    @Override
    public void increseTicket(int number) {
        List<Statistics> statistics=repository.findAll();
        if(statistics.size()==0){
            Statistics statistics1=new Statistics();
            statistics1.setTotalTickets(1);
            repository.save(statistics1);
        }else
        {
            Statistics statistics1=statistics.get(0);
            statistics1.setTotalTickets(statistics1.getMoviePlayed()+1);
            repository.save(statistics1);
        }
    }

    @Override
    public void increseMovie(int number) {

        List<Statistics> statistics=repository.findAll();
        if(statistics.size()==0){
            Statistics statistics1=new Statistics();
            statistics1.setMoviePlayed(1);
            repository.save(statistics1);
        }else
        {
            Statistics statistics1=statistics.get(0);
            statistics1.setMoviePlayed(statistics1.getMoviePlayed()+1);
            repository.save(statistics1);
        }
    }

    @Override
    public void increaseEarning(float number) {

        List<Statistics> statistics=repository.findAll();
        if(statistics.size()==0){
            Statistics statistics1=new Statistics();
            statistics1.setTotalEarnings(number);
            repository.save(statistics1);
        }else
        {
            Statistics statistics1=statistics.get(0);
            statistics1.setTotalEarnings(statistics1.getTotalEarnings()+number);
            repository.save(statistics1);
        }
    }

    @Autowired
    IStatisticsRepository repository;
}
