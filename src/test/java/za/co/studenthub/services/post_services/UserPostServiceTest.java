package za.co.studenthub.services.post_services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import za.co.studenthub.domain.User;
import za.co.studenthub.domain.UserPost;
import za.co.studenthub.domain.enums.ChannelType;
import za.co.studenthub.domain.enums.UserPostType;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.factory.ChannelFactory;
import za.co.studenthub.factory.UserFactory;
import za.co.studenthub.factory.UserPostFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserPostServiceTest {

    @Autowired
    private UserPostService userPostService;

    static User user_1;
    static User user_2;
    static Set<User> channelMembers;
    static za.co.studenthub.domain.Channel channel_1;
    static za.co.studenthub.domain.Channel channel_2;
    static UserPostType userPostType;
    static UserPostType userPostType2;
    static UserPostType userPostType3;
    static UserPost post_1;
    static UserPost post_2;
    UserPost post_3;

    @BeforeAll
    public static void setUp(){
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

        channelMembers = new HashSet<>();
            channelMembers.add(user_1);
            channelMembers.add(user_2);

        channel_1 = ChannelFactory.createChannel(
            "Application Development Practice 3",
            ChannelType.ACADEMIC_HELP,
            user_2,
            channelMembers
        );

        channel_2 = ChannelFactory.createChannel(
            "Business Start-Up Ideas",
            ChannelType.PUBLIC_FORUM,
            user_2, 
            channelMembers
        );

        userPostType = UserPostType.TEXT_POST;
        userPostType2 = UserPostType.EVENT_REMINDER;
        userPostType3 = UserPostType.VIDEO_CLIP;

        post_1 = UserPostFactory.createUserPost(
            user_1,
            channel_1,
            userPostType,
            "Don't forget to submit your assignments by the end of this week!"
        );

        post_2 = UserPostFactory.createUserPost(
            user_2,
            channel_2,
            userPostType,
            "Join our upcoming webinar on entrepreneurship this Friday at 3 PM!"
        );

    }


    @Test
    @Order(1)
    void create() {
        UserPost createdPost1 = userPostService.create(post_1);
        UserPost createdPost2 = userPostService.create(post_2);
        assertNotNull(createdPost1);
        assertNotNull(createdPost2);
        assertNotNull(createdPost1.getUserPostId());
        assertNotNull(createdPost2.getUserPostId());
        assertEquals(post_1.getUserPostId(), createdPost1.getUserPostId());
        assertEquals(post_2.getUserPostId(), createdPost2.getUserPostId());
        System.out.println("Created Post 1: " + createdPost1);
        System.out.println("Created Post 2: " + createdPost2);
    }

    @Test
    @Order(2)
    void read() {
        UserPost retrievedPost1 = userPostService.read(post_1.getUserPostId());
        UserPost retrievedPost2 = userPostService.read(post_2.getUserPostId());
        assertNotNull(retrievedPost1);
        assertNotNull(retrievedPost2);
        assertEquals(post_1.getUserPostId(), retrievedPost1.getUserPostId());
        assertEquals(post_2.getUserPostId(), retrievedPost2.getUserPostId());
        System.out.println("Retrieved Post 1: " + retrievedPost1);
        System.out.println("Retrieved Post 2: " + retrievedPost2);
    }

    @Test
    @Order(3)
    void update() {
        UserPost updatedPost1 = post_1.toBuilder()
            .content("Updated content: Please remember to submit your assignments by Sunday!")
            .build();
        UserPost resultPost1 = userPostService.update(updatedPost1);
        assertNotNull(resultPost1);
        assertEquals("Updated content: Please remember to submit your assignments by Sunday!", resultPost1.getContent());
        System.out.println("Updated Post 1: " + resultPost1);
    }

    @Test
    @Order(4)
    void delete() {
        userPostService.create(post_3 = UserPostFactory.createUserPost(
            user_1,
            channel_2,
            userPostType3,
            "Check out this cool video on effective study techniques!"
        ));
        assertNotNull(userPostService.read(post_3.getUserPostId()));
        userPostService.delete(post_3.getUserPostId());
        assertNull(userPostService.read(post_3.getUserPostId()));
        System.out.println("Deleted Post 3 with ID: " + post_3.getUserPostId());
    }

    @Test
    @Order(5)
    void getAll() {
        userPostService.create(post_1);
        userPostService.create(post_2);
        var allPosts = userPostService.getAll();
        assertNotNull(allPosts);
        assertTrue(allPosts.size() >= 2);
        System.out.println("All Posts: " + allPosts);
    }
}