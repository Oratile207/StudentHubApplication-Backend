package za.co.studenthub.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendDto {
    private Long id;
    private String name;
    private String email;
    private Boolean isOnline;
    private String status; // ONLINE, AWAY, BUSY, etc.
    private String avatarUrl;
    private LocalDateTime lastSeen;
    private String userRole;
}
