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

    private String idMovie;

    private String time;

    private String name;

    private int day;

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
    places=new ArrayList<>(Collections.nCopies(noRows,new ArrayList<>(Collections.nCopies(noColumns,0))));
    }

    public Room(String idMovie, String time, int day, String name, int noRows, int noColumns) {
        this.idMovie = idMovie;
        this.day = day;
        this.time = time;
        this.name = name;
        this.noRows = noRows;
        this.noColumns = noColumns;
        this.places = new ArrayList<>(Collections.nCopies(noRows, new ArrayList<>(Collections.nCopies(noColumns, 0))));
    }

    public int getDay() {
        return day;
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

    public void setDay(int day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getIdMovie() {
        return idMovie;
    }

    public String getName() {
        return name;
    }
}
