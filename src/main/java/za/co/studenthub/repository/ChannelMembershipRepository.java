package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.ChannelMembership;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.ChannelRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelMembershipRepository extends JpaRepository<ChannelMembership, Long> {
    
    // Find membership by user and channel
    Optional<ChannelMembership> findByUserAndChannel(User user, Channel channel);
    
    // Find active membership by user and channel
    Optional<ChannelMembership> findByUserAndChannelAndIsActiveTrue(User user, Channel channel);
    
    // Check if user is active member of channel
    boolean existsByUserAndChannelAndIsActiveTrue(User user, Channel channel);
    
    // Get all active members of a channel
    List<ChannelMembership> findByChannelAndIsActiveTrueOrderByJoinedAtAsc(Channel channel);
    
    // Get all channels user is actively member of
    List<ChannelMembership> findByUserAndIsActiveTrueOrderByJoinedAtDesc(User user);
    
    // Count active members in channel
    long countByChannelAndIsActiveTrue(Channel channel);
    
    // Find channel admins
    List<ChannelMembership> findByChannelAndRoleAndIsActiveTrue(Channel channel, ChannelRole role);
    
    // Find user's role in channel
    @Query("SELECT cm.role FROM ChannelMembership cm WHERE cm.user = :user AND cm.channel = :channel AND cm.isActive = true")
    Optional<ChannelRole> findUserRoleInChannel(@Param("user") User user, @Param("channel") Channel channel);
    
    // Check if user is admin or moderator of channel
    @Query("SELECT COUNT(cm) > 0 FROM ChannelMembership cm WHERE cm.user = :user AND cm.channel = :channel AND cm.isActive = true AND (cm.role = 'ADMIN' OR cm.role = 'MODERATOR')")
    boolean isUserModeratorOrAdmin(@Param("user") User user, @Param("channel") Channel channel);
    
    // Get online users in channel
    @Query("SELECT cm FROM ChannelMembership cm WHERE cm.channel = :channel AND cm.isActive = true AND cm.user.isOnline = true")
    List<ChannelMembership> findOnlineMembers(@Param("channel") Channel channel);
    
    // Find membership by user ID and channel ID (for easier lookup)
    @Query("SELECT cm FROM ChannelMembership cm WHERE cm.user.userId = :userId AND cm.channel.channelId = :channelId")
    Optional<ChannelMembership> findByUserIdAndChannelId(@Param("userId") Long userId, @Param("channelId") Long channelId);
}
