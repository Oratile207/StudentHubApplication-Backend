package za.co.studenthub.factory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.ChannelType;
import za.co.studenthub.domain.enums.UserRole;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class ChannelFactoryTest {
    User user_1;
    User user_2;
    User user_3;
    Set<User> channelMembers;
    Channel channel_1;
    Channel channel_2;
    
    @Test
    void createChannel() {
            user_1 = UserFactory.createUser(
            UserRole.FACULTY_MEMBER,
            "Miriam", "Kembell",
            "merriammk@example.com",
            "password",
            null,
            "3499234",
            "A passionate educator with over 10 years of experience in higher education.",
            false,
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
            false,
            null
        );

        user_3 = UserFactory.createUser(
            UserRole.ENTREPRENEUR,
            "Allister", "Mc'Gilliger",
            "sllster@example.com",
            "password",
            null,
            "SMS222342",
            "Creative entrepreneur with a knack for innovative solutions.",
            false,
            null
        );

        //Creating the HashSet of channel members
        channelMembers = new HashSet<>();
            channelMembers.add(user_1);
            channelMembers.add(user_2);
            channelMembers.add(user_3);

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

        assertNotNull(channel_1);
        assertNotNull(channel_2);
        assertNotNull(channel_1.getChannelId());
        assertNotNull(channel_2.getChannelId());
        assertEquals("Application Development Practice 3", channel_1.getChannelName());
        assertEquals("Business Start-Up Ideas", channel_2.getChannelName());
        assertEquals(ChannelType.ACADEMIC_HELP, channel_1.getChannelType());
        assertEquals(ChannelType.PUBLIC_FORUM, channel_2.getChannelType());
    }
}