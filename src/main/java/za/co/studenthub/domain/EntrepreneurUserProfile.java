package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = {"userProducts"})
@Table(name = "entrepreneur_profile")
@NoArgsConstructor
@AllArgsConstructor
public class EntrepreneurUserProfile {
    @Id
    @Column(name = "user_id")
    private Long entrepreneurUserId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_commerce_portfolio_enabled")
    private boolean isCommercePortfolioEnabled;

    @Column(name = "session_url")
    private String sessionUrl;

    @Column(name = "biography")
    private String biography;

    @OneToMany(mappedBy = "entrepreneurUserProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserProduct> userProducts;

}