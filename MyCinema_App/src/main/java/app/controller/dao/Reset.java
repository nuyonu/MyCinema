package app.controller.dao;

public class Reset {
    private String email;

    public Reset(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Reset() {
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

