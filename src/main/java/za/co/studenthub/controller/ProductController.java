package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.Products;
import za.co.studenthub.services.channel_services.ProductsService;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductsService service;

    @Autowired
    public ProductController(ProductsService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Products> create(@RequestBody Products product, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.create(product));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Products> read(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Products product = service.read(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Products> update(@RequestBody Products product, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.update(product));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Products>> getAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.getAll());
    }
/*
    @GetMapping("/search")
    public ResponseEntity<List<Products>> searchByName(@RequestParam String name, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Products>> getByCategory(@PathVariable String category, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findByCategory(category));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Products>> getBySellerId(@PathVariable Long sellerId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findBySellerId(sellerId));
    }

 */
}
