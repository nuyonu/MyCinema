package app.controller;

import app.controller.services.ICookieService;
import app.database.entities.Reservation;
import app.database.entities.ScreeningHours;
import app.database.infrastructure.IRepositoryReservation;
import app.database.infrastructure.IRepositoryRoom;
import app.database.infrastructure.IRepositoryScreeningHours;
import app.database.infrastructure.IRepositoryUser;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservationController {
    private static final String ERROR_MESSAGES = "errorMessages";
    private static final String REDIRECT_TO_PROGRAM = "redirect:/program";
    private static final int RESERVED = 1;

    @Autowired
    IRepositoryRoom service;

    @Autowired
    IRepositoryScreeningHours repositoryScreeningHours;

    @Autowired
    IRepositoryUser repositoryUser;

    @Autowired
    IRepositoryReservation repositoryReservation;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ICookieService cookieService;

    private List<String> errorMessages;

    @PostMapping(value = "reservation")
    public String reservation(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam(value = "idScreeningHour") String idScreeningHour,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        cookieService.setConfig(request, response);
        if (!cookieService.isConnected())
            return "error403";

        Optional<ScreeningHours> screeningHour = repositoryScreeningHours.findById(idScreeningHour);

        if (!screeningHour.isPresent()) {
            errorMessages.add("You can no longer reserve for this program");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_PROGRAM;
        } else {
            model.addAttribute("seats", screeningHour.get().getSeats());
        }

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

        Optional<ScreeningResult> screening = screenings.
                stream().
                filter(screeningResult -> screeningResult.getId().equals(idScreeningHour)).findAny();

        if (screening.isPresent())
            model.addAttribute("screening", screening.get());

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));

        return "Room";
    }

    @PostMapping(value = "/book_ticket")
    public String book(@RequestParam(value = "screening-id", required = false, defaultValue = "0") String screeningId,
                       @RequestParam(value = "seat-row", required = false, defaultValue = "0") String rowString,
                       @RequestParam(value = "seat-col", required = false, defaultValue = "0") String columnString,
                       RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        Optional<ScreeningHours> screeningHours = repositoryScreeningHours.findById(screeningId);

        if(screeningId.equals("0") || rowString.equals("0") || columnString.equals("0")) {
            return REDIRECT_TO_PROGRAM;
        }

        if (!screeningHours.isPresent()) {
            errorMessages.add("The program no longer exists");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_PROGRAM;
        }

        int row = Integer.parseInt(rowString);
        int column = Integer.parseInt(columnString);

        if (screeningHours.get().getSeats().get(row).get(column) == RESERVED) {
            errorMessages.add("This chair has been reserved in the meantime");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return "redirect:/reservation?idScreeningHour=" + screeningId;
        }

        screeningHours.get().getSeats().get(row).set(column, RESERVED);

        repositoryReservation.save(new Reservation(new ObjectId(repositoryUser.findByUsername(cookieService.getUser()).getId()),
                new ObjectId(screeningId)));

        return "redirect:/reservation?idScreeningHour=" + screeningId;
    }
}
