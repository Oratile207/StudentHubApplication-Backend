package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.UserPostType;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_posts")
public class UserPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channelId;

    @Enumerated(EnumType.STRING)
    private UserPostType userPostType;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate postTimestamp;
    private boolean isModeratedPost;
    private boolean isUserChannelModerator;
}