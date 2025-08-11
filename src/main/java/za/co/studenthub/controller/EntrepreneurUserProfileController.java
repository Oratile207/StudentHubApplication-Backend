package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.EntrepreneurUserProfile;
import za.co.studenthub.services.user_services.entrepreneur_student.EntrepreneurUserProfileService;

import java.util.List;

@RestController
@RequestMapping("/entrepreneur_profile")
@CrossOrigin(origins = "http://localhost:3000")
public class EntrepreneurUserProfileController {
    private final EntrepreneurUserProfileService service;

    @Autowired
    public EntrepreneurUserProfileController(EntrepreneurUserProfileService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<EntrepreneurUserProfile> create(@RequestBody EntrepreneurUserProfile profile, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.create(profile));
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<EntrepreneurUserProfile> read(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        EntrepreneurUserProfile profile = service.read(id);
        return profile != null ? ResponseEntity.ok(profile) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<EntrepreneurUserProfile> update(@RequestBody EntrepreneurUserProfile profile, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.update(profile));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<EntrepreneurUserProfile>> getAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.getAll());
    }
/*
    @GetMapping("/search")
    public ResponseEntity<List<EntrepreneurUserProfile>> searchByBusinessName(@RequestParam String businessName, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findByBusinessName(businessName));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EntrepreneurUserProfile>> getByUserId(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

 */
}
