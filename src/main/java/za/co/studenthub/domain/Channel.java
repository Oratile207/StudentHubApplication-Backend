package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.ChannelPermissions;
import za.co.studenthub.domain.enums.ChannelType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "channel")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long channelId;

    @ManyToOne
    @JoinColumn(name = "admin_created_channel_user_id")
    private User adminCreatedChannel;

    @OneToMany(mappedBy = "channelId", cascade = CascadeType.ALL)
    private Set<UserPost> userPosts;

    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    private String channelName;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "channel_members",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> channelMembers;

    @ElementCollection(targetClass = ChannelPermissions.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "channel_permissions", joinColumns = @JoinColumn(name = "channel_id"))
    @Column(name = "permission")
    private Set<ChannelPermissions> permissions; // New: Store multiple permissions
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    // Enhanced membership management
    @JsonIgnore
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChannelMembership> memberships = new java.util.ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }
    
    // Helper method to get active members count
    public long getActiveMemberCount() {
        return memberships.stream()
            .filter(m -> m.getIsActive())
            .count();
    }
    
    // Frontend compatibility method
    public boolean getIsPrivate() {
        return channelType == ChannelType.PRIVATE_GROUP;
    }
    
    public void setIsPrivate(boolean isPrivate) {
        this.channelType = isPrivate ? ChannelType.PRIVATE_GROUP : ChannelType.PUBLIC_FORUM;
    }
    
    // For JSON serialization
    @Transient
    public Long getId() {
        return channelId;
    }
    
    @Transient 
    public String getName() {
        return channelName;
    }
}