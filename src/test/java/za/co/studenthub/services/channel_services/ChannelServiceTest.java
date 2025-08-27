package za.co.studenthub.services.channel_services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.ChannelType;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.factory.ChannelFactory;
import za.co.studenthub.factory.UserFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChannelServiceTest {

    @Autowired
    private ChannelService channelService;

    private static User user_1;
    private static User user_2;
    private static User user_3;
    private static Set<User> channelMembers;
    private static Channel channel_1;
    private static Channel channel_2;

    @BeforeAll
    static void setUp() {
        user_1 = UserFactory.createUser(
            UserRole.FACULTY_MEMBER,
            "Miriam", "Kembell",
            "merriammk@example.com",
            "password",
            null,
            "3499234",
            "A passionate educator with over 10 years of experience in higher education.",
            false,
            ""
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
            ""
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
            ""
        );

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
    }
    
    @Test
    @Order(1)
    void create() {
        channelService.create(channel_1);   
        assertNotNull(channel_1);
        assertNotNull(channel_1.getChannelId());
        System.out.println("Created Channel: " + channel_1);

    }

    @Test
    @Order(2)
    void read() {
        channelService.create(channel_2);
        Channel readChannel = channelService.read(channel_2.getChannelId());
        assertNotNull(readChannel);
        assertEquals(channel_2.getChannelId(), readChannel.getChannelId());
        System.out.println("Read Channel: " + readChannel);
    }

    @Test
    @Order(3)    
    void update() {
        channelService.create(channel_2);
        new Channel();
        Channel updatedChannel = Channel.builder()
            .channelId(channel_2.getChannelId())
            .channelName("Business Start-Up Ideas and Strategies")
            .channelType(ChannelType.PUBLIC_FORUM)
            .adminCreatedChannel(user_2)
            .channelMembers(channelMembers)
            .build();

        channelService.update(updatedChannel);
        Channel readChannel = channelService.read(updatedChannel.getChannelId());
        assertNotNull(readChannel);
        assertEquals("Business Start-Up Ideas and Strategies", readChannel.getChannelName());
        System.out.println("Updated Channel: " + readChannel);
    }

    @Test
    @Order(4)
    void delete() {
        channelService.create(channel_1);
        assertNotNull(channelService.read(channel_1.getChannelId()));
        channelService.delete(channel_1.getChannelId());
        assertNull(channelService.read(channel_1.getChannelId()));
        System.out.println("Deleted Channel with ID: " + channel_1.getChannelId());
    }

    @Test
    @Order(5)
    void getAll() {
        channelService.create(channel_1);
        channelService.create(channel_2);
        List<Channel> allChannels = channelService.getAll();
        assertNotNull(allChannels);
        assertTrue(allChannels.size() >= 2);
        System.out.println("All Channels: " + allChannels);
    }
}