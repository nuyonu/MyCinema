package app.controller.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter

public class LoginInput implements Serializable {
    private String username;
    private String password;
    private boolean remainConnected;

    public LoginInput() {
        this.username = "";
        this.password = "";
        this.remainConnected = false;
    }



    @Override
    public String toString() {
        return "LoginClass{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", remainConnected=" + remainConnected +
                '}';
    }
}
