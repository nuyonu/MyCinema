package app.controller;

import app.controller.services.CookieHandler;
import app.controller.services.ICookieService;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryMovie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class MoviesController {

    @Autowired
    IRepositoryMovie repository;

    @GetMapping(value = "/movies")
    public String book(HttpServletRequest request, HttpServletResponse response, Model model) {
       cookieService.setConfig(request,response);
        if (!cookieService.isConnected()) return "error403";
        model.addAttribute("user",cookieService.getUser());
        List<Movie> listOfMovies = repository.findAll();
        model.addAttribute("listOfMovies", listOfMovies);
        return "movies";
    }
    @Autowired
    private ICookieService cookieService;

}
