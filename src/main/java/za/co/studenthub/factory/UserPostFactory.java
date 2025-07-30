package za.co.studenthub.factory;

import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.UserPost;
import za.co.studenthub.domain.enums.UserPostType;
import za.co.studenthub.util.Helper;

import java.time.LocalDate;

public class UserPostFactory {
    public static UserPost createUserPost(User user, Channel channel, UserPostType postType, String content) {
        if (user == null || channel == null || postType == null || Helper.isNullOrEmpty(content)) {
            return null;
        }

        return UserPost.builder()
                .userPostId(Helper.generateId())
                .user(user)
                .channel(channel)
                .userPostType(postType)
                .postTimestamp(LocalDate.now())
                .build();
    }
}
