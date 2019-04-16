package app.database.exception;

public class InvalidDurationMovie extends Exception {
    private final int duration;

    public InvalidDurationMovie(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
