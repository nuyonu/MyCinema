package app.database.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@EntityScan
@Document(collection = "Genre")
@Getter
@Setter
public class GenreMovie {

    @Id
    private String id;

    //    @NotNull
    private final String movieId;

    //    @NotNull
//    @Size(min = 3)
    private final String genre;

    public GenreMovie(String movieId, String genre) {
        this.movieId = movieId;
        this.genre = genre;
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
}
