package app.database.exception;

public class NullTitle extends Exception {
    private final String error;

    public NullTitle() {
        error = "Null title";
    }

    public String getError() {
        return error;
    }
}
