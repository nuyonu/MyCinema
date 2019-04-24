package app.controller.dao;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieDao {
    private String name;
    private int price;
    private String path;
}
