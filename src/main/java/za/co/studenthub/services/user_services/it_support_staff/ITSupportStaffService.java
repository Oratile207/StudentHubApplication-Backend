package za.co.studenthub.services.user_services.it_support_staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.ITSupportStaff;
import za.co.studenthub.repository.ITSupportStaffRepository;

import java.util.List;

@Service
public class ITSupportStaffService implements IITSupportStaffServices {

    private final ITSupportStaffRepository repository;

    @Autowired
    public ITSupportStaffService(ITSupportStaffRepository repository) {
        this.repository = repository;
    }

    @Override
    public ITSupportStaff create(ITSupportStaff itSupportStaff) {
        return repository.save(itSupportStaff);
    }

    @Override
    public ITSupportStaff read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ITSupportStaff update(ITSupportStaff itSupportStaff) {
        return repository.save(itSupportStaff);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ITSupportStaff> getAll() {
        return repository.findAll();
    }
}
