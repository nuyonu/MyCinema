package app.database.entities;

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

    public List<List<String>> getScren() {
        return scren;
    }

    public void setScren(List<List<String>> scren) {
        this.scren = scren;
    }

    public Movie() {
        this.title = " ";
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getId() {
        return id;
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
