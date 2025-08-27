package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import za.co.studenthub.domain.enums.ChannelRole;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "channel_memberships", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "channel_id"}))
public class ChannelMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private ChannelRole role = ChannelRole.MEMBER;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
    
    @Column(name = "left_at")
    private LocalDateTime leftAt;
    
    // Helper method to check if user is admin of this channel
    public boolean isAdmin() {
        return role == ChannelRole.ADMIN;
    }
    
    // Helper method to check if user is moderator or admin
    public boolean canModerate() {
        return role == ChannelRole.ADMIN || role == ChannelRole.MODERATOR;
    }
}
