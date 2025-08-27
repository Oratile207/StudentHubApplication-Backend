package za.co.studenthub.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDto {
    private Long id;
    private String name;
    private String email;
    private Boolean isOnline;
    private Boolean isFriend;
    private Boolean requestSent;
    private Boolean requestReceived;
    private String avatarUrl;
    private String status;
    private String userRole;
}
