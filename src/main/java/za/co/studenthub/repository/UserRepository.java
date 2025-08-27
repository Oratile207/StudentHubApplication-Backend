package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String email);
    
    // Search users by name or email
    List<User> findByUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCaseOrUserEmailContainingIgnoreCase(
        String firstName, String lastName, String email);
    
    // Find online users
    List<User> findByIsOnlineTrue();
    
    // Find online users by channel membership
    @Query("SELECT DISTINCT u FROM User u " +
           "JOIN ChannelMembership cm ON cm.user = u " +
           "WHERE cm.channel.channelId = :channelId AND cm.isActive = true AND u.isOnline = true")
    List<User> findOnlineUsersByChannel(@Param("channelId") Long channelId);
}