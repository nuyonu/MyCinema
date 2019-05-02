package app.database.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EntityScan
@Getter
@Setter
@Document(collection = "CinemaRooms")
public class CinemaRoom implements Serializable
{
    @Id
    @Column(name = "ID")
    private String id;

    @NotNull
    @Size(min=2, max=30, message="The name of the room must be between 2 and 30 characters.")
    private String name;

    public CinemaRoom() { this.name = ""; }

    public CinemaRoom(String name)
    {
        this.name = name;
    }


    @Override
    public String toString() {
        return "CinemaRoom{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
