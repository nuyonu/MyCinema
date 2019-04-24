package app.controller.dao;



import app.controller.constraints.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class ChangeInformationModel implements Serializable {
    @Size(min = 4, max = 30, message = "First Name must contain at least 3 characters and a maximum of 30 characters")
    private String firstName;
    @Size(min = 4, max = 30, message = "Last Name must contain at least 3 characters and a maximum of 30 characters")
    private String lastName;
    @Email(message = "Email should be valid")
    private String email;
    @PhoneNumber
    private String phoneNumber;

}
