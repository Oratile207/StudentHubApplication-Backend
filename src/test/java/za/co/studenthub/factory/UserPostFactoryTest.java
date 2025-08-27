package za.co.studenthub.factory;

import org.springframework.boot.test.context.SpringBootTest;

import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.UserPost;
import za.co.studenthub.domain.enums.ChannelType;
import za.co.studenthub.domain.enums.UserPostType;
import za.co.studenthub.domain.enums.UserRole;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class UserPostFactoryTest {

    void createUserPost() {

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

        Set<User> channelMembers = new HashSet<>();
            channelMembers.add(user_1);
            channelMembers.add(user_2);

        Channel channel = ChannelFactory.createChannel(
            "Application Development Practice 3",
            ChannelType.ACADEMIC_HELP,
            user_2,
            channelMembers
        );

        UserPostType userPostType = UserPostType.TEXT_POST;
        String content = "Don't forget to submit your assignments by the end of this week!";

        UserPost userPost = UserPostFactory.createUserPost(
            user_1,
            channel,
            userPostType,
            content
        );

        assertNotNull(userPost);
        assertNotNull(userPost.getUserPostId());
        assertEquals(user_1, userPost.getUser());
        assertEquals(channel, userPost.getChannelId());
        assertEquals(userPostType, userPost.getUserPostType());
        assertEquals(content, userPost.getContent());
    
    }
}