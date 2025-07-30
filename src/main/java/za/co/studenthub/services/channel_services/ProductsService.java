package za.co.studenthub.services.channel_services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.Products;
import za.co.studenthub.repository.ProductsRepository;

import java.util.List;

@Service
public class ProductsService implements IProductsService {

    private final ProductsRepository repository;

    @Autowired
    public ProductsService(ProductsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Products create(Products products) {
        return repository.save(products);
    }

    @Override
    public Products read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Products update(Products products) {
        return repository.save(products);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Products> getAll() {
        return repository.findAll();
    }
}