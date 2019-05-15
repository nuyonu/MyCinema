package app.controller.dao;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CinemaRoomDTO {
    @NotNull
    @Size(min=2, max=30, message="The name of the room must be between 2 and 30 characters.")
    private String name;
}
