package app.database.entities;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EntityScan
@Document(collection = "CinemaRooms")
public class CinemaRoom implements Serializable
{
    @Id
    @Column(name = "ID")
    private String id;

    @NotNull
    @Indexed(unique = true)
    @Column(name = "Name")
    private String name;

    @NotNull
    @Column(name = "ImagePath")
    private String imagePath;

    public CinemaRoom(String name, String imagePath)
    {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "CinemaRoom{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
