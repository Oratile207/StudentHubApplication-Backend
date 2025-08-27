package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.ReturnType;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
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

    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;

    @Enumerated(EnumType.STRING)
    private ReturnType productReturnType;
}