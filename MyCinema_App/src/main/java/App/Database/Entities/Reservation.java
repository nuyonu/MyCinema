package App.Database.Entities;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@EntityScan
@Document(collection = "Reservation")
public class Reservation {
    @Id
    private String id;

    @NotNull
    private String userId;

    @NotNull
    private String movieId;

    @NotNull
    private String roomId;

    @NotNull
    private Integer day;

    @NotNull
    private LocalTime time;


    public Reservation(@NotNull String userId, @NotNull String movieId, @NotNull String roomId, @NotNull Integer day, @NotNull LocalTime time) {
        this.userId = userId;
        this.movieId = movieId;
        this.roomId = roomId;
        this.day = day;
        this.time = time;
    }

    public Reservation() {
        userId = " ";
        movieId = " ";
        roomId = " ";
        day = 0;
        time = LocalTime.now();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getRoomId() {
        return roomId;
    }

    public Integer getDay() {
        return day;
    }

    public LocalTime getTime() {
        return time;
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", movieId='" + movieId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", day=" + day +
                ", time=" + time +
                '}';
    }
}
