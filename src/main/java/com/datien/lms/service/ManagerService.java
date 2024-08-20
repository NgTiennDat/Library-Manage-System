package com.datien.lms.service;

import com.datien.lms.dao.Admin;
import com.datien.lms.dao.Role;
import com.datien.lms.dto.request.AdminRequest;
import com.datien.lms.repo.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {


    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public void createAdmin(AdminRequest request) {
        var admin = new Admin();
        admin.setFirstname(request.getFirstname());
        admin.setLastname(request.getLastname());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        adminRepository.save(admin);
    }

    public void updateAdminInfo(AdminRequest request, Long adminId) {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found."));
        if(admin.isEnabled()) {
            admin.setFirstname(request.getFirstname());
            admin.setLastname(request.getLastname());
            admin.setEmail(request.getEmail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setRole(Role.ADMIN);
        } else {
            throw new RuntimeException("Admin is not active.");
        }
        adminRepository.save(admin);
    }

    public void deleteAdmin(Long adminId) {
        adminRepository.deleteById(adminId);
    }

    public void getAdminDetail(Long adminId) {
    }
}