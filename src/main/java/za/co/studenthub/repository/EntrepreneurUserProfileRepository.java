package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.EntrepreneurUserProfile;

@Repository
    public interface EntrepreneurUserProfileRepository  extends JpaRepository<EntrepreneurUserProfile, Long> {
}
