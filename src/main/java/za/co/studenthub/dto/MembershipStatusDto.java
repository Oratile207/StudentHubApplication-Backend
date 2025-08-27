package za.co.studenthub.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipStatusDto {
    private Boolean isMember;
    private String role;
    private LocalDateTime joinedAt;
    private Boolean isActive;
}
