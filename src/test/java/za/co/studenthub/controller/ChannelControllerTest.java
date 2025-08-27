package za.co.studenthub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.ChannelType;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.factory.ChannelFactory;
import za.co.studenthub.factory.UserFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChannelControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/channel";


    private static User user_1;
    private static User user_2;
    private static User user_3;
    private static Set<User> channelMembers;
    private static Channel channel_1;
    private static Channel channel_2;

    public ChannelControllerTest(ChannelController controller) {
    }

    @BeforeAll
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

        user_3 = UserFactory.createUser(
            UserRole.ENTREPRENEUR,
            "Allister", "Mc'Gilliger",
            "sllster@example.com",
            "password",
            null,
            "SMS222342",
            "Creative entrepreneur with a knack for innovative solutions.",
            fail(null, null),
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
    }

    @Test
    @Order(1)
    void create() {
        String url = BASE_URL + "/create";
        Channel createdChannel = this.restTemplate.postForObject(url, channel_1, Channel.class);
        System.out.println("Created: " + createdChannel);
        ResponseEntity <Channel> response = this.restTemplate.getForEntity(url + "/" + createdChannel.getChannelId(), Channel.class);
        assertNotNull(response.getBody());
        assertEquals(createdChannel.getChannelId(), response.getBody().getChannelId());
        assertEquals(createdChannel.getChannelName(), response.getBody().getChannelName());
        assertEquals(createdChannel.getChannelType(), response.getBody().getChannelType());
        assertEquals(createdChannel.getChannelMembers(), response.getBody().getChannelMembers());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    @Order(2)
    void read() {
        String url = BASE_URL + "read/" + channel_1.getChannelId();
        ResponseEntity<Channel> response = this.restTemplate.getForEntity(url, Channel.class);
        assertNotNull(response.getBody());
        assertEquals(channel_1.getChannelId(), response.getBody().getChannelId());
        assertEquals(channel_1.getChannelName(), response.getBody().getChannelName());
        assertEquals(channel_1.getChannelType(), response.getBody().getChannelType());
        assertEquals(channel_1.getChannelMembers(), response.getBody().getChannelMembers());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    @Order(3)
    void update() {
        String url = BASE_URL + "/update";
        Channel updatedChannel = channel_1.toBuilder().channelName("ADP3 - Updated").build();
        this.restTemplate.postForObject(url, updatedChannel, Channel.class);
        ResponseEntity<Channel> response = this.restTemplate.getForEntity(url + "/" + updatedChannel.getChannelId(), Channel.class);
        assertNotNull(response.getBody());
        assertEquals(updatedChannel.getChannelId(), response.getBody().getChannelId());
        assertEquals(updatedChannel.getChannelName(), response.getBody().getChannelName());
        assertEquals(updatedChannel.getChannelType(), response.getBody().getChannelType());
        assertEquals(updatedChannel.getChannelMembers(), response.getBody().getChannelMembers());
        System.out.println("Updated: " + response.getBody());
    }

    @Test
    @Order(4)
    void delete() {
        String url = BASE_URL + "/delete/" + channel_1.getChannelId();
        this.restTemplate.delete(url);
        ResponseEntity<Channel> response = this.restTemplate.getForEntity(url, Channel.class);
        assertNull(response.getBody());
        System.out.println("Deleted: " + response.getBody());
    }

    @Test
    @Order(5)
    void getAll() {
        String url = BASE_URL + "/getAll";
        ResponseEntity<Channel[]> response = this.restTemplate.getForEntity(url, Channel[].class);
        System.out.println("Get All: ");
        for(Channel channel : response.getBody()){
            System.out.println(channel);
        }
    }

    @Test
    @Order(5)
    void joinChannel(){
        String url = BASE_URL + "/joinChannel/" + channel_2.getChannelId() + "/" + user_3.getUserId();
        this.restTemplate.put(url, null);
        ResponseEntity<Channel> response = this.restTemplate.getForEntity(url, Channel.class);
        assertNotNull(response.getBody());
        assertEquals(channel_2.getChannelId(), response.getBody().getChannelId());
        assertEquals(channel_2.getChannelName(), response.getBody().getChannelName());
        assertEquals(channel_2.getChannelType(), response.getBody().getChannelType());
        assertEquals(channel_2.getChannelMembers(), response.getBody().getChannelMembers());
        System.out.println("Joined: " + response.getBody());
    }

    @Test
    @Order(6)
    void leaveChannel(){
        String url = BASE_URL + "/leaveChannel/" + channel_2.getChannelId() + "/" + user_3.getUserId();
        this.restTemplate.put(url, null);
        ResponseEntity<Channel> response = this.restTemplate.getForEntity(url, Channel.class);
        assertNotNull(response.getBody());
        assertEquals(channel_2.getChannelId(), response.getBody().getChannelId());
        assertEquals(channel_2.getChannelName(), response.getBody().getChannelName());
        assertEquals(channel_2.getChannelType(), response.getBody().getChannelType());
        assertEquals(channel_2.getChannelMembers(), response.getBody().getChannelMembers());
        System.out.println("Left: " + response.getBody());
    }

    @Test
    @Order(7)
    void getChannelMembers(){
        String url = BASE_URL + "/getChannelMembers/" + channel_2.getChannelId();
        ResponseEntity<User[]> response = this.restTemplate.getForEntity(url, User[].class);
        System.out.println("Channel Members: ");
        for(User user : response.getBody()){
            System.out.println(user);
        }
    }
}