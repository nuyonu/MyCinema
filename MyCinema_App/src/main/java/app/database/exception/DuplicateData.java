package app.database.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateData extends Exception {
    private  final    String error;
    public DuplicateData() {
        error ="Null parameter passed";
    }
}
