package app.controller;

import app.controller.services.CookieHandler;
import app.controller.services.ICookieService;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryMovie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProgramController {

    @Autowired
    IRepositoryMovie movieRepository;

    @GetMapping("/program")
    public String getProgram(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam(name = "day", defaultValue = "0") String day,
                             @RequestParam(name = "idMovie", defaultValue = "") String idMovie) {

        model.addAttribute("movieList", getMovieList(idMovie, day));
        model.addAttribute("day", day);


        return checkForAcces(model ,request, response);
    }

    @PostMapping("/program")
    public String programQuery(HttpServletRequest request, HttpServletResponse response, Model model,
                               @RequestParam(name = "day", defaultValue = "0") String day,
                               @RequestParam(name = "idMovie", defaultValue = "") String idMovie) {

        model.addAttribute("movieList", getMovieList(idMovie, day));
        model.addAttribute("day", day);

        return checkForAcces(model, request, response);
    }

    private String checkForAcces(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        cookieService.setConfig(request,response);
        if (!cookieService.isConnected()) return "error403";

        else {
            model.addAttribute("user", cookieService.getUser());
            return "program";
        }
    }

    private List<Movie> getMovieList(String idMovie, String day)
    {
        List<Movie> movieList = movieRepository.findAll();
        movieList.forEach(e->e.setDay(Integer.parseInt(day)));

        if (!idMovie.isEmpty())
            movieList = movieList.stream().filter(e -> e.getId().equals(idMovie)).collect(Collectors.toList());

        return movieList;
    }
    @Autowired
    private ICookieService cookieService;

}
