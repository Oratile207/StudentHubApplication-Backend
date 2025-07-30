package za.co.studenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.studenthub.domain.ITSupportStaff;

@Repository
public interface ITSupportStaffRepository  extends JpaRepository<ITSupportStaff, Long> {
}
