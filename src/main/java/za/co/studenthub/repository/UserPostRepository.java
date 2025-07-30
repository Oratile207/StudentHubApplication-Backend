package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.UserPost;

@Repository
public interface UserPostRepository  extends JpaRepository<UserPost, Long> {
}
