package app.database.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@EntityScan
@Document(collection = "Screening")
@Getter
@Setter
@ToString
public class ScreeningHours {

    @Id
    private String id;

    @CreatedDate
    private String date;

    @CreatedDate
    private String time;

    private ObjectId movieId;

    private ObjectId roomId;

    private List<List<Integer>> seats;

    public ScreeningHours(ObjectId movieId, ObjectId roomId, String date, String time, List<List<Integer>> seats) {
        this.movieId = movieId;
        this.roomId = roomId;
        this.date = date;
        this.time = time;
        this.seats = seats;
    }

    public ScreeningHours() { }
}