package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication

public class Application {


    //    for html
//    https://www.mkyong.com/spring-boot/spring-boot-hello-world-example-thymeleaf/
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
