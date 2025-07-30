package za.co.studenthub.services.user_services.admin;

import org.springframework.stereotype.Service;
import za.co.studenthub.domain.Admin;
import za.co.studenthub.repository.AdminRepository;

import java.util.List;

@Service
public class AdminService implements IAdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    @Override
    public Admin create(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin read(Long aLong) {
        return adminRepository.findById(aLong).orElse(null);
    }

    @Override
    public Admin update(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public void delete(Long aLong) {
        adminRepository.deleteById(aLong);
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }
}
