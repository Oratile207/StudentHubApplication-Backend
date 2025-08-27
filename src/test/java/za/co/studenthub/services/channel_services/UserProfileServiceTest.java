package za.co.studenthub.services.channel_services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

@Deprecated
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserProfileServiceTest {
    UserProfileServiceTest(UserProfileService userProfileService) {
    }

    @BeforeAll
    public void setUp(){

    }

    @Test
    @Order(1)
    void create() {
    }

    @Test
    @Order(2)
    void read() {
    }

    @Test
    @Order(3)
    void update() {
    }

    @Test
    @Order(4)
    void delete() {
    }

    @Test
    @Order(5)
    void getAll() {
    }
    
}
