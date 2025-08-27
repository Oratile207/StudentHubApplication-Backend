package za.co.studenthub.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusDto {
    private String status; // ONLINE, AWAY, BUSY, INVISIBLE, OFFLINE
}
