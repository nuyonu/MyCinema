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

    private int row;

    private int column;

    public Reservation(ObjectId userId, ObjectId screeningId, int row, int column) {
        this.userId = userId;
        this.screeningId = screeningId;
        this.row = row;
        this.column = column;
    }
}
