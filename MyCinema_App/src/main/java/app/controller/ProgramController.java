package app.controller;

import app.controller.services.ICookieService;
import app.database.entities.ScreeningHours;
import app.database.infrastructure.IRepositoryUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    private ArrayList<ScreeningHours> screenings;
}

@Controller
public class ProgramController {
    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ICookieService cookieService;

    @GetMapping("/program")
    public String getProgram(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam(name = "date", defaultValue = "") String date,
                             @RequestParam(name = "idMovie", defaultValue = "") String idMovie)
    {
        List<Pair<String, String>> dates = new ArrayList<>();

        for (int day = 0; day < 7; ++day)
        {
            dates.add(Pair.of(
                LocalDateTime.now().plusDays(day).format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)),
                LocalDateTime.now().plusDays(day).format(DateTimeFormatter.ofPattern("EEEE, dd MMM", Locale.ENGLISH))
            ));
        }

        model.addAttribute("dates", dates);

        LookupOperation lookupScreenings = LookupOperation.newLookup()
                .from("Screening")
                .localField("_id")
                .foreignField("movieId")
                .as("screenings");

        Aggregation aggregation = Aggregation.newAggregation(lookupScreenings);
        ArrayList<ProgramResult> programs = new ArrayList<>(mongoTemplate.aggregate(aggregation, "Movies", ProgramResult.class).getMappedResults());

        for (ProgramResult programResult : programs)
        {
            if (!date.isEmpty())
                programResult.getScreenings().removeIf(s -> !s.getDate().equals(date));
            else
                programResult.getScreenings().removeIf(s -> !s.getDate().equals(dates.get(0).getFirst()));

            if (!idMovie.isEmpty())
                programResult.getScreenings().removeIf(s -> !s.getMovieId().toString().equals(idMovie));
        }

        programs.removeIf(e -> e.getScreenings().isEmpty());

        model.addAttribute("programs", programs);

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
