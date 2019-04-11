package App.Database.Entities;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
    private final int duration;


    public Movie(@NotNull String title, @Positive @Min(0) Integer duration) {
        this.title = title;
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
                '}';
    }
}
