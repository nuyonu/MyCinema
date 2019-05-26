package app.controller;

import app.controller.services.ICookieService;
import app.database.infrastructure.IRepositoryCinemaRoom;
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

@Controller
public class AdminHomeController {
    @Autowired
    private ICookieService cookieService;

    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private IRepositoryMovie repositoryMovie;

    @Autowired
    private IRepositoryStatistics repositoryStatistics;

    @Autowired
    private IRepositoryCinemaRoom repositoryCinemaRoom;

    @GetMapping("/admin-home")
    public String showHomePage(HttpServletRequest request,
                               HttpServletResponse response,
                               Model model) {
        cookieService.setConfig(request, response);
        if (!cookieService.isConnected())
            return "error403";

        if (!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            return "noAccess";

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
        model.addAttribute("totalUsers", repositoryUser.count());
        model.addAttribute("totalMovies", repositoryMovie.count());
        model.addAttribute("totalRooms", repositoryCinemaRoom.count());
        if (!repositoryStatistics.findAll().isEmpty())
            model.addAttribute("totalTickets", repositoryStatistics.findAll().get(0).getTotalTickets());
        else
            model.addAttribute("totalTickets", "0");
        model.addAttribute("lastMovie", repositoryMovie.findFirstByOrderByCreatedDateDesc());
        model.addAttribute("lastUser", repositoryUser.findFirstByOrderByCreatedDateDesc());
        model.addAttribute("lastRoom", repositoryCinemaRoom.findFirstByOrderByCreatedDateDesc());

        return "AdminHome";
    }
}
