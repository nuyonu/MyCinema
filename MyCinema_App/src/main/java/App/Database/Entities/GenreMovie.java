package App.Database.Entities;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@EntityScan
@Document(collection = "Genre")
public class GenreMovie {

    @Id
    private String id;

    //    @NotNull
    private String movieId;

    //    @NotNull
//    @Size(min = 3)
    private String genre;

    public GenreMovie(String movieId, String genre) {
        this.movieId = movieId;
        this.genre = genre;
//        this.id = "n";
    }

    public GenreMovie() {
        this.movieId = " ";
        this.genre = " ";

    }

    @Override
    public String toString() {
        return "GenreMovie{" +
                "id='" + id + '\'' +
                ", movieId='" + movieId + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getGenre() {
        return genre;
    }
}
