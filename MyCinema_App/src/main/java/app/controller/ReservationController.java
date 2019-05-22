package app.controller;

import app.controller.services.ICookieService;
import app.database.entities.CinemaRoom;
import app.database.entities.Movie;
import app.database.entities.ScreeningHours;
import app.database.infrastructure.IRepositoryRoom;
import app.database.infrastructure.IRepositoryScreeningHours;
import app.database.infrastructure.IRepositoryUser;
import lombok.Getter;
import lombok.ToString;
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
import java.util.List;
import java.util.Optional;

@Controller
public class ReservationController {
    @Autowired
    IRepositoryRoom service;

    @Autowired
    IRepositoryScreeningHours repositoryScreeningHours;

    @Autowired
    IRepositoryUser repositoryUser;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ICookieService cookieService;

    @PostMapping(value = "reservation")
    public String reservation(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam(value = "idScreeningHour") String idScreeningHour,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        cookieService.setConfig(request,response);
        if (!cookieService.isConnected())
            return "error403";

        Optional<ScreeningHours> screeningHour = repositoryScreeningHours.findById(idScreeningHour);

        if(!screeningHour.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessages","You can no longer reserve for this program");
            return "redirect:/program";
        }

        else {
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

        if(screening.isPresent())
            model.addAttribute("screening", screening.get());

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));

        return "Room";
    }
}
