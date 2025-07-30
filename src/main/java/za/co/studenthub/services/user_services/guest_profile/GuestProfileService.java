package za.co.studenthub.services.user_services.guest_profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.GuestProfile;
import za.co.studenthub.repository.GuestProfileRepository;
import za.co.studenthub.services.IService;

import java.util.List;

@Service
public class GuestProfileService implements IGuestProfileService {

    private final GuestProfileRepository repository;

    @Autowired
    public GuestProfileService(GuestProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public GuestProfile create(GuestProfile guestProfile) {
        return repository.save(guestProfile);
    }

    @Override
    public GuestProfile read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public GuestProfile update(GuestProfile guestProfile) {
        return repository.save(guestProfile);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<GuestProfile> getAll() {
        return repository.findAll();
    }
}
