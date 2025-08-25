package za.co.studenthub.util;

import za.co.studenthub.domain.User;
import za.co.studenthub.dto.AuthResponse;

public class UserResponseFormatter {
    
    public static AuthResponse.UserInfo formatUserForFrontend(User user) {
        if (user == null) {
            return null;
        }
        
        return new AuthResponse.UserInfo(
            user.getId(),
            user.getUserEmail(), // Using email as username for now
            user.getUserEmail(),
            user.getUserFirstName() + " " + user.getUserLastName(),
            user.getAvatar() != null ? user.getAvatar() : 
                "https://ui-avatars.com/api/?name=" + user.getUserFirstName() + "&background=7289da&color=fff",
            user.getStatus() != null ? user.getStatus() : "offline",
            user.isOnline(),
            user.getUserRole() != null ? user.getUserRole().name() : "STUDENT",
            user.getCreatedAt() != null ? user.getCreatedAt() : java.time.LocalDateTime.now(),
            user.getLastSeen() != null ? user.getLastSeen() : java.time.LocalDateTime.now()
        );
    }
    
    public static AuthResponse createAuthResponse(String token, User user) {
        return new AuthResponse(token, formatUserForFrontend(user));
    }
}
