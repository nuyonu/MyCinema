package app.controller;

import app.controller.services.ICookieService;
import app.database.entities.CinemaRoom;
import app.database.entities.Movie;
import app.database.entities.ScreeningHours;
import app.database.infrastructure.IRepositoryCinemaRoom;
import app.database.infrastructure.IRepositoryMovie;
import app.database.infrastructure.IRepositoryScreeningHours;
import app.database.infrastructure.IRepositoryUser;
import lombok.Getter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ToString
@Getter
class ScreeningResult {
    private String id;
    private String date;
    private String time;

    private Movie movie;
    private CinemaRoom room;
}

@Controller
public class AdminProgramController {
    private static final String ERROR_MESSAGES = "errorMessages";
    private static final String SUCCESSFUL_MESSAGES = "successfulMessages";
    private static final String REDIRECT_TO_ADMIN_PROGRAM = "redirect:/admin-program";
    private static final String REDIRECT_TO_ADMIN_ADD_PROGRAM = "redirect:/admin-add-program";
    private static final int ALLOWED_TIME = 3600;

    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private ICookieService cookieService;

    @Autowired
    private IRepositoryMovie movieRepository;

    @Autowired
    private IRepositoryCinemaRoom cinemaRoomRepository;

    @Autowired
    private IRepositoryScreeningHours repositoryScreeningHours;

    private List<String> errorMessages;
    private List<String> successfulMessages;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Logger logger = LoggerFactory.getLogger(AdminProgramController.class);

    @GetMapping(value = "/admin-program")
    public String showProgram(HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {
        cookieService.setConfig(request, response);

        if (!cookieService.isConnected())
            return "redirect:/Login";

//        deleteScreenings(repositoryScreeningHours.findAll());

        LookupOperation lookupMovies = LookupOperation.newLookup()
                .from("Movies")
                .localField("movieId")
                .foreignField("_id")
                .as("movie");

        LookupOperation lookupRooms = LookupOperation.newLookup()
                .from("CinemaRooms")
                .localField("roomId")
                .foreignField("_id")
                .as("room");

        Aggregation aggregation = Aggregation.newAggregation(lookupMovies, lookupRooms);
        List<ScreeningResult> screenings = mongoTemplate.aggregate(aggregation, "Screening", ScreeningResult.class).getMappedResults();
        model.addAttribute("screenings", screenings);
        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));

        return "AdminProgram";
    }

    @PostMapping(value = "/admin-delete-program")
    public String deleteProgram(@RequestParam(name = "program-ids", required = false) List<String> programIds,
                                RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();
        if (programIds == null) {
            errorMessages.add("You have not selected any programs");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_PROGRAM;
        } else {
            for (String id : programIds) {
                Optional<ScreeningHours> screeningHour = repositoryScreeningHours.findById(id);
                if (!screeningHour.isPresent()) {
                    errorMessages.add("Un program pe care ai incercat sa il elimini nu mai exista.");
                } else {

                    repositoryScreeningHours.deleteById(id);
                }
            }
        }

        if (programIds.size() == 1)
            successfulMessages.add("You have successfully removed the selected program");
        else
            successfulMessages.add("You removed the selected programs successfully");

        redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);

        return REDIRECT_TO_ADMIN_PROGRAM;
    }

    @GetMapping("/admin-add-program")
    public String redirectToAdminProgramAdd(HttpServletRequest request, HttpServletResponse response, Model model) {
        cookieService.setConfig(request, response);

        if (!cookieService.isConnected())
            return "redirect:/Login";

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("rooms", cinemaRoomRepository.findAll());
        return "AdminProgramAdd";
    }

    @PostMapping("/admin-add-program-submit")
    public String addMovieInProgram(@RequestParam(name = "movie-id") String movieId,
                                    @RequestParam(name = "room-id") String roomId,
                                    @RequestParam(name = "date") String date,
                                    @RequestParam(name = "time") String time,
                                    RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        if (!movieRepository.findById(movieId).isPresent()) {
            errorMessages.add("The chosen movie does not exist in the database");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
        }

        if (!cinemaRoomRepository.findById(roomId).isPresent()) {
            errorMessages.add("The chosen room does not exist in the database");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
        }

        if (date.isEmpty()) {
            errorMessages.add("You must select a date");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
        }

        if (time.isEmpty()) {
            errorMessages.add("You must select an hour");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
        }

        long diff = 0;
        //seconds
        try {
            Date oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);
            diff = oldDate.getTime() - new Date().getTime();
            diff /= 1000; //in seconds

        } catch (ParseException e) {
            String error = "Date parse error " + e;
            logger.error(error);
        }

        if (diff < ALLOWED_TIME)
            errorMessages.add("You can add a movie to the program starting at " + new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date().getTime() + ALLOWED_TIME * 1000));

        Optional<CinemaRoom> room = cinemaRoomRepository.findById(roomId);
        if (room.isPresent())
            repositoryScreeningHours.save(new ScreeningHours(new ObjectId(movieId), new ObjectId(roomId), date, time, room.get().getSeats()));
        else
            errorMessages.add("The selected room no longer exists");

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_ADD_PROGRAM;
        }

        successfulMessages.add("You added a movie on " + date + " at time " + time + " successfully.");
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);

        return REDIRECT_TO_ADMIN_PROGRAM;
    }

    private void deleteScreenings(List<ScreeningHours> screeningHours) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (ScreeningHours screeningHour : screeningHours) {
            try {
                if (simpleDateFormat.parse(screeningHour.getDate() + " " + screeningHour.getTime()).before(new Date(new Date().getTime() + ALLOWED_TIME - 600 * 1000))) {
                    repositoryScreeningHours.delete(screeningHour);
                }
            } catch (ParseException e) {
                String error = "Parse date error " + e;
                logger.error(error);
            }
        }
    }
}