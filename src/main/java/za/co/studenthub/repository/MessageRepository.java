package za.co.studenthub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChannelChannelIdOrderByTimestampDesc(Long channelId, Pageable pageable);
}
