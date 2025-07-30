package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.EntrepreneurUserProfile;
import za.co.studenthub.services.user_services.entrepreneur_student.EntrepreneurUserProfileService;

import java.util.List;

@RestController
@RequestMapping("/entrepreneur_user_profile")
@CrossOrigin(origins = "http://localhost:3000")
public class EntrepreneurUserProfileController {
    private final EntrepreneurUserProfileService entrepreneurUserProfileService;

    @Autowired
    public EntrepreneurUserProfileController(EntrepreneurUserProfileService entrepreneurUserProfileService) {
        this.entrepreneurUserProfileService = entrepreneurUserProfileService;
    }

    @PostMapping("/create")
    public EntrepreneurUserProfile create(@RequestBody EntrepreneurUserProfile entrepreneurUserProfile) {
        return entrepreneurUserProfileService.create(entrepreneurUserProfile);
    }

    @GetMapping("/read/{id}")
    public EntrepreneurUserProfile read(@PathVariable Long id) {
        return entrepreneurUserProfileService.read(id);
    }

    @PutMapping("/update")
    public EntrepreneurUserProfile update(@RequestBody EntrepreneurUserProfile entrepreneurUserProfile) {
        return entrepreneurUserProfileService.update(entrepreneurUserProfile);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        entrepreneurUserProfileService.delete(id);
    }

    @GetMapping("/getAll")
    public List<EntrepreneurUserProfile> getAll() {
        return entrepreneurUserProfileService.getAll();
    }
}
