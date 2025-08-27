package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByChannelNameContainingIgnoreCase(String name);
    
    @Query("SELECT c FROM Channel c WHERE c.adminCreatedChannel = :user")
    List<Channel> findByAdminCreatedChannel(@Param("user") User user);
    
    @Query("SELECT c FROM Channel c JOIN c.channelMembers m WHERE m = :user")
    List<Channel> findByChannelMembersContaining(@Param("user") User user);
    
    @Query("SELECT c FROM Channel c WHERE c.adminCreatedChannel = :user OR :user MEMBER OF c.channelMembers")
    List<Channel> findChannelsForUser(@Param("user") User user);

}