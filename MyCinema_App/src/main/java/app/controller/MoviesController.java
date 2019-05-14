package app.controller;

import app.controller.services.ICookieService;
import app.database.infrastructure.IRepositoryMovie;
import app.database.infrastructure.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MoviesController {
    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private IRepositoryMovie repositoryMovie;

    @Autowired
    private ICookieService cookieService;

    @GetMapping(value = "/movies")
    public String book(HttpServletRequest request, HttpServletResponse response, Model model) {
        cookieService.setConfig(request, response);
        if (!cookieService.isConnected()) return "error403";
        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
        model.addAttribute("listOfMovies", repositoryMovie.findAll());
        return "movies";
    }
}
