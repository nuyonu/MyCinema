package app.controller;

import app.controller.services.CookieHandler;
import app.database.entities.Movie;
import app.database.service.IRepository;
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
    IRepository repository;

    @GetMapping(value = "/movies")
    public String book(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieHandler cookieHandler = new CookieHandler(request, response);
        if (!cookieHandler.isConnected()) return "error403";
        List<Movie> listOfMovies = repository.getAllMovies();
        model.addAttribute("listOfMovies", listOfMovies);
        return "movies";
    }

}