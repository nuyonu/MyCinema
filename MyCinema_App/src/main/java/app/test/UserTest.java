package app.test;


import app.database.entities.User;
import app.database.infrastructure.IRepositoryUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase
@ContextConfiguration
public class UserTest {
//    @Autowired
//    private TestEntityManager testEntityManager;

    private User user;



    @Before
    public void setUp() {
        user = new User("TestUser", "TestUser", "TestUser",
                        "TestUser@gmail.com", "Test1.app.test");


    }

    @Test
    public void getUsername(){
        assertThat(user.getUsername().equals("TestUser")).isTrue();
    }
}