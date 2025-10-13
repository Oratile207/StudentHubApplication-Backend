package za.co.studenthub.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendshipControllerTest {

    @Test
    @Order(1)
    void getFriends() {
    }

    @Test
    @Order(2)
    void sendFriendRequest() {
    }

    @Test
    @Order(3)
    void acceptFriendRequest() {
    }

    @Test
    @Order(4)
    void rejectFriendRequest() {
    }

    @Test
    @Order(5)
    void getFriendRequests() {
    }

    @Test
    @Order(6)
    void removeFriend() {
    }

    @Test
    @Order(7)
    void searchUsers() {
    }

    @Test
    @Order(8)
    void blockUser() {
    }

    @Test
    @Order(9)
    void unblockUser() {
    }
}