package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.Students;
import za.co.studenthub.services.user_services.student.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Students create(@RequestBody Students student) {
        return service.create(student);
    }

    @GetMapping("/read/{id}")
    public Students read(@PathVariable Long id) {
        return service.read(id);
    }

    @PutMapping("/update")
    public Students update(@RequestBody Students student) {
        return service.update(student);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/getAll")
    public List<Students> getAll() {
        return service.getAll();
    }
}
