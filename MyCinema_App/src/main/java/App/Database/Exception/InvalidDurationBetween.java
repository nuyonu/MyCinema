package App.Database.Exception;

public class InvalidDurationBetween extends Exception {
    private int from;
    private int to;

    public InvalidDurationBetween(int from, int to) {
        this.from = from;
        this.to = to;
    }
}
