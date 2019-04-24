package app.database.exception;

import lombok.Getter;

@Getter
public class NullParameterPassed extends Exception {
    private final String error;
    public NullParameterPassed() {
        error ="Null parameter passed";
    }

}
