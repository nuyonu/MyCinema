package App;

import App.Database.Entities.User;
import App.Database.Service.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class Application implements CommandLineRunner {


    @Autowired
    Repository repository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repository.addUser(new User("dada", "dasda", "Dwdef", "dead"));
        System.out.println(repository.genreMovieFindByGenre("action"));
        System.out.println(repository.genreMovieFindByMovieId("dads"));
        System.out.println(repository.movieFindByTitle("asda"));
        System.out.println(repository.movieFindByDuration(100));
        System.out.println(repository.movieFindByDurationBetween(40, 2000));
        System.out.println(repository.movieFindByDurationLessThan(300));
        System.out.println(repository.movieFindByDurationLessThan(300));
    }

}
