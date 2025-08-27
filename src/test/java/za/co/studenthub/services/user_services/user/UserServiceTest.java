package za.co.studenthub.services.user_services.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.factory.UserFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    private final UserService userService;
    UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    User user_1;
    User user_2;

    @BeforeEach
    public void setUp(){
        user_1 = UserFactory.createUser(
            UserRole.FACULTY_MEMBER,
            "Miriam", "Kembell",
            "merriammk@example.com",
            "password",
            null,
            "3499234",
            "A passionate educator with over 10 years of experience in higher education.",
            fail(null, null),
            null
        );

        user_2 = UserFactory.createUser(
            UserRole.ADMIN,
            "Ranger", "Mc'Gilliger",
            "gylendor@example.com",
            "password",
            null,
            "222342",
            "Always striving for the empowerment of students through education.",
            fail(null, null),
            null
        );

    }

    @Test
    @Order(1)
    void create() {
        User createdUser1 = userService.create(user_1);
        User createdUser2 = userService.create(user_2);
        assertNotNull(createdUser1);
        assertNotNull(createdUser2);
        assertNotNull(createdUser1.getUserId());
        assertNotNull(createdUser2.getUserId());
        assertEquals("Miriam", createdUser1.getUserFirstName());
        assertEquals("Ranger", createdUser2.getUserFirstName());
        System.out.println("Created User 1: " + createdUser1); 
        System.out.println("Created User 2: " + createdUser2);
    }

    @Test
    @Order(2)
    void read() {
        User retrievedUser1 = userService.read(user_1.getUserId());
        User retrievedUser2 = userService.read(user_2.getUserId());
        assertNotNull(retrievedUser1);
        assertNotNull(retrievedUser2);
        assertEquals(user_1.getUserId(), retrievedUser1.getUserId());
        assertEquals(user_2.getUserId(), retrievedUser2.getUserId());
        System.out.println("Retrieved User 1: " + retrievedUser1);
        System.out.println("Retrieved User 2: " + retrievedUser2);
    }

    @Test
    @Order(3)
    void update() {
        User updatedUser1 = user_1.toBuilder()
            .userEmail("Kembella@gmail.com")
            .build();
        User resultUser1 = userService.update(updatedUser1);
        assertNotNull(resultUser1);
        assertEquals("An updated email for Miriam Kembell.", resultUser1.getUserEmail());
        System.out.println("Updated User 1: " + resultUser1);
    }

    @Test
    @Order(4)
    void delete() {
        userService.create(user_1);
        assertNotNull(userService.read(user_1.getUserId()));
        userService.delete(user_1.getUserId());
        assertNull(userService.read(user_1.getUserId()));
        System.out.println("Deleted User with ID: " + user_1.getUserId());
    }

    @Test
    @Order(5)
    void getAll() {
        userService.create(user_1);
        userService.create(user_2);
        var allUsers = userService.getAll();
        assertNotNull(allUsers);
        assertTrue(allUsers.size() >= 2);
        System.out.println("All Users: " + allUsers);
    }
}