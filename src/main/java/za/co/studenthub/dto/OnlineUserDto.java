package za.co.studenthub.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserDto {
    private Long id;
    private String name;
    private String status;
    private LocalDateTime lastSeen;
    private String avatarUrl;
    private Boolean isOnline;
    private String userRole;
}
