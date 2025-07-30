package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.ReturnType;
import java.util.Set;

@Entity
@Table(name = "user_product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepreneur_profile_id")
    private EntrepreneurUserProfile entrepreneurProfile;

    @OneToMany(mappedBy = "userProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Products> productsSet;

    @Enumerated(EnumType.STRING)
    private ReturnType returnType;

    private String productName;


}
