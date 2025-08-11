package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.UserProfile;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    List<UserProfile> findByUserUserEmailContainingIgnoreCase(String email); // Updated from findByEmailContainingIgnoreCase

    List<UserProfile> findByUserUserFirstNameContainingIgnoreCaseOrUserUserLastNameContainingIgnoreCase(String firstName, String lastName);
}