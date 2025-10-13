package za.co.studenthub.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageControllerTest {

    @Test
    @Order(1)
    void getMessages() {
    }

    @Test
    @Order(2)
    void sendMessage() {
    }

    @Test
    @Order(3)
    void editMessage() {
    }

    @Test
    @Order(4)
    void deleteMessage() {
    }
}