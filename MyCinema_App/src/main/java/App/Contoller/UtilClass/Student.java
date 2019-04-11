package App.Contoller.UtilClass;

import java.io.Serializable;


public class Student implements Serializable {
    private Integer id = 1;
    private String name = "dasd";
    private Character gender = 'm';

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }
}
