package za.co.studenthub.services.user_services.faculty_members;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.FacultyMembers;
import za.co.studenthub.repository.FacultyMembersRepository;

import java.util.List;

@Service
public class FacultyMembersService implements IFacultyMembersService {

    private final FacultyMembersRepository repository;

    @Autowired
    public FacultyMembersService(FacultyMembersRepository repository) {
        this.repository = repository;
    }

    @Override
    public FacultyMembers create(FacultyMembers facultyMember) {
        return repository.save(facultyMember);
    }

    @Override
    public FacultyMembers read(Long id) {
        return (FacultyMembers) repository.findById(id).orElse(null);
    }

    @Override
    public FacultyMembers update(FacultyMembers facultyMember) {
        return repository.save(facultyMember);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<FacultyMembers> getAll() {
        return List.of();
    }
}
