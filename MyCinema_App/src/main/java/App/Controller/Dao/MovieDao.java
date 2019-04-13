package App.Controller.Dao;

public class MovieDao {
    private String name;
    private int price;
    private String path;

    public MovieDao(String name, int price, String path) {
        this.name = name;
        this.price = price;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
