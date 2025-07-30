package za.co.studenthub.services.channel_services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.UserProduct;
import za.co.studenthub.repository.UserProductRepository;

import java.util.List;

@Service
public class UserProductService implements IUserProductService {

    private final UserProductRepository repository;

    @Autowired
    public UserProductService(UserProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserProduct create(UserProduct userProduct) {
        return repository.save(userProduct);
    }

    @Override
    public UserProduct read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public UserProduct update(UserProduct userProduct) {
        return repository.save(userProduct);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<UserProduct> getAll() {
        return repository.findAll();
    }
}
