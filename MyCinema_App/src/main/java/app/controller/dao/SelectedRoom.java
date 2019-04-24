package app.controller.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SelectedRoom {
    private  String name;

    public SelectedRoom() {
    name=" ";
    }

}
