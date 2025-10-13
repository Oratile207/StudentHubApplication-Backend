package za.co.studenthub.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChannelMembershipControllerTest {

    @Test
    @Order(1)
    void getChannelMembers() {
    }

    @Test
    @Order(2)
    void checkMembership() {
    }

    @Test
    @Order(3)
    void getOnlineUsers() {
    }

    @Test
    @Order(4)
    void getMyChannels() {
    }

    @Test
    @Order(5)
    void updateMemberRole() {
    }
}