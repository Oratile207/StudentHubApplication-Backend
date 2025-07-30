package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.Students;
import za.co.studenthub.domain.User;

@Repository
public interface StudentsRepository  extends JpaRepository<Students, Long> {
}
