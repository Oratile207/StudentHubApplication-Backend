package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.UserProfile;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /* Creating a Java Persistence Query method so that I can 
     * search for user profiles by email, first name, or last name.
     */
    @Query("SELECT u FROM UserProfile u WHERE LOWER(u.user.userEmail) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<UserProfile> searchByEmail(@Param("email") String email);

    /* Creating another JPQL Query so that I can 
     * search for user profiles by first name or last name.
     */
    @Query("SELECT u FROM UserProfile u " +
        "WHERE LOWER(u.user.userFirstName) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
        "OR LOWER(u.user.userLastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<UserProfile> searchByFirstNameOrLastName(@Param("firstName") String firstName,
                                                @Param("lastName") String lastName);

    
}