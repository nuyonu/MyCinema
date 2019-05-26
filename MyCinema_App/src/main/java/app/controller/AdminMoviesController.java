package app.controller;

import app.controller.services.CommonFunctions;
import app.controller.services.ICookieService;
import app.database.service.StatisticsService;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryMovie;
import app.database.infrastructure.IRepositoryUser;
import app.database.utils.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminMoviesController {
    private static final String REDIRECT_TO_ADMIN_MOVIES = "redirect:/admin-movies";
    private static final String ERROR_MESSAGES = "errorMessages";
    private static final String SUCCESSFUL_MESSAGES = "successfulMessages";
    private static final String REDIRECT_TO_LOGIN = "redirect:/Login";

    @Autowired
    private IRepositoryMovie repositoryMovie;

    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private ICookieService cookieService;

    private List<String> errorMessages;
    private List<String> successfulMessages;

    private Logger logger = LoggerFactory.getLogger(AdminMoviesController.class);

    @GetMapping(value = "/admin-movies")
    public String showMovies(@RequestParam(value = "movieTitle", required = false, defaultValue = "") String movieTitle,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             Model model) {
        cookieService.setConfig(request, response);

        if (!cookieService.isConnected())
            return REDIRECT_TO_LOGIN;

        if (!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            return "noAccess";

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
        model.addAttribute("movies", repositoryMovie.findAllByTitleContainingOrderByCreatedDateDesc(movieTitle));
        model.addAttribute("currentMovieTitle", movieTitle);
        return "AdminMovies";
    }

    @PostMapping(value = "/admin-delete-movies")
    public String deleteMovie(@RequestParam(name = "movie-ids", required = false) List<String> movieIds,
                              RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();
        if (movieIds == null) {
            errorMessages.add("You have not selected any movie");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_MOVIES;
        } else {
            for (String id : movieIds) {
                Optional<Movie> movie = repositoryMovie.findById(id);
                if (!movie.isPresent()) {
                    errorMessages.add("A movie you tried to remove does not exist anymore");
                } else {
                    try {
                        Files.deleteIfExists(Paths.get("src/main/resources/static/images/movieImages/"
                                + movie.get().getId() + ".jpg"));
                    } catch (IOException e) {
                        String error = "Couldn't delete profile file: " + e;
                        logger.error(error);
                    }
                    repositoryMovie.deleteById(id);
                }
            }
        }

        if (movieIds.size() == 1)
            successfulMessages.add("You have successfully removed the selected movie");
        else
            successfulMessages.add("You removed the selected movies successfully");

        redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);

        return REDIRECT_TO_ADMIN_MOVIES;
    }

    @GetMapping("/admin-add-movie")
    public String getAddMovie(HttpServletRequest request, HttpServletResponse response, Model model) {
        cookieService.setConfig(request, response);

        if (!cookieService.isConnected())
            return REDIRECT_TO_LOGIN;

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
        return "AdminMoviesAdd";
    }

    @PostMapping(value = "/admin-add-movie-submit")
    public String addMovie(@RequestParam(name = "movie-title") String movieTitle,
                           @RequestParam(name = "movie-seconds") String movieSeconds,
                           @RequestParam(name = "movie-price") String moviePrice,
                           @RequestParam(name = "movie-image") MultipartFile movieImage,
                           RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        Movie movie = new Movie();

        movie.setTitle(movieTitle);
        movie.setSeconds(Integer.parseInt(movieSeconds));

        double price = 0.0;

        //Price must be double(24.4, 1, etc).
        try {
            price = Double.parseDouble(moviePrice);
        } catch (Exception e) {
            errorMessages.add("The price entered is incorrect");
        }

        movie.setPrice(price);
        repositoryMovie.save(movie);

        //Save movie Image
        if (!movieImage.isEmpty())
            saveMovieImage(movieImage, movie);

        //Have errors
        if (!errorMessages.isEmpty()) {
            repositoryMovie.delete(movie);
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return "redirect:/admin-add-movie";
        }

        //Success
        repositoryMovie.save(movie);

        statisticsService.increaseTotalMovies();

        successfulMessages.add("You added the movie: " + movieTitle + " successfully.");
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);
        return REDIRECT_TO_ADMIN_MOVIES;
    }

    @GetMapping(value = "/admin-edit-movie")
    public String toEditMoviePage(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestParam(name = "id") String movieId,
                                  Model model) {
        Optional<Movie> movie = repositoryMovie.findById(movieId);
        errorMessages = new ArrayList<>();

        cookieService.setConfig(request, response);
        if (!cookieService.isConnected())
            return REDIRECT_TO_LOGIN;

        if (!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            return "noAccess";

        if (movie.isPresent()) {
            model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
            model.addAttribute("movie", movie.get());
            return "AdminMoviesEdit";
        } else {
            errorMessages.add("The movie you tried to modify is no longer in the database");
            return REDIRECT_TO_ADMIN_MOVIES;
        }
    }

    @PostMapping(value = "/admin-movies-submit-edit")
    public String saveMovie(@RequestParam(name = "new-movie-title", required = false) String newMovieTitle,
                            @RequestParam(name = "new-movie-duration", required = false, defaultValue = "0") String newMovieDuration,
                            @RequestParam(name = "new-movie-price", required = false) String newMoviePrice,
                            @RequestParam(name = "new-movie-image", required = false) MultipartFile newMovieImage,
                            @RequestParam(name = "movie-id") String movieId,
                            RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        Optional<Movie> optionalMovie = repositoryMovie.findById(movieId);

        if (!optionalMovie.isPresent()) {
            errorMessages.add("The movie you tried to remove is no longer in the database");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_MOVIES;
        }

        Movie movie = optionalMovie.get();

        if (!newMovieTitle.isEmpty()) {
            movie.setTitle(newMovieTitle);
        }

        if (!newMovieDuration.equals("0"))
            movie.setSeconds(Integer.parseInt(newMovieDuration));

        double price = movie.getPrice();

        //Price must be double(24.4, 1, etc).
        if (!newMoviePrice.isEmpty())
            try {
                price = Double.parseDouble(newMoviePrice);
            } catch (Exception e) {
                errorMessages.add("The price entered is incorrect");
            }
        if (price < 0)
            errorMessages.add("The price you entered is incorrect, it can not be negative");
        movie.setPrice(price);

        if (!newMovieImage.isEmpty())
            saveMovieImage(newMovieImage, movie);

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return "redirect:/admin-edit-movie?id=" + movie.getId();
        }

        repositoryMovie.save(movie);
        successfulMessages.add("You've changed the movie " + movie.getTitle() + " successfully");
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);
        return REDIRECT_TO_ADMIN_MOVIES;
    }

    @GetMapping("/images/movieImages/{imageId}")
    @ResponseBody
    public byte[] getImage(@PathVariable String imageId) {
        if (!imageId.matches("[a-zA-Z0-9.]++"))
            return new byte[0];
        else {
            Path path = Paths.get("src/main/resources/static/images/movieImages/" + imageId);
            if (Files.exists(path))
                return CommonFunctions.imageFromPath(path);
            return new byte[0];
        }
    }

    private void saveMovieImage(MultipartFile movieImage, Movie movie) {
        if (movieImage.getContentType().equals("image/gif") ||
                movieImage.getContentType().equals("image/jpeg") ||
                movieImage.getContentType().equals("image/png")) {
            final String folder = System.getProperty("user.dir") + "/src/main/resources/static/images/movieImages/";

            try {
                byte[] bytes = movieImage.getBytes();
                Path path = Paths.get(folder + movie.getId() + ".jpg");
                Files.write(path, bytes);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            movie.setPath("/images/movieImages/" + movie.getId() + ".jpg");
        } else
            errorMessages.add("You can only upload images with the .jpg / .jpeg, .png, .gif extension");
    }
}
