package za.co.studenthub.services.post_services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.UserPost;
import za.co.studenthub.repository.UserPostRepository;

import java.util.List;

@Service
public class UserPostService implements IUserPostService {

    private final UserPostRepository repository;

    @Autowired
    public UserPostService(UserPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserPost create(UserPost userPost) {
        return repository.save(userPost);
    }

    @Override
    public UserPost read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public UserPost update(UserPost userPost) {
        return repository.save(userPost);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<UserPost> getAll() {
        return repository.findAll();
    }
}