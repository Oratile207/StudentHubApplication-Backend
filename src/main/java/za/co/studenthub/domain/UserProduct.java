package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.ReturnType;

import java.util.Set;

@Entity
@Table(name = "user_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class UserProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepreneur_profile_id")
    private EntrepreneurUserProfile entrepreneurUserProfile;

    @OneToMany(mappedBy = "userProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Products> productsSet;

    @Enumerated(EnumType.STRING)
    private ReturnType returnType;

    private String productName;
}