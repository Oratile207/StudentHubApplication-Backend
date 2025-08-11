package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.EntrepreneurUserProfile;

import java.util.List;

@Repository
public interface EntrepreneurUserProfileRepository extends JpaRepository<EntrepreneurUserProfile, Long> {
    List<EntrepreneurUserProfile> findByBiographyContainingIgnoreCase(String biography);

    List<EntrepreneurUserProfile> findByUserUserId(Long userId);
}