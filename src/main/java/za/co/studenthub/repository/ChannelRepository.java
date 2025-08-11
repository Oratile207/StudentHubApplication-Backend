package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.Channel;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByChannelNameContainingIgnoreCase(String name);

}