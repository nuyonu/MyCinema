package app.database.utils;

import java.util.List;

public class MovieProgram {
    private String movieName;
    private String movieId;
    private List<ScreeningRoom> screening;

    public MovieProgram(String movieName, String movieId, List<ScreeningRoom> screening) {
        this.movieName = movieName;
        this.movieId = movieId;
        this.screening = screening;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public List<ScreeningRoom> getScreening() {
        return screening;
    }

    public void setScreening(List<ScreeningRoom> screening) {
        this.screening = screening;
    }
}
