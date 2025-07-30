package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;

@Repository
public interface ChannelRepository  extends JpaRepository<Channel, Long> {}
