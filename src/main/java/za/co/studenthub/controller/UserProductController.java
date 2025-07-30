package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.UserProduct;
import za.co.studenthub.services.channel_services.UserProductService;

import java.util.List;

@RestController
@RequestMapping("/user_product")
@CrossOrigin(origins = "http://localhost:3000")
public class UserProductController {
    private final UserProductService userProductService;

    @Autowired
    public UserProductController(UserProductService userProductService) {
        this.userProductService = userProductService;
    }

    @PostMapping("/create")
    public UserProduct createUserProduct(@RequestBody UserProduct userProduct) {
        return userProductService.create(userProduct);
    }

    @GetMapping("/read/{id}")
    public UserProduct readUserProduct(@PathVariable Long id) {
        return userProductService.read(id);
    }

    @PutMapping("/update")
    public UserProduct updateUserProduct(@RequestBody UserProduct userProduct) {
        return userProductService.update(userProduct);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserProduct(@PathVariable Long id) {
        userProductService.delete(id);
    }

    @GetMapping("/getAll")
    public List<UserProduct> getAllUserProducts() {
        return userProductService.getAll();
    }
}
