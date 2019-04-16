package app.controller;

import app.controller.services.CookieHandler;
import app.database.entities.Movie;
import app.database.service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProgramController {

    @Autowired
    IRepository serviceDatabase;

    @RequestMapping(value = "/program", method = { RequestMethod.GET, RequestMethod.POST })
    public String programQuery(HttpServletRequest request, HttpServletResponse response, Model model,
                               @RequestParam(name = "day", defaultValue = "0") String day,
                               @RequestParam(name = "idMovie", defaultValue = "") String idMovie)
    {
        CookieHandler cookieHandler = new CookieHandler(request, response);

        if (!cookieHandler.isConnected())
            return "redirect:/error403";

        List<Movie> movieList = serviceDatabase.getAllMovies();
        movieList.forEach(e->e.setDay(Integer.parseInt(day)));

        if (!idMovie.isEmpty())
            movieList = movieList.stream().filter(e -> e.getId().equals(idMovie)).collect(Collectors.toList());

        model.addAttribute("movieList", movieList);
        model.addAttribute("day", day);

        return "program";
    }

}
