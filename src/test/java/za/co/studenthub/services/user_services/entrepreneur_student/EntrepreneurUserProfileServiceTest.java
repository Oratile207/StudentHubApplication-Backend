package za.co.studenthub.services.user_services.entrepreneur_student;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.co.studenthub.domain.EntrepreneurUserProfile;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.factory.UserFactory;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class EntrepreneurUserProfileServiceTest {

    @Autowired
    private EntrepreneurUserProfileService entrepreneurUserProfileService;

    private User entrepreneurUser;

    @BeforeEach
    void setUp() {
        entrepreneurUser = UserFactory.createUser(
            UserRole.ENTREPRENEUR,
            "Lerato", "Mabena",
            "lerato@startuphub.ac.za",
            "entrepreneurPass321",
            null,
            null,
            "Innovator and founder of multiple student-led ventures.",
            true,
            "https://startuphub.ac.za/session/lerato"
        );

        EntrepreneurUserProfile profile = entrepreneurUser.getEntrepreneurProfile();
        if (profile != null) {
            profile.setUser(entrepreneurUser); // Ensure bidirectional link
        }

    }

    @Test
    @Order(1)
    void create() {
        entrepreneurUser = entrepreneurUserProfileService.create(entrepreneurUser.getEntrepreneurProfile()).getUser();
        assertNotNull(entrepreneurUser);
        assertNotNull(entrepreneurUser.getUserId());
        assertEquals(UserRole.ENTREPRENEUR, entrepreneurUser.getUserRole());

        EntrepreneurUserProfile profile = entrepreneurUser.getEntrepreneurProfile();
        assertNotNull(profile);
        assertTrue(profile.isCommercePortfolioEnabled());
        assertEquals("Innovator and founder of multiple student-led ventures.", profile.getBiography());

        System.out.println("Created Entrepreneur User: " + entrepreneurUser);
    }

    @Test
    @Order(2)
    void read() {
        EntrepreneurUserProfile profile = entrepreneurUserProfileService.read(entrepreneurUser.getUserId());
        assertNotNull(profile);
        assertEquals("lerato@startuphub.ac.za", profile.getUser().getUserEmail());
        System.out.println("Read Entrepreneur Profile: " + profile);
    }

    @Test
    @Order(3)
    void update() {
        EntrepreneurUserProfile profile = entrepreneurUser.getEntrepreneurProfile();
        profile.setBiography("Updated bio: passionate about tech startups.");
        profile.setCommercePortfolioEnabled(false);

        EntrepreneurUserProfile updated = entrepreneurUserProfileService.update(profile);
        assertNotNull(updated);
        assertEquals("Updated bio: passionate about tech startups.", updated.getBiography());
        assertFalse(updated.isCommercePortfolioEnabled());

        System.out.println("Updated Entrepreneur Profile: " + updated);
    }

    @Test
    @Order(4)
    void delete() {
        entrepreneurUserProfileService.delete(entrepreneurUser.getUserId());
        EntrepreneurUserProfile deleted = entrepreneurUserProfileService.read(entrepreneurUser.getUserId());
        assertNull(deleted);
        System.out.println("Deleted Entrepreneur Profile for user ID: " + entrepreneurUser.getUserId());
    }
}
