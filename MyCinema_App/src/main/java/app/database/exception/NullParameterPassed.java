package app.database.exception;

public class NullParameterPassed extends Exception {
    public NullParameterPassed() {
        System.out.println("Null parameter passed");
    }
}
