package app.database.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@EntityScan
@Getter
@Setter
@ToString
@Document(collection = "CinemaRooms")
public class CinemaRoom implements Serializable
{
    @Id
    @Column(name = "ID")
    private String id;

    @NotNull
    @Size(min=2, max=30, message="The name of the room must be between 2 and 30 characters.")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private String createdDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

    public CinemaRoom() { this.name = ""; }

    public CinemaRoom(String name)
    {
        this.name = name;
    }
}
