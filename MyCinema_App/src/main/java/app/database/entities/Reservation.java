package app.database.entities;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EntityScan
@Document(collection = "Reservation")
@Getter
@Setter
public class Reservation {
    @Id
    private String id;

    private ObjectId userId;

    private ObjectId screeningId;

    public Reservation(ObjectId userId, ObjectId screeningId) {
        this.userId = userId;
        this.screeningId = screeningId;
    }
}
