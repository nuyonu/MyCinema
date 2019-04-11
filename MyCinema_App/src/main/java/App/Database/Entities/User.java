package App.Database.Entities;

import App.Database.Constraints.ValidPassword;
import org.hibernate.envers.Audited;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    private final String firstName;
    @NotNull
    @Size(min = 4, max = 50)
    @Column(name = "Last Name")
    private final String lastName;
    @Email()
    @Column(name = "Email")
    private final String email;
    @ValidPassword
    @Column(name = "Password")
    private final String password;
    @NotNull()
    @Indexed(unique = true)
    @Size(min = 5, max = 30)
    @Column(name = "username")
    private String username;


    public User(@NotNull @Size(min = 5, max = 30) String username, @NotNull @Size(min = 4, max = 200) String firstName, @NotNull @Size(min = 4, max = 50) String lastName, @Email String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User() {
            lastName=" ";
            firstName=" ";
            username=" ";
            email=" ";
            password=" ";
    }

    public String getUsername() {
        return username;
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
