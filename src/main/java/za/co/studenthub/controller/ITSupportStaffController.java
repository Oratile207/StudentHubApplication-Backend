package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.ITSupportStaff;
import za.co.studenthub.services.user_services.it_support_staff.ITSupportStaffService;

import java.util.List;

@RestController
@RequestMapping("/it-support-staff")
@CrossOrigin(origins = "http://localhost:3000")
public class ITSupportStaffController {

    private final ITSupportStaffService service;

    @Autowired
    public ITSupportStaffController(ITSupportStaffService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ITSupportStaff create(@RequestBody ITSupportStaff itSupportStaff) {
        return service.create(itSupportStaff);
    }

    @GetMapping("/read/{id}")
    public ITSupportStaff read(@PathVariable Long id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public ITSupportStaff update(@RequestBody ITSupportStaff itSupportStaff) {
        return service.update(itSupportStaff);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/getAll")
    public List<ITSupportStaff> getAll() {
        return service.getAll();
    }
}
