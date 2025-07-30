package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import za.co.studenthub.domain.enums.UserProfileType;
import za.co.studenthub.domain.enums.UserRole;

@Entity
@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPassword;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserProfileType userProfileType;
}