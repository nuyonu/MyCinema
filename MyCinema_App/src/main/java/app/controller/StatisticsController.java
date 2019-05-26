package app.controller;

import app.controller.services.ICookieService;
import app.database.entities.Statistics;
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
import java.util.List;

@Controller
public class StatisticsController {
    @Autowired
    IRepositoryUser repositoryUser;

    @Autowired
    IRepositoryStatistics repository;

    @Autowired
    ICookieService cookieService;

    @GetMapping("statistics")
    public String statistics(HttpServletRequest request, HttpServletResponse response, Model model) {

        cookieService.setConfig(request, response);
        if (!cookieService.isConnected()) return "error403";
        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
        List<Statistics> statistics = repository.findAll();
        if(!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            model.addAttribute("show","hidden");
        else
            model.addAttribute("show","show");
        if (statistics.isEmpty()) {
            model.addAttribute("tickets", 0);
            model.addAttribute("movies", 0);
            model.addAttribute("earnings", 0);
        } else {
            model.addAttribute("tickets", statistics.get(0).getTotalTickets());
            model.addAttribute("movies", statistics.get(0).getMoviePlayed());
            model.addAttribute("earnings", new DecimalFormat("#.##").format(statistics.get(0).getTotalEarnings()));
        }
        return "Statistics";
    }
}
