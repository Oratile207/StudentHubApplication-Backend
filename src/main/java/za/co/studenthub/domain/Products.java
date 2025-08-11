package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.ReturnType;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = {"userProduct"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_product_id", nullable = false)
    private UserProduct userProduct;

    private String productName;
    private String productDescription;

    @Enumerated(EnumType.STRING)
    private ReturnType productReturnType;
}