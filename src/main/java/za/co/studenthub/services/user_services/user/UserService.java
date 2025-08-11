package za.co.studenthub.services.user_services.user;

import org.springframework.stereotype.Service;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        if (user == null || user.getUserRole() == null) {
            throw new IllegalArgumentException("User and userRole cannot be null");
        }
        user.setUserRole(user.getUserRole() != null ? user.getUserRole() : UserRole.STUDENT);
        if (user.getEntrepreneurProfile() != null) {
            user.getEntrepreneurProfile().setUser(user); // Set bidirectional relation
        }
        return userRepository.save(user);
    }

    @Override
    public User read(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User update(User user) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("User and userId cannot be null for update");
        }
        if (user.getEntrepreneurProfile() != null) {
            user.getEntrepreneurProfile().setUser(user);
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null for deletion");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findByUserEmail(String email) {
        return userRepository.findByUserEmail(email).orElse(null); // Fixed to call findByUserEmail
    }
}