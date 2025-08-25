package za.co.studenthub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserInfo user;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String displayName;
        private String avatar;
        private String status;
        private boolean isOnline;
        private String userRole;
        private LocalDateTime createdAt;
        private LocalDateTime lastSeen;
    }
}
