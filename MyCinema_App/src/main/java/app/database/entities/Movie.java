package app.database.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.text.SimpleDateFormat;
import java.util.Date;

@EntityScan
@Document(collection = "Movies")
@Getter
@Setter
@NoArgsConstructor
public class Movie {
    @Id
    private String id;

    @NotNull
    @Indexed(unique = true)
    private String title;

    @Positive
    private int seconds;

    @Positive
    private double price;

    private String path;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private String createdDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

    public Movie(@NotNull String title, @Positive int seconds, @Positive double price, String path) {
        this.title = title;
        this.seconds = seconds;
        this.price = price;
        this.path = path;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", seconds=" + seconds +
                ", price=" + price +
                ", path='" + path + '\'' +
                '}';
    }

    public String getDuration() {
        return  Integer.toString(this.seconds/3600) + ":" +
                Integer.toString((this.seconds % 3600) / 60) + ":" +
                Integer.toString(this.seconds % 60);
    }
}
