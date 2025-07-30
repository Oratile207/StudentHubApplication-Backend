package za.co.studenthub.domain;
import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.UserProfileType;

import java.util.Set;

@Entity
@Getter
@Builder
@ToString(exclude = {"userProducts"}) // Exclude collections from toString to prevent infinite loops
@Table(name = "entrepreneur_profile")
@NoArgsConstructor
@AllArgsConstructor
public class EntrepreneurUserProfile {
    @Id
    private Long id;

    private boolean isCommercePotrfolioEnabled;
    private String sessionUrl;

    private String biography;
    @Enumerated(EnumType.STRING)
    private UserProfileType userProfileType;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToMany(mappedBy = "entrepreneurProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserProduct> userProducts;

}
