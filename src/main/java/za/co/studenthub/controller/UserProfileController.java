package za.co.studenthub.controller;

import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.UserProfile;
import za.co.studenthub.services.channel_services.UserProfileService;

import java.util.List;

@RestController
@RequestMapping("/user_profile")
@CrossOrigin(origins = "http://localhost:3000")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/create")
    public UserProfile create(@RequestBody UserProfile userProfile, @RequestHeader("Authorization") String token) {
        return userProfileService.create(userProfile);
    }

    @GetMapping("/read/{id}")
    public UserProfile read(@PathVariable long id, @RequestHeader("Authorization") String token) {
        return userProfileService.read(id);
    }

    @PutMapping("/update")
    public UserProfile update(@RequestBody UserProfile userProfile, @RequestHeader("Authorization") String token) {
        return userProfileService.update(userProfile);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id, @RequestHeader("Authorization") String token) {
        userProfileService.delete(id);
    }

    @GetMapping("/getAll")
    public List<UserProfile> getAll(@RequestHeader("Authorization") String token) {
        return userProfileService.getAll();
    }

    @GetMapping("/search")
    public List<UserProfile> searchByName(@RequestParam String firstName, @RequestParam String lastName, @RequestHeader("Authorization") String token) {
        return userProfileService.findByNameContaining(firstName, lastName);
    }

    @GetMapping("/search/email")
    public List<UserProfile> searchByEmail(@RequestParam String email, @RequestHeader("Authorization") String token) {
        return userProfileService.findByEmailContaining(email);
    }
}