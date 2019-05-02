package app.controller;

import app.controller.services.CookieHandler;
import app.controller.services.ICookieService;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryMovie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminMoviesController
{
    @Autowired
    IRepositoryMovie repository;

    @GetMapping(value = "/admin-movies")
    public String showMovies(HttpServletRequest request, HttpServletResponse response, Model model)
    {
        cookieService.setConfig(request,response);

        if (!cookieService.isConnected())
            return "error403";

        model.addAttribute("movies", repository.findAll());
        return "AdminMovies";
    }

    @PostMapping(value = "/admin-delete-movies")
    public String deleteMovie(@RequestParam(name = "movie-ids", required = false) List<String> movieIds)
    {
        if (movieIds != null)
            for (String id : movieIds)
                repository.deleteById(id);

        return RETURNED_STRING;
    }

    @PostMapping(value = "/admin-add-movie")
    public String addMovie(@RequestParam(name = "movie-name", required = true) String movieName,
                          @RequestParam(name = "movie-duration", required = true) int movieDuration,
                          @RequestParam(name = "movie-price", required = true) int moviePrice,
                          @RequestParam(name = "movie-image-path", required = true) String movieImagePath)
    {
        if (!movieName.trim().isEmpty() && !movieImagePath.trim().isEmpty())
            repository.save(new Movie(movieName, movieDuration, moviePrice, movieImagePath));

        return RETURNED_STRING;
    }

    @GetMapping(value = "/admin-edit-movie")
    public String toEditMoviePage(@RequestParam(name = "id", required = true) String movieId, Model model)
    {
        Optional<Movie> optionalMovie = repository.findById(movieId);

        if (optionalMovie.isPresent())
        {
            model.addAttribute("movie", optionalMovie.get());
            return "AdminMoviesEdit";
        }

        return RETURNED_STRING;
    }

    @PostMapping(value = "/admin-movies-submit-edit")
    public String saveMovie(@RequestParam(name = "new-movie-name") String newMovieName,
                            @RequestParam(name = "new-movie-duration") int newMovieDuration,
                            @RequestParam(name = "new-movie-price") int newMoviePrice,
                            @RequestParam(name = "new-movie-image-path") String newMovieImagePath,
                            @RequestParam(name = "movie-id") String movieId)
    {
        Optional<Movie> optionalMovie = repository.findById(movieId);

        if (optionalMovie.isPresent())
        {
            Movie movie = optionalMovie.get();

            if (!newMovieName.trim().isEmpty())
                movie.setTitle(newMovieName);

            if (newMovieDuration != 0)
                movie.setDuration(newMovieDuration);

            if (newMoviePrice != 0)
                movie.setPrice(newMoviePrice);

            if (!newMovieImagePath.trim().isEmpty())
                movie.setPath(newMovieImagePath);

            repository.save(movie);
        }

        return RETURNED_STRING;
    }

    private static final String RETURNED_STRING = "redirect:/admin-movies";
    @Autowired
    private ICookieService cookieService;
}
