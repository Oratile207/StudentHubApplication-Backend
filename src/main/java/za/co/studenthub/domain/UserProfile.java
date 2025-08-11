package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile")
public class UserProfile {
    @Id
    private Long profileId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isEntrepreneurUserProfileEnabled;
    private String sessionUrl;
    private String campusDetails;
    private String courseOfStudy;
    private String securityOptions;
}