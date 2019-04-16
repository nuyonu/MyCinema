package app.controller.dao;

import app.controller.constraints.UniqueUsername;
import app.database.constraints.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class RegisterModel implements Serializable {
    @Size(min = 4, max = 30, message = "First Name must contain at least 3 characters and a maximum of 30 characters")
    private String firstName;
    @Size(min = 4, max = 30, message = "Last Name must contain at least 3 characters and a maximum of 30 characters")
    private String lastName;
    @Email(message = "Email should be valid")
    private String eMail;
    @UniqueUsername
    @Size(min = 4, max = 30, message = "Username must contain at least 3 characters and a maximum of 30 characters")
    private String username;
    @ValidPassword
    private String password;
    @Size(min = 4, max = 30, message = "Confirm Password must contain at least 3 characters and a maximum of 30 characters")
    private String confirmPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "RegisterClass{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", eMail='" + eMail + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
