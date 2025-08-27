package za.co.studenthub.factory;

import org.junit.jupiter.api.Test;

import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.UserRole;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryTest {

    @Test
    void createUser() {
            User user_1 = UserFactory.createUser(
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

        User user_2 = UserFactory.createUser(
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

        assertNotNull(user_1);
        assertNotNull(user_2);
        assertNotNull(user_1.getUserId());
        assertNotNull(user_2.getUserId());
        assertEquals(UserRole.FACULTY_MEMBER, user_1.getUserRole());
        assertEquals(UserRole.ADMIN, user_2.getUserRole());
        assertEquals("Miriam", user_1.getUserFirstName());
        assertEquals("Ranger", user_2.getUserLastName());
        
    }
}