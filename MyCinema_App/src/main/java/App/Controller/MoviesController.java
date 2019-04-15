package App.Controller;

import App.Controller.Dao.MovieDao;
import App.Controller.ServiceController.CookieHandler;
import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MoviesController {

    @Autowired
    IRepository repository;

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public String show(HttpServletRequest request, HttpServletResponse response, Model model)
    {
        CookieHandler cookieHandler = new CookieHandler(request, response);

        if (!cookieHandler.isConnected())
            return "error403";

        model.addAttribute("movieList", repository.getAllMovies());
        return "movies";
    }

    @RequestMapping(value = "/program/{id}", method = RequestMethod.POST)
    public String book(HttpServletRequest request, HttpServletResponse response, @PathVariable String id)
    {
        CookieHandler cookieHandler = new CookieHandler(request, response);

        if (!cookieHandler.isConnected())
            return "error403";

        return "redirect:/program#" + id;
    }

}
