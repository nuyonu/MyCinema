package app.controller;

import app.controller.services.ICookieService;
import app.database.entities.Movie;
import app.database.entities.Statistics;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryMovie;
import app.database.infrastructure.IRepositoryStatistics;
import app.database.infrastructure.IRepositoryUser;
import app.database.utils.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

@Controller
public class StatisticsController {
    @Autowired
    IRepositoryUser repositoryUser;

    @Autowired
    IRepositoryStatistics repository;

    @Autowired
    ICookieService cookieService;

    @Autowired
    IRepositoryMovie repositoryMovie;

    @GetMapping("statistics")
    public String statistics(HttpServletRequest request, HttpServletResponse response, Model model) {


        cookieService.setConfig(request, response);
        if (!cookieService.isConnected()) return "error403";
        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
        List<Statistics> statistics = repository.findAll();
        if (!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            model.addAttribute("show", "hidden");
        else
            model.addAttribute("show", "show");
        if (statistics.isEmpty()) {
            model.addAttribute("tickets", 0);
            model.addAttribute("movies", 0);
            model.addAttribute("earnings", 0);
        } else {
            model.addAttribute("tickets", statistics.get(0).getTotalTickets());
            model.addAttribute("movies", statistics.get(0).getMoviePlayed());
            model.addAttribute("earnings", new DecimalFormat("#.##").format(statistics.get(0).getTotalEarnings()));
        }
        List<User> list = repositoryUser.findAll();
        int age;
        int ageUnder7 = 0;
        int age7B14 = 0;
        int age14b18 = 0;
        int age18 = 0;
        for (User user : list) {
            age = Period.between(user.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears();
            if (age < 7) ageUnder7++;
            else if (age < 14) age7B14++;
            else if (age < 18) age14b18++;
            else age18++;
        }


        List<Movie> movieList = repositoryMovie.findAll();
        long val10 = movieList.stream().filter(e->e.getPrice() <= 10).count();
        long val15 = movieList.stream().filter(e->e.getPrice() <= 15 && e.getPrice() > 10).count();
        long val20 = movieList.stream().filter(e->e.getPrice() <= 20 && e.getPrice() > 15).count();
        long val25 = movieList.stream().filter(e->e.getPrice() <= 25 && e.getPrice() > 20).count();
        long val30 = movieList.stream().filter(e->e.getPrice() <= 30 && e.getPrice() > 25).count();
        long val31 = movieList.stream().filter(e->e.getPrice() > 30).count();

        model.addAttribute("val1", val10);
        model.addAttribute("val2", val15);
        model.addAttribute("val3", val20);
        model.addAttribute("val4", val25);
        model.addAttribute("val5", val30);
        model.addAttribute("val6", val31);


        model.addAttribute("ageUnder7", ageUnder7);
        model.addAttribute("age7_14", age7B14);
        model.addAttribute("age14_18", age14b18);
        model.addAttribute("age18", age18);
        return "Statistics";
    }
}
