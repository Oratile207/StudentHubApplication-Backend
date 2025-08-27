package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.Friendship;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.FriendshipStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    
    // Find friendship between two users (bidirectional)
    @Query("SELECT f FROM Friendship f WHERE (f.fromUser = :user1 AND f.toUser = :user2) OR (f.fromUser = :user2 AND f.toUser = :user1)")
    Optional<Friendship> findByUsers(@Param("user1") User user1, @Param("user2") User user2);
    
    // Find all accepted friends of a user
    @Query("SELECT f FROM Friendship f WHERE ((f.fromUser = :user OR f.toUser = :user) AND f.status = :status)")
    List<Friendship> findByUserAndStatus(@Param("user") User user, @Param("status") FriendshipStatus status);
    
    // Find pending friend requests sent by user
    List<Friendship> findByFromUserAndStatus(User fromUser, FriendshipStatus status);
    
    // Find pending friend requests received by user
    List<Friendship> findByToUserAndStatus(User toUser, FriendshipStatus status);
    
    // Check if friendship exists between two users
    @Query("SELECT COUNT(f) > 0 FROM Friendship f WHERE (f.fromUser = :user1 AND f.toUser = :user2) OR (f.fromUser = :user2 AND f.toUser = :user1)")
    boolean existsByUsers(@Param("user1") User user1, @Param("user2") User user2);
    
    // Get all friends (accepted friendships) for a user - using simpler approach
    @Query("SELECT f FROM Friendship f WHERE (f.fromUser = :user OR f.toUser = :user) AND f.status = :status")
    List<Friendship> findAcceptedFriendships(@Param("user") User user, @Param("status") FriendshipStatus status);
    
    // Count friends
    @Query("SELECT COUNT(f) FROM Friendship f WHERE (f.fromUser = :user OR f.toUser = :user) AND f.status = :status")
    long countFriends(@Param("user") User user, @Param("status") FriendshipStatus status);
    
    // Find blocked users
    @Query("SELECT f FROM Friendship f WHERE f.fromUser = :user AND f.status = :status")
    List<Friendship> findBlockedByUser(@Param("user") User user, @Param("status") FriendshipStatus status);
}
