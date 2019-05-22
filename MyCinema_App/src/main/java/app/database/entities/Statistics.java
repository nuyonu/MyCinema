package app.database.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;

@EntityScan
@Document(collection = "Statistics")
@Getter
@Setter
@ToString
public class Statistics {
    @Id
    private String id;

    @Column(name = "Total Tickets")
    private Integer totalTickets;

    @Column(name = "Movie Played")
    private Integer moviePlayed;

    @Column(name = "Total Earnings")
    private Float totalEarnings;

    public Statistics() {
        totalTickets = 0;
        moviePlayed = 0;
        totalEarnings = 0.0f;
    }
}
