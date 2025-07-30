package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.FacultyMembers;
import za.co.studenthub.services.user_services.faculty_members.FacultyMembersService;

import java.util.List;

@RestController
@RequestMapping("/faculty-members")
@CrossOrigin(origins = "http://localhost:3000")
public class FacultyMemberController {

    private final FacultyMembersService service;

    @Autowired
    public FacultyMemberController(FacultyMembersService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public FacultyMembers create(@RequestBody FacultyMembers facultyMember) {
        return service.create(facultyMember);
    }

    @GetMapping("/read/{id}")
    public FacultyMembers read(@PathVariable Long id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public FacultyMembers update(@RequestBody FacultyMembers facultyMember) {
        return service.update(facultyMember);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/getAll")
    public List<FacultyMembers> getAll() {
        return service.getAll();
    }
}
