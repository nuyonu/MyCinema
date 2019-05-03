package app.controller.dao;

import app.database.entities.Movie;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter

public class AjaxResponseSearch {
    private List<MovieDao> listOfResults;
    public  void setListOfResults(List<Movie> list){
        listOfResults=new ArrayList<>();
        list.stream().forEach(e->listOfResults.add(bind(e)));
    }
    private  MovieDao bind(Movie movie){
        return new MovieDao(movie.getTitle(),movie.getPrice(),movie.getPath());
    }

}
