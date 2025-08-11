package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.ChannelPermissions;
import za.co.studenthub.domain.enums.ChannelType;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
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

    @ElementCollection(targetClass = ChannelPermissions.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "channel_permissions", joinColumns = @JoinColumn(name = "channel_id"))
    @Column(name = "permission")
    private Set<ChannelPermissions> permissions; // New: Store multiple permissions
}