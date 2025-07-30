package za.co.studenthub.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.GuestProfile;
import za.co.studenthub.services.user_services.guest_profile.GuestProfileService;

import java.util.List;

@RestController
@RequestMapping("/guest_profile")
@CrossOrigin(origins = "http://localhost:3000")
public class GuestProfileController {
    private final GuestProfileService guestProfileService;

    @Autowired
    public GuestProfileController(GuestProfileService guestProfileService) {
        this.guestProfileService = guestProfileService;
    }

    @PostMapping("/create")
    public GuestProfile create(@RequestBody GuestProfile guestProfile) {
        return guestProfileService.create(guestProfile);
    }

    @GetMapping("/read/{id}")
    public GuestProfile read(@PathVariable Long id) {
        return guestProfileService.read(id);
    }

    @PutMapping("/update")
    public GuestProfile update(@RequestBody GuestProfile guestProfile) {
        return guestProfileService.update(guestProfile);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        guestProfileService.delete(id);
    }

    @GetMapping("/getAll")
    public List<GuestProfile> getAll() {
        return guestProfileService.getAll();
    }
}
