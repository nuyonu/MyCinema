package app;

import app.database.entities.Movie;
import app.database.entities.Room;
import app.database.entities.User;
import app.database.exception.DuplicateData;
import app.database.exception.NullParameterPassed;
import app.database.service.IRepository;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
public class DatabasePop {
    private IRepository repository;
    private Random random = new Random();

    public DatabasePop(IRepository repository) {
        this.repository = repository;
    }

    public void pop(boolean run) {
        if (run) {
            try {
                for (int gen = 1; gen < 30; gen++) {
                    repository.addMovie(new Movie("titlu" + gen, random.nextInt(100) + 20, random.nextInt(11) + 10, "/images/movie" + gen % 3 + ".png", this.generateTime()));
                }
                repository.addUser(new User("user1", "firstNameUser1", "lastNameUser1", "user.gmail.com", "password"));
                for (int i = 0; i <= 6; i++) {
                    repository.addRoom(new Room("nameRoom" + i, 13, 7));
                }
                List<Movie> movie = repository.getAllMovies();
                for (Movie movie1 : movie) {
                    int day = 0;
                    for (List<String> time : movie1.getScren()) {
                        for (String time1 : time) {
                            repository.addRoom(new Room(movie1.getId(), time1, day, "RoomName", 7, 13));
                        }
                        day++;
                    }
                }

            } catch (DuplicateData | NullParameterPassed error) {
                error.printStackTrace();
            }
        }
    }


    private List<List<String>> generateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        // Generate random integers in range 0 to 999
        LocalTime time1 = LocalTime.of(8, 0, 0);
        LocalTime time2 = LocalTime.of(15, 0, 0);
        int secondOfDayTime1 = time1.toSecondOfDay();
        int secondOfDayTime2 = time2.toSecondOfDay();
        List<List<String>> lists = new ArrayList<>();
        for (int day = 0; day < 7; day++) {
            int numberOfHours = random.nextInt(12) + 1;
            List<String> dayHours = new ArrayList<>();
            for (int hour = 0; hour < numberOfHours; hour++) {
                int randomSecondOfDay = secondOfDayTime1 + random.nextInt(secondOfDayTime2 - secondOfDayTime1);
                LocalTime randomLocalTime = LocalTime.ofSecondOfDay(randomSecondOfDay);
                dayHours.add(randomLocalTime.format(dtf));

            }
            lists.add(dayHours);
        }
        return lists;
    }

}
