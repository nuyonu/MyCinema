package app.controller;

import app.controller.dao.AjaxResponseBody;
import app.controller.dao.AjaxResponseSearch;
import app.controller.dao.LoginInput;
import app.controller.dao.Search;
import app.controller.services.ICookieService;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryMovie;
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
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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
    @PostMapping("/api/search")
    @ResponseBody
    public ResponseEntity<AjaxResponseSearch> getMessage(@RequestBody Search search) {
        AjaxResponseSearch result = new AjaxResponseSearch();
        List<Movie> movieList=repository.findByTitleLike(search.getInput_search());
        if(movieList.size()==0){

            return ResponseEntity.notFound().build();
        }else
        {
            result.setListOfResults(movieList);
            return ResponseEntity.ok().body(result);
        }
    }

    @Autowired
    private ICookieService cookieService;



}
