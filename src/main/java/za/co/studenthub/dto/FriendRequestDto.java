package za.co.studenthub.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {
    private Long userId;
    private Long fromUserId;
    private String fromUserName;
    private String fromUserEmail;
    private String fromUserAvatar;
    private Long toUserId;
    private String toUserName;
    private String toUserEmail;
    private String status; // PENDING, ACCEPTED, REJECTED, BLOCKED
    private LocalDateTime createdAt;
}
