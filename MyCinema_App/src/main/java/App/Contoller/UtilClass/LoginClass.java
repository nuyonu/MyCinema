package App.Contoller.UtilClass;

import java.io.Serializable;

public class LoginClass implements Serializable {
    public String username;
    public String password;
    private boolean remainConnected;

    public boolean isRemainConnected() {
        return remainConnected;
    }

    public void setRemainConnected(boolean remainConnected) {
        this.remainConnected = remainConnected;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
