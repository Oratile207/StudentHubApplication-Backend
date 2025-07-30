package za.co.studenthub.services.user_services.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.Students;
import za.co.studenthub.repository.StudentsRepository;

import java.util.List;

@Service
public class StudentService implements IStudentService {

    private final StudentsRepository repository;

    @Autowired
    public StudentService(StudentsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Students create(Students student) {
        return repository.save(student);
    }

    @Override
    public Students read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Students update(Students student) {
        return repository.save(student);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Students> getAll() {
        return repository.findAll();
    }
}
