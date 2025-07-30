package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.UserPostType;
import za.co.studenthub.domain.enums.UserRole;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_posts")
public class UserPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPostId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_post")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel")
    private Channel channel;

    @Enumerated(EnumType.STRING)
    private UserPostType userPostType;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private LocalDate postTimestamp;
    private boolean isModeratedPost;
    private boolean isUserChannelModerator;

}
