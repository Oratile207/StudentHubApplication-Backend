package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.Products;
import za.co.studenthub.services.channel_services.ProductsService;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductsService productsService;

    @Autowired
    public ProductController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping("/create")
    public Products create(@RequestBody Products products) {
        return productsService.create(products);
    }

    @GetMapping("/read/{id}")
    public Products read(@PathVariable Long id) {
        return productsService.read(id);
    }

    @PutMapping("/update")
    public Products update(@RequestBody Products products) {
        return productsService.update(products);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        productsService.delete(id);
    }

    @GetMapping("/getAll")
    public List<Products> getAll() {
        return productsService.getAll();
    }
}
