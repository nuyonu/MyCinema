package app.controller.dao;

import app.controller.constraints.UniqueUsername;
import app.database.constraints.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
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


    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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
