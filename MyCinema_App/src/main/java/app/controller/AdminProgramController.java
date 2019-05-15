package app.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import app.controller.services.ICookieService;
import app.database.entities.CinemaRoom;
import app.database.entities.Movie;
import app.database.entities.ScreeningHours;
import app.database.infrastructure.IRepositoryCinemaRoom;
import app.database.infrastructure.IRepositoryMovie;
import app.database.infrastructure.IRepositoryScreeningHours;
import lombok.Getter;
import lombok.ToString;
import org.bson.types.ObjectId;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ToString
@Getter
class ScreeningResult
{
    private String id;
    private String date;
    private String time;

    private Movie movie;
    private CinemaRoom room;
}

@Controller
public class AdminProgramController {

    @Autowired
    private ICookieService cookieService;

    @Autowired
    private IRepositoryMovie movieRepository;

    @Autowired
    private IRepositoryCinemaRoom cinemaRoomRepository;

    @Autowired
    private IRepositoryScreeningHours screeningHoursRepository;

    private static final String ERROR_MESSAGES = "errorMessages";
    private static final String SUCCESSFUL_MESSAGES = "successfulMessages";
    private static final String REDIRECT_TO_ADMIN_PROGRAM = "redirect:/admin-program";


    List<String> errorMessages;
    List<String> successfulMessages;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping(value = "/admin-program")
    public String showProgram(HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {
        cookieService.setConfig(request, response);

        if (!cookieService.isConnected())
            return "error403";

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
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("rooms", cinemaRoomRepository.findAll());

        return "AdminProgram";
    }

    @PostMapping(value = "/admin-delete-program")
    public String deleteProgram(@RequestParam(name = "program-ids", required = false) List<String> programIds,
                                RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();
        if (programIds == null) {
            errorMessages.add("Nu ai selectat niciun program");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_PROGRAM;
        } else {
            for (String id : programIds) {
                Optional<ScreeningHours> screeningHour = screeningHoursRepository.findById(id);
                if (!screeningHour.isPresent()) {
                    errorMessages.add("Un program pe care ai incercat sa il elimini nu mai exista.");
                } else {

                    screeningHoursRepository.deleteById(id);
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

    @PostMapping(value = "/admin-add-program")
    public String addMovieInProgram(@RequestParam(name = "movie-id") String movieId,
                                    @RequestParam(name = "room-id") String roomId,
                                    @RequestParam(name = "date") String date,
                                    @RequestParam(name = "time") String time,
                                    RedirectAttributes redirectAttributes) throws ParseException {

        ScreeningHours screeningHours = new ScreeningHours();

        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        if (movieId.equals("empty")) {
            errorMessages.add("Trebuie sa selectezi un film.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        if (roomId.equals("empty")) {
            errorMessages.add("Trebuie sa selectezi o camera.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        if (date.isEmpty()) {
            errorMessages.add("Trebuie sa selectezi o data.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        if (time.isEmpty()) {
            errorMessages.add("Trebuie sa selectezi o ora.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        screeningHours.setMovieId(new ObjectId(movieId));
        screeningHours.setRoomId(new ObjectId(roomId));

        long diff = 0;
        long allowedTime = 3600;
        try {
            Date oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);
            diff = oldDate.getTime() - new Date().getTime();
            diff /= 1000; //in seconds

        } catch (ParseException e) {
            e.getStackTrace();
        }

        boolean canSave = true;

        if (diff < allowedTime)
            canSave = false;

        screeningHours.setDate(date);
        screeningHours.setTime(time);

        if (!canSave) {
            errorMessages.add("Poti pune filme la ora " + (Date.from(new Date().toInstant().plus(Duration.ofHours(allowedTime / 3600)))));
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        successfulMessages.add("Ai adaugat un film pe data de " + date + " la ora " + time + " cu succes.");
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);
        screeningHoursRepository.save(screeningHours);

        return REDIRECT_TO_ADMIN_PROGRAM;
    }

    @GetMapping(value = "/admin-edit-program")
    public String toEditProgramPage(@RequestParam(name = "id") String screeningId, Model model) {
        Optional<ScreeningHours> screeningHour = screeningHoursRepository.findById(screeningId);
        errorMessages = new ArrayList<>();

        if (screeningHour.isPresent()) {
            model.addAttribute("screeningHour", screeningHour.get());
            return "AdminProgramEdit";
        } else {
            errorMessages.add("Programul pe care ai incercat sa il modifici nu mai exista in baza de date");
            return REDIRECT_TO_ADMIN_PROGRAM;
        }
    }

    @PostMapping(value = "/admin-program-submit-edit")
    public String saveMovie(@RequestParam(name = "new-program-movie-title", required = false) String newMovieTitle,
                            @RequestParam(name = "new-program-room-name", required = false) String newRoomName,
                            @RequestParam(name = "new-date", required = false) String newDate,
                            @RequestParam(name = "new-time", required = false) String newTime,
                            @RequestParam(name = "program-id") String programId,
                            RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        Optional<ScreeningHours> optionalScreeningHours = screeningHoursRepository.findById(programId);

        if (!optionalScreeningHours.isPresent()) {
            errorMessages.add("Programul pe care ai incercat sa il elimini nu mai exista in baza de date.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        ScreeningHours screeningHours = optionalScreeningHours.get();

        if (!newMovieTitle.isEmpty()) {
            //screeningHours.setMovieId(newMovieTitle);      ///////////// Title in loc de Id
        }

        if (!newRoomName.isEmpty()) {
            //screeningHours.setRoomId(newRoomName);         ///////////// Name in loc de Id
        }

        long diff = 0;
        long allowedTime = 3600;
        try {
            Date oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(newDate + " " + newTime);
            diff = oldDate.getTime() - new Date().getTime();
            diff /= 1000; //in seconds

        } catch (ParseException e) {
            e.getStackTrace();
        }

        boolean canSave = true;

        if (diff < allowedTime)
            canSave = false;


        screeningHours.setDate(newDate);
        screeningHours.setTime(newTime);

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return "redirect:/admin-edit-program?id=" + screeningHours.getId();
        }

        if (!canSave) {
            errorMessages.add("Poti pune filme la ora " + (Date.from(new Date().toInstant().plus(Duration.ofHours(allowedTime / 3600)))));
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        screeningHoursRepository.save(screeningHours);
        successfulMessages.add("Ai modificat programul cu succes");
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);

        return REDIRECT_TO_ADMIN_PROGRAM;
    }


}