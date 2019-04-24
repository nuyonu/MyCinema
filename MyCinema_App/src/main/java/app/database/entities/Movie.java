package app.database.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@EntityScan
@Document(collection = "Movies")
@Getter
@Setter
public class Movie {
    @Id
    private String id;

    @NotNull
    @Indexed(unique = true)
    private String title;

    @Positive
    @Min(0)
    private int duration;


    @Positive
    @Min(0)
    private int price;

    private String path;

    private List<List<String>> scren;

    public Movie(@NotNull String title, @Positive @Min(0) int duration, @Positive @Min(0) int price, String path, List<List<String>> scren) {
        this.title = title;
        this.duration = duration;
        this.price = price;
        this.path = path;
        this.scren = scren;
    }


    public Movie() {
        this.title = " ";
    }



    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", price=" + price +
                ", path='" + path + '\'' +
                '}';
    }

    public void setDay(int day) {
        List<String> times = scren.get(day);
        scren = new ArrayList<>();
        scren.add(times);
    }
}
