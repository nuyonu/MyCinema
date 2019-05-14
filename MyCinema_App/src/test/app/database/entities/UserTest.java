package app.database.entities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User("TestUserUsername", "TestUserFirstName", "TestUserLastName",
                "TestUser@gmail.com", "Test1.test");
    }

    @Test
    public void shouldReturnUsername(){
        assertThat(user.getUsername().equals("TestUserUsername")).isTrue();
    }

    @Test
    public void shouldReturnFirstName() {
        assertThat(user.getFirstName().equals("TestUserFirstName")).isTrue();
    }

    @Test
    public void shouldReturnLastName() {
        assertThat(user.getLastName().equals("TestUserLastName")).isTrue();
    }

    @Test
    public void shouldReturnEmail() {
        assertThat(user.getEmail().equals("TestUser@gmail.com")).isTrue();
    }

    @Test
    public void shouldReturnNoPhoneNumber() {
        assertThat(user.getPhoneNumber().equals("No phone number")).isTrue();
    }

    //TODO -> Make here test password after encrypt.


    @Test
    public void mightSetNewFirstName() {
        user.setFirstName("newFirstName");
        assertThat(user.getFirstName().equals("newFirstName")).isTrue();
    }

    @Test
    public void mightSetNewLastName() {
        user.setLastName("newLastName");
        assertThat(user.getLastName().equals("newLastName")).isTrue();
    }

    @Test
    public void mightSetNewEmail() {
        user.setEmail("newEmail@gmail.com");
        assertThat(user.getEmail().equals("newEmail@gmail.com")).isTrue();
    }

    @Test
    public void mightSetNewPhoneNumber() {
        user.setPhoneNumber("0741234567");
        assertThat(user.getPhoneNumber().equals("0741234567")).isTrue();
    }

}