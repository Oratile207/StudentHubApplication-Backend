package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.UserPost;
import za.co.studenthub.services.post_services.UserPostService;

import java.util.List;

@RestController
@RequestMapping("/user_post")
@CrossOrigin(origins = "http://localhost:3000")
public class UserPostController {
    private final UserPostService userPostService;

    @Autowired
    public UserPostController(UserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @PostMapping("/create")
    public UserPost create(@RequestBody UserPost userPost) {
        return userPostService.create(userPost);
    }

    @GetMapping("/read/{id}")
    public UserPost read(@PathVariable Long id) {
        return userPostService.read(id);
    }

    @PutMapping("/update")
    public UserPost update(@RequestBody UserPost userPost) {
        return userPostService.update(userPost);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        userPostService.delete(id);
    }

    @GetMapping("/getAll")
    public List<UserPost> getAll() {
        return userPostService.getAll();
    }
}
