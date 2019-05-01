package app.controller.dao;

import app.database.constraints.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetConn {
    @ValidPassword
    private String password=" ";
    private String passwordConfirm=" ";

}
