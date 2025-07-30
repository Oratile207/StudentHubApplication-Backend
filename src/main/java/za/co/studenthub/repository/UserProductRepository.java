package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.UserProduct;

@Repository
public interface UserProductRepository  extends JpaRepository<UserProduct, Long> {
}
