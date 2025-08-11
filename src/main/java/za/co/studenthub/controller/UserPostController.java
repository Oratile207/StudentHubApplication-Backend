package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.UserPost;
import za.co.studenthub.services.post_services.UserPostService;

import java.util.List;

@RestController
@RequestMapping("/user_post")
@CrossOrigin(origins = "http://localhost:3000")
public class UserPostController {
    private final UserPostService service;

    @Autowired
    public UserPostController(UserPostService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<UserPost> create(@RequestBody UserPost post, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.create(post));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserPost> read(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        UserPost post = service.read(id);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<UserPost> update(@RequestBody UserPost post, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.update(post));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserPost>> getAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.getAll());
    }

    // Uncomment and update this endpoint
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserPost>> getByUserId(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    // Uncomment if needed
    @GetMapping("/search")
    public ResponseEntity<List<UserPost>> searchByContent(@RequestParam String content, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findByContent(content));
    }
}