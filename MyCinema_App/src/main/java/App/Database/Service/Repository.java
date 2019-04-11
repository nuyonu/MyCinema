package App.Database.Service;

import App.Database.Entities.*;
import App.Database.Exception.*;
import App.Database.Infrastructure.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class Repository implements IRepository {
    @Autowired
    IRepositoryMovie iRepositoryMovie;
    @Autowired
    IRepositoryUser user;
    @Autowired
    IRepositoryGenreMovie genreMovie;
    @Autowired
    IRepositoryRezervation rezervation;
    @Autowired
    IRepositoryRoom room;
    @Autowired
    IRepositoryScreaningHours screaningHours;


    @Override
    public Movie movieFindByTitle(String Title) {
        Movie movie = iRepositoryMovie.findByTitle(Title);
        return movie != null ? movie : new Movie("", 100);
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
            //todo throw ex
            throw new DuplicateData();
        }

    }


    @Override
    public void addGenreToMovie(String movieId, String genre) throws NullParameterPassed, DuplicateData {
        if (movieId == null || genre == null) throw new NullParameterPassed();
        try {
            genreMovie.save(new GenreMovie(movieId, genre));
        } catch (DuplicateKeyException e) {
            //todo throw ex
            throw new DuplicateData();
        }

    }

    @Override
    public GenreMovie genreMovieFindByMovieId(String movieId) throws NullParameterPassed {
        if (movieId == null) throw new NullParameterPassed();
        GenreMovie genreMovie = this.genreMovie.findByMovieId(movieId);
        return genreMovie != null ? genreMovie : new GenreMovie();
    }

    @Override
    public List<GenreMovie> genreMovieFindByGenre(String genre) throws NullParameterPassed {
        if (genre == null) throw new NullParameterPassed();
        return genreMovie.findByGenre(genre);
    }

    @Override
    @Transactional
    public void addRezervation(Reservation reservation) throws NullParameterPassed, DuplicateData {
        if (reservation == null) throw new NullParameterPassed();
        try {
            this.rezervation.save(reservation);
        } catch (DuplicateKeyException e) {
            throw new DuplicateData();
        }
    }

    @Override
    public Reservation rezervationFindByMovieId(String movieId) throws NullParameterPassed {
        if (movieId == null) throw new NullParameterPassed();
        Reservation reservation = this.rezervation.findByMovieId(movieId);
        return reservation != null ? reservation : new Reservation();

    }


    @Override
    public Reservation rezervationFindByRoomId(String roomId) throws NullParameterPassed {
        if (roomId == null) throw new NullParameterPassed();
        Reservation reservation = this.rezervation.findByRoomId(roomId);
        return reservation != null ? reservation : new Reservation();

    }

    @Override
    public Reservation rezervationFindByDay(Integer day) {
        Reservation reservation = this.rezervation.findByDay(day);
        return reservation != null ? reservation : new Reservation();

    }

    @Override
    public Reservation rezervationFindByTime(LocalTime time) {
        Reservation reservation = this.rezervation.findByTime(time);
        return reservation != null ? reservation : new Reservation();
    }

    @Override
    public Room roomFindById(String id) throws NullParameterPassed {
        if (id == null) throw new NullParameterPassed();
        Room room = this.room.findById(id).get();
        return room;
    }

    @Override
    @Transactional
    public void addRoom(Room room) throws DuplicateData, NullParameterPassed {
        if (room == null) throw new NullParameterPassed();
        try {
            this.room.save(room);
        } catch (DuplicateKeyException e) {
            //todo throw ex
            throw new DuplicateData();
        }
    }

    @Override
    public void addScreening(ScreeningHours screeningHours) throws NullParameterPassed, DuplicateData {
        if (screeningHours == null) throw new NullParameterPassed();
        try {
            this.screaningHours.save(screeningHours);
        } catch (DuplicateKeyException e) {
            //todo throw ex
            throw new DuplicateData();
        }
    }

    @Override
    public ScreeningHours screamingFindById(String id) throws NullParameterPassed {
        if (id == null) throw new NullParameterPassed();
        try {
            ScreeningHours screeningHours = this.screaningHours.findById(id).get();
            return screeningHours;
        } catch (NoSuchElementException e) {
            return new ScreeningHours();
        }

    }

    @Override
    public ScreeningHours screeningFindByMovieId(String movieId) throws NullParameterPassed {
        if (movieId == null) throw new NullParameterPassed();
        ScreeningHours screeningHours = this.screaningHours.findByMovieId(movieId);
        return screeningHours != null ? screeningHours : new ScreeningHours();

    }

    @Override
    @Transactional
    public void addUser(User user) throws NullParameterPassed, DuplicateData {
        if (user == null) throw new NullParameterPassed();
        try {
            this.user.save(user);
        } catch (DuplicateKeyException e) {
            //todo throw ex
            throw new DuplicateData();
        }
    }

    @Override
    public User userFindByFirstName(String firstName) throws NullParameterPassed {
        if (firstName == null) throw new NullParameterPassed();
        User user = this.user.findByFirstName(firstName);
        return user != null ? user : new User();

    }

    @Override
    public List<User> userFindAllByLastName(String lastName) throws NullParameterPassed {
        if (lastName == null) throw new NullParameterPassed();
        return this.user.findAllByLastName(lastName);
    }

    @Override
    public User userFindByEmail(String email) throws NullParameterPassed {
        if (email == null) throw new NullParameterPassed();
        User user = this.user.findByEmail(email);
        return user != null ? user : new User();
    }

    @Override
    public User userFindById(String id) throws NullParameterPassed {
        if (id == null) throw new NullParameterPassed();
        try {
            User user = this.user.findById(id).get();
            return user;
        } catch (NoSuchElementException e) {
            return new User();
        }

    }

    @Override
    public User userFindByUsername(String username) {
        User user= this.user.findByUsername(username);
        return  user!=null?user:new User();
    }
    @Override
    public void deleteRoom()
    {
        room.deleteAll();
    }

    @Override
    public Room findRoomByName(String name){
        return this.room.findByName(name);
    }

}

