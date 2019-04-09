package App.Database.Entities;

import App.Database.Constraints.ValidPassword;
import org.hibernate.envers.Audited;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EntityScan
@Audited
@Document(collection = "Users")
public class User implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @NotNull
    @Size(min = 4, max = 200)
    @Column(name = "First Name")
    private String firstName;

    @NotNull
    @Size(min = 4, max = 50)
    @Column(name = "Last Name")
    private String lastName;

    @Email
    @Column(name = "Email")
    private String email;

    @ValidPassword
    @Column(name = "Password")
    private String password;


    public User(@NotNull @Size(min = 4, max = 200) String firstName, @NotNull @Size(min = 4, max = 50) String lastName, @Email String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
