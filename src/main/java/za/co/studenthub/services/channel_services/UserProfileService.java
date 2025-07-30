package za.co.studenthub.services.channel_services;

import org.springframework.stereotype.Service;
import za.co.studenthub.domain.UserProfile;
import za.co.studenthub.repository.UserProfileRepository;

import java.util.List;

@Service
public class UserProfileService implements IUserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }


    @Override
    public UserProfile create(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile read(Long aLong) {
        return userProfileRepository.findById(aLong).orElse(null);
    }

    @Override
    public UserProfile update(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public void delete(Long aLong) {
        userProfileRepository.deleteById(aLong);
    }

    @Override
    public List<UserProfile> getAll() {
        return userProfileRepository.findAll();
    }
}
