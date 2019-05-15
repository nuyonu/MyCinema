package app.controller.dao;

import app.database.entities.CinemaRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoomDao {
    List<CinemaRoom> roomList;

}
