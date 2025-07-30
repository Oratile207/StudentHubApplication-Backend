package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.UserProfile;

@Repository
public interface UserProfileRepository  extends JpaRepository<UserProfile, Long> {
}
