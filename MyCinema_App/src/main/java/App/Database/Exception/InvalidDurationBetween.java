package App.Database.Exception;

public class InvalidDurationBetween extends Exception {
    private final int from;
    private final int to;

    public InvalidDurationBetween(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void printStackTrace() {
        System.out.println("your value:" + from + " " + to);
    }
}
