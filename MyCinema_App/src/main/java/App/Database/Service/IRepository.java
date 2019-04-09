package App.Database.Service;

import App.Database.Entities.*;
import App.Database.Exception.*;

import java.time.LocalTime;
import java.util.List;


public interface IRepository {
    //Movie
    Movie movieFindByTitle(String Title);

    List<Movie> movieFindByDuration(Integer duration) throws InvalidDurationMovie;

    List<Movie> movieFindByDurationLessThan(Integer duration) throws InvalidDurationMovie;

    List<Movie> movieFindByDurationGreaterThan(Integer duration) throws InvalidDurationMovie;

    List<Movie> movieFindByTitleContaining(String title) throws NullTitle;

    List<Movie> movieFindByDurationBetween(Integer from, Integer to) throws InvalidDurationBetween;

    void addMovie(Movie movie) throws DuplicateData, NullParameterPassed;


    //    GenreMovie

    void addGenreToMovie(String movieId, String genre) throws NullParameterPassed, DuplicateData;

    GenreMovie genreMovieFindByMovieId(String movieId) throws NullParameterPassed;

    List<GenreMovie> genreMovieFindByGenre(String genre) throws NullParameterPassed;


    //rezervation
    void addRezervation(Rezervetion rezervetion) throws NullParameterPassed, DuplicateData;

    Rezervetion rezervationFindByMovieId(String movieId) throws NullParameterPassed;

    Rezervetion rezervationFindByRoomId(String roomId) throws NullParameterPassed;

    Rezervetion rezervationFindByDay(Integer day);

    Rezervetion rezervationFindByTime(LocalTime time);

    //Room
    Room roomFindById(String id) throws NullParameterPassed;

    void addRoom(Room room) throws NullParameterPassed, DuplicateData;

    //Screaming
    void addScreening(ScreeningHours screeningHours) throws NullParameterPassed, DuplicateData;

    ScreeningHours screamingFindById(String id) throws NullParameterPassed;

    ScreeningHours screeningFindByMovieId(String movieId) throws NullParameterPassed;

    //User
    void addUser(User user) throws NullParameterPassed, DuplicateData;

    User userFindByFirstName(String firstName) throws NullParameterPassed;

    List<User> userFindAllByLastName(String lastName) throws NullParameterPassed;

    User userFindByEmail(String email) throws NullParameterPassed;

    User userFindById(String id) throws NullParameterPassed;
}
