package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.UserPost;

import java.util.List;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    List<UserPost> findByUserUserId(Long userId); // Changed from findByUserId

    List<UserPost> findByContentContainingIgnoreCase(String content);
}