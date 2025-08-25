package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import za.co.studenthub.domain.enums.UserRole;

@Entity
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Setter
    @Getter
    @Column(name = "user_first_name")
    private String userFirstName;

    @Setter
    @Getter
    @Column(name = "user_last_name")
    private String userLastName;

    @Setter
    @Getter
    @Column(name = "user_email")
    private String userEmail;

    @Setter
    @Getter
    @Column(name = "user_password")
    private String userPassword;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Getter
    @Setter
    @Column(name = "student_number", nullable = true)
    private String studentNumber;

    @Getter
    @Setter
    @Column(name = "staff_number", nullable = true)
    private String staffNumber;

    @Getter
    @Setter
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private EntrepreneurUserProfile entrepreneurProfile;

    @Setter
    @Getter
    @Builder.Default
    @Column(name = "status", nullable = true)
    private String status = "offline";

    @Builder.Default
    @Column(name = "is_online", nullable = false)
    private boolean isOnline = false;

    @Getter
    @Setter
    @Column(name = "avatar", nullable = true)
    private String avatar = "https://ui-avatars.com/api/?name=User&background=7289da&color=fff";

    @Getter
    @Setter
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Getter
    @Setter
    @Column(name = "last_seen")
    private java.time.LocalDateTime lastSeen;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        lastSeen = java.time.LocalDateTime.now();
        if (avatar == null) {
            avatar = "https://ui-avatars.com/api/?name=" + 
                    (userFirstName != null ? userFirstName : "User") + 
                    "&background=7289da&color=fff";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastSeen = java.time.LocalDateTime.now();
    }

    // In za.co.studenthub.domain.User
    public boolean isAdmin() {
        return UserRole.ADMIN.equals(userRole);
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Long getId() {
        return userId;
    }

    public boolean isPresent() {
        return true;
    }
}