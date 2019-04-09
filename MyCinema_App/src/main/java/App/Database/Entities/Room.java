package App.Database.Entities;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EntityScan
@Document(collection = "Rooms")
public class Room {
    @Id
    private String id;

    private int noRows;
    private int noColumns;

    private List<Integer> places;

    public Room(int noRows, int noColumns) {
        this.noRows = noRows;
        this.noColumns = noColumns;
        places = new ArrayList<>(Collections.nCopies(noColumns * noRows, 0));
    }

    public Room(int noColumns, int noRows, List<Integer> places) {
        this.places = places;
        this.noColumns = noColumns;
        this.noRows = noRows;

    }

    public Room() {
    }

    public String getId() {
        return id;
    }

    public int getNoRows() {
        return noRows;
    }

    public int getNoColumns() {
        return noColumns;
    }

    public List<Integer> getPlaces() {
        return places;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", noRows=" + noRows +
                ", noColumns=" + noColumns +
                ", places=" + places +
                '}';
    }
}
