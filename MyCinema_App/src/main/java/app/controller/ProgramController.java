package app.controller;

import app.controller.services.ICookieService;
import app.database.entities.Movie;
import app.database.entities.ScreeningHours;
import app.database.infrastructure.IRepositoryMovie;
import app.database.infrastructure.IRepositoryUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
class ProgramResult
{
    private String id;
    private String title;
    private int seconds;
    private double price;
    private String path;
    private String createdDate;

    private List<ScreeningHours> screenings;

    public List<ScreeningHours> screeningsByDay(String day)
    {
        final int dayOfWeek = Integer.parseInt(day) + 1;

        if (dayOfWeek < 1 || dayOfWeek > 7)
            return screenings;

        List<ScreeningHours> filtered = new ArrayList<ScreeningHours>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d", Locale.ENGLISH);

        for (ScreeningHours screening : screenings)
        {
            LocalDate date = LocalDate.parse(screening.getDate(), formatter);

            if (dayOfWeek == date.getDayOfWeek().getValue())
                filtered.add(screening);
        }

        return filtered;
    }

    public List<ScreeningHours> screeningsByMovieId(String movieId)
    {
        if (movieId.isEmpty())
            return screenings;

        List<ScreeningHours> filtered = new ArrayList<ScreeningHours>();

        for (ScreeningHours screening : screenings)
        {
            if (screening.getMovieId().toString().equals(movieId))
                filtered.add(screening);
        }

        return filtered;
    }
}

@Controller
public class ProgramController {
    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private IRepositoryMovie movieRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ICookieService cookieService;

    @GetMapping("/program")
    public String getProgram(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam(name = "day", defaultValue = "0") String day,
                             @RequestParam(name = "idMovie", defaultValue = "") String idMovie)
    {
        LookupOperation lookupScreenings = LookupOperation.newLookup()
                .from("Screening")
                .localField("_id")
                .foreignField("movieId")
                .as("screenings");

        Aggregation aggregation = Aggregation.newAggregation(lookupScreenings);
        List<ProgramResult> programs = mongoTemplate.aggregate(aggregation, "Movies", ProgramResult.class).getMappedResults();

        programs.stream().forEach(e -> e.setScreenings(e.screeningsByDay(day)));
        programs.stream().forEach(e -> e.setScreenings(e.screeningsByMovieId(idMovie)));
        programs = programs.stream().filter(e -> !e.getScreenings().isEmpty()).collect(Collectors.toList());

        model.addAttribute("programs", programs);
        model.addAttribute("day", day);

        return checkForAccess(model, request, response);
    }

    private String checkForAccess(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        cookieService.setConfig(request,response);
        if (!cookieService.isConnected()) return "error403";

        else {
            model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
            return "program";
        }
    }
}
