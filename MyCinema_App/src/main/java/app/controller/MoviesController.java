package app.controller;

import app.controller.dao.AjaxResponseSearch;
import app.controller.dao.Search;
import app.controller.services.ICookieService;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryMovie;
import app.database.infrastructure.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @PostMapping("/api/search")
    @ResponseBody
    public ResponseEntity<AjaxResponseSearch> getMessage(@RequestBody Search search) {
        AjaxResponseSearch result = new AjaxResponseSearch();
        List<Movie> movieList=repositoryMovie.findByTitleLike(search.getInput_search());
        if(movieList.isEmpty()){

            return ResponseEntity.notFound().build();
        }else
        {
            result.setListOfResults(movieList);
            return ResponseEntity.ok().body(result);
        }
    }

}
