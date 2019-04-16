package app.database.entities;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@EntityScan
@Document(collection = "Screening")
public class ScreeningHours {

    @Id
    private String id;


    @Indexed(unique = true)
    private String movieId;

    private List<List<ScreeningHours>> screeaningHours;

    public ScreeningHours(@NotNull String movieId, List<List<ScreeningHours>> screeaningHours) {
        this.movieId = movieId;
        this.screeaningHours = screeaningHours;
    }

    public ScreeningHours() {
    }

    public String getId() {
        return id;
    }

    public String getMovieId() {
        return movieId;
    }

    public List<List<ScreeningHours>> getScreeaningHours() {
        return screeaningHours;
    }

    @Override
    public String toString() {
        return "ScreeningHours{" +
                "id='" + id + '\'' +
                ", movieId='" + movieId + '\'' +
                ", screeaningHours=" + screeaningHours +
                '}';
    }
}
