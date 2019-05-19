package app.database.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
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

    private String date;

    private String time;

    private ObjectId movieId;

    private ObjectId roomId;

    public ScreeningHours(ObjectId movieId, ObjectId roomId, String date, String time) {
        this.movieId = movieId;
        this.roomId = roomId;
        this.date = date;
        this.time = time;
    }

    public ScreeningHours() { }
}