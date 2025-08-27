package za.co.studenthub.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMemberDto {
    private Long id;
    private String name;
    private String email;
    private Boolean isOnline;
    private String role; // ADMIN, MODERATOR, MEMBER
    private LocalDateTime joinedAt;
    private String avatarUrl;
    private String status;
    private String userRole;
}
