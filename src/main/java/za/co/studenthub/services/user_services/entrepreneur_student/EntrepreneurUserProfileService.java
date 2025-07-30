package za.co.studenthub.services.user_services.entrepreneur_student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.EntrepreneurUserProfile;
import za.co.studenthub.repository.EntrepreneurUserProfileRepository;

import java.util.List;


@Service
public class EntrepreneurUserProfileService implements IEntrepreneurUserProfileService {

    private final EntrepreneurUserProfileRepository repository;

    @Autowired
    public EntrepreneurUserProfileService(EntrepreneurUserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public EntrepreneurUserProfile create(EntrepreneurUserProfile entrepreneurUserProfile) {
        return repository.save(entrepreneurUserProfile);
    }

    @Override
    public EntrepreneurUserProfile read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public EntrepreneurUserProfile update(EntrepreneurUserProfile entrepreneurUserProfile) {
        return repository.save(entrepreneurUserProfile);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<EntrepreneurUserProfile> getAll() {
        return repository.findAll();
    }
}
