package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.Products;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    List<Products> findByProductNameContainingIgnoreCase(String name);

    List<Products> findByProductReturnType(za.co.studenthub.domain.enums.ReturnType returnType);

    List<Products> findByUserProductEntrepreneurUserProfileUserUserId(Long userId);
}