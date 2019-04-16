package app.database.service;

import app.database.entities.*;
import app.database.exception.*;
import app.database.infrastructure.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Repository implements IRepository {
    @Autowired
    IRepositoryMovie iRepositoryMovie;
    @Autowired
    IRepositoryUser repositoryUser;
    @Autowired
    IRepositoryGenreMovie repositoryGenreMovie;
    @Autowired
    IRepositoryRezervation repositoryRezervation;
    @Autowired
    IRepositoryRoom repositoryRoom;
    @Autowired
    IRepositoryScreaningHours repositoryScreaningHours;


    @Override
    public Movie movieFindByTitle(String title) {
        Movie movie = iRepositoryMovie.findByTitle(title);
        return movie != null ? movie : new Movie();
    }

    @Override
    public List<Movie> movieFindByDuration(Integer duration) throws InvalidDurationMovie {
        if (duration <= 0) throw new InvalidDurationMovie(duration);
        return iRepositoryMovie.findByDuration(duration);
    }

    @Override
    public List<Movie> movieFindByDurationLessThan(Integer duration) throws InvalidDurationMovie {
        if (duration <= 0) throw new InvalidDurationMovie(duration);
        return iRepositoryMovie.findByDurationLessThan(duration);
    }

    @Override
    public List<Movie> movieFindByDurationGreaterThan(Integer duration) throws InvalidDurationMovie {
        if (duration <= 0) throw new InvalidDurationMovie(duration);
        return iRepositoryMovie.findByDurationGreaterThan(duration);
    }

    @Override
    public List<Movie> movieFindByTitleContaining(String title) throws NullTitle {
        if (title == null) throw new NullTitle();
        return iRepositoryMovie.findByTitleContaining(title);
    }

    @Override
    public List<Movie> movieFindByDurationBetween(Integer from, Integer to) throws InvalidDurationBetween {
        if (from < 0 || to < 0 || from > to) throw new InvalidDurationBetween(from, to);
        return iRepositoryMovie.findByDurationBetween(from, to);
    }

    @Override
    @Transactional
    public void addMovie(Movie movie) throws DuplicateData, NullParameterPassed {
        if (movie == null) throw new NullParameterPassed();
        try {
            iRepositoryMovie.save(movie);
        } catch (DuplicateKeyException e) {
            throw new DuplicateData();
        }

    }


    @Override
    public void addGenreToMovie(String movieId, String genre) throws NullParameterPassed, DuplicateData {
        if (movieId == null || genre == null) throw new NullParameterPassed();
        try {
            repositoryGenreMovie.save(new GenreMovie(movieId, genre));
        } catch (DuplicateKeyException e) {
            throw new DuplicateData();
        }

    }

    @Override
    public GenreMovie genreMovieFindByMovieId(String movieId) throws NullParameterPassed {
        if (movieId == null) throw new NullParameterPassed();
        GenreMovie genreMovie = this.repositoryGenreMovie.findByMovieId(movieId);
        return genreMovie != null ? genreMovie : new GenreMovie();
    }

    @Override
    public List<GenreMovie> genreMovieFindByGenre(String genre) throws NullParameterPassed {
        if (genre == null) throw new NullParameterPassed();
        return repositoryGenreMovie.findByGenre(genre);
    }

    @Override
    @Transactional
    public void addRezervation(Reservation reservation) throws NullParameterPassed, DuplicateData {
        if (reservation == null) throw new NullParameterPassed();
        try {
            this.repositoryRezervation.save(reservation);
        } catch (DuplicateKeyException e) {
            throw new DuplicateData();
        }
    }

    @Override
    public Reservation rezervationFindByMovieId(String movieId) throws NullParameterPassed {
        if (movieId == null) throw new NullParameterPassed();
        Reservation reservation = this.repositoryRezervation.findByMovieId(movieId);
        return reservation != null ? reservation : new Reservation();

    }


    @Override
    public Reservation rezervationFindByRoomId(String roomId) throws NullParameterPassed {
        if (roomId == null) throw new NullParameterPassed();
        Reservation reservation = this.repositoryRezervation.findByRoomId(roomId);
        return reservation != null ? reservation : new Reservation();

    }

    @Override
    public Reservation rezervationFindByDay(Integer day) {
        Reservation reservation = this.repositoryRezervation.findByDay(day);
        return reservation != null ? reservation : new Reservation();

    }

    @Override
    public Reservation rezervationFindByTime(LocalTime time) {
        Reservation reservation = this.repositoryRezervation.findByTime(time);
        return reservation != null ? reservation : new Reservation();
    }

    @Override
    public Room roomFindById(String id) throws NullParameterPassed {
        if (id == null) throw new NullParameterPassed();
        Optional<Room> room = this.repositoryRoom.findById(id);
        return room.orElseGet(Room::new);
    }

    @Override
    @Transactional
    public void addRoom(Room room) throws DuplicateData, NullParameterPassed {
        if (room == null) throw new NullParameterPassed();
        try {
            this.repositoryRoom.save(room);
        } catch (DuplicateKeyException e) {
            throw new DuplicateData();
        }
    }

    @Override
    public void addScreening(ScreeningHours screeningHours) throws NullParameterPassed, DuplicateData {
        if (screeningHours == null) throw new NullParameterPassed();
        try {
            this.repositoryScreaningHours.save(screeningHours);
        } catch (DuplicateKeyException e) {
            throw new DuplicateData();
        }
    }

    @Override
    public ScreeningHours screamingFindById(String id) throws NullParameterPassed {
        if (id == null) throw new NullParameterPassed();
        Optional<ScreeningHours> screeningHours = this.repositoryScreaningHours.findById(id);
        return screeningHours.orElseGet(ScreeningHours::new);
    }

    @Override
    public ScreeningHours screeningFindByMovieId(String movieId) throws NullParameterPassed {
        if (movieId == null) throw new NullParameterPassed();
        ScreeningHours screeningHours = this.repositoryScreaningHours.findByMovieId(movieId);
        return screeningHours != null ? screeningHours : new ScreeningHours();

    }

    @Override
    @Transactional
    public void addUser(User user) throws NullParameterPassed, DuplicateData {
        if (user == null) throw new NullParameterPassed();
        try {
            this.repositoryUser.save(user);
        } catch (DuplicateKeyException e) {
            throw new DuplicateData();
        }
    }

    @Override
    public User userFindByFirstName(String firstName) throws NullParameterPassed {
        if (firstName == null) throw new NullParameterPassed();
        User user = this.repositoryUser.findByFirstName(firstName);
        return user != null ? user : new User();

    }

    @Override
    public List<User> userFindAllByLastName(String lastName) throws NullParameterPassed {
        if (lastName == null) throw new NullParameterPassed();
        return this.repositoryUser.findAllByLastName(lastName);
    }

    @Override
    public User userFindByEmail(String email) throws NullParameterPassed {
        if (email == null) throw new NullParameterPassed();
        User user = this.repositoryUser.findByEmail(email);
        return user != null ? user : new User();
    }

    @Override
    public User userFindById(String id) throws NullParameterPassed {
        if (id == null) throw new NullParameterPassed();
        Optional<User> user = this.repositoryUser.findById(id);
        return user.orElseGet(User::new);

    }

    @Override
    public User userFindByUsername(String username) {
        User user = this.repositoryUser.findByUsername(username);
        return user != null ? user : new User();
    }

    @Override
    public void deleteRoom() {
        repositoryRoom.deleteAll();
    }

    @Override
    public Room findRoomByName(String name) {
        return this.repositoryRoom.findByName(name);
    }

    @Override
    public List<Movie> getAllMovies() {
        return iRepositoryMovie.findAll();
    }

    @Override
    public boolean isUsernameAlreadyInUse(String username) throws NullParameterPassed {
        if (username == null)
            throw new NullParameterPassed();
        return this.repositoryUser.findByUsername(username) != null;
    }

    @Override
    public Room findRoomReservation(String id, String time, Integer day) {
        return repositoryRoom.findByIdMovieAndTimeAndDay(id, time, day);
    }

    @Override
    public List<Movie> getAllMoviesById(String idMovie) {
        return iRepositoryMovie.findById(idMovie).stream().collect(Collectors.toList());
    }


}

