package app.controller.dao;

public class SelectedRoom {
    private  String name;

    public SelectedRoom(String name) {
        this.name = name;
    }

    public SelectedRoom() {
    name=" ";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
