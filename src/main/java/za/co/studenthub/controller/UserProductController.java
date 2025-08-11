package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.UserProduct;
import za.co.studenthub.services.channel_services.UserProductService;

import java.util.List;

@RestController
@RequestMapping("/user_product")
@CrossOrigin(origins = "http://localhost:3000")
public class UserProductController {
    private final UserProductService service;

    @Autowired
    public UserProductController(UserProductService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<UserProduct> create(@RequestBody UserProduct userProduct, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.create(userProduct));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserProduct> read(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        UserProduct userProduct = service.read(id);
        return userProduct != null ? ResponseEntity.ok(userProduct) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<UserProduct> update(@RequestBody UserProduct userProduct, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.update(userProduct));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserProduct>> getAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.getAll());
    }
/*
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserProduct>> getByUserId(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<UserProduct>> getByProductId(@PathVariable Long productId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findByProductId(productId));
    }

 */
}
