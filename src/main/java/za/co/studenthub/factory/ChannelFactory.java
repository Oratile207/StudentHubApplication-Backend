package za.co.studenthub.factory;

import java.util.Set;

import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.ChannelType;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.util.Helper;

public class ChannelFactory {
    public static Channel createChannel(String channelName,
                                        ChannelType channelType,
                                        User admin,
                                        Set<User> channelMembers) {

        if (Helper.isNullOrEmpty(channelName) ||
                channelType == null ||
                admin == null) {
            return null;
        }

        // Initialize channelMembers if null
        if (channelMembers == null) {
            channelMembers = new java.util.HashSet<>();
        }

        // Always add the admin as a member
        channelMembers.add(admin);
        
        // Add some default test users for testing purposes
        User user_1 = UserFactory.createUser(
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

        User user_2 = UserFactory.createUser(
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

        User user_3 = UserFactory.createUser(
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

        // Add test users to channel members
        if (user_1 != null) channelMembers.add(user_1);
        if (user_2 != null) channelMembers.add(user_2);
        if (user_3 != null) channelMembers.add(user_3);
        
        return Channel.builder()
                .channelId(Helper.generateId())
                .channelName(channelName)
                .channelType(channelType)
                .adminCreatedChannel(admin)
                .channelMembers(channelMembers)
                .build();
    }
}