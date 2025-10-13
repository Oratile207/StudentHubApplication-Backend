package za.co.studenthub.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String id;  // Frontend expects string
    private String content;
    private String timestamp;  // Frontend expects ISO string
    private boolean edited;
    private String editedAt;    // Frontend expects ISO string
    
    // Author information
    private AuthorDto author;
    
    // Channel ID
    private Long channelId;
    
    // Additional fields for frontend compatibility
    private Long userId;     // Author ID for backward compatibility
    private String userName; // Author name for backward compatibility
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDto {
        private Long id;
        private String name;
        private String email;
        private String avatar;
        private boolean isOnline;
        private String status;
        private String userRole;
    }
}
