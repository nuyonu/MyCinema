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

    private String name;

    private int noRows;
    private int noColumns;

    //0 liber
    // 1
    //2 ocupat
    private List<List <Integer>> places;

    public Room(String name, int noRows, int noColumns) {
        this.noRows = noRows;
        this.name=name;
        this.noColumns = noColumns;
//        places = new ArrayList<>(Collections.nCopies(noColumns * noRows, 0));
    places=new ArrayList<>(Collections.nCopies(noRows,new ArrayList<>(Collections.nCopies(noColumns,0))));
    }

    public Room(String name, int noRows, int noColumns, List<List<Integer>> places) {
        this.name = name;
        this.noRows = noRows;
        this.noColumns = noColumns;
        this.places = places;
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

    public List<List<Integer>> getPlaces() {
        return places;
    }

    public  String getType(int number){
        if(number==0) return "chair free";
        if(number==1) return  "chair booked";
        return  "chair sold";
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
