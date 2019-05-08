package app.database.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@EntityScan
@Document(collection = "Screening")
@Getter
@Setter
@ToString
public class ScreeningHours {

    @Id
    private String id;

    private String movieId;

    private String roomId;

    private String date;

    private String time;

    public ScreeningHours(String movieId, String roomId, String date, String time) {
        this.movieId = movieId;
        this.roomId = roomId;
        this.date = date;
        this.time = time;
    }

    public ScreeningHours() {
    }
}
