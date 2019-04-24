package app.database.exception;

import lombok.Getter;

@Getter
public class InvalidDurationBetween extends Exception {
    public InvalidDurationBetween(int from, int to) {
        this.from = from;
        this.to = to;
    }

    private final int from;
    private final int to;

}
