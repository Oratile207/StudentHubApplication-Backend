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
    public UserProfile create(@RequestBody UserProfile userProfile) {
        return userProfileService.create(userProfile);
    }

    @GetMapping("/read/{id}")
    public UserProfile read(@PathVariable long id) {
        return userProfileService.read(id);
    }

    @PutMapping("/update")
    public UserProfile update(@RequestBody UserProfile userProfile) {
        return userProfileService.update(userProfile);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {
        userProfileService.delete(id);
    }

    @GetMapping("/getAll")
    public List<UserProfile> getAll() {
        return userProfileService.getAll();
    }
}
