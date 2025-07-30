package za.co.studenthub.domain;
import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.UserRole;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="user-profile")
public class UserProfile {
    @Id
    private Long profileId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "admin_id", referencedColumnName = "admin_id")
    private Admin admin;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userId;

    private boolean isEntrepreneurUserProfileEnabled;
    private String sessionUrl;
    private String campusDetails;
    private String courseOStudy;
    private String securityOptions;
    private UserRole userRole;
    private String email;
    private String password;


}
