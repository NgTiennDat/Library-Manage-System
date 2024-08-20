package com.datien.lms.service;

import com.datien.lms.dao.Admin;
import com.datien.lms.dao.Role;
import com.datien.lms.dto.request.AdminRequest;
import com.datien.lms.dto.response.AdminResponse;
import com.datien.lms.repo.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public AdminResponse getAdminDetail(Long adminId) {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found."));

        var response = this.toAdminResponse(admin);
        adminRepository.save(admin);
        return response;
    }

    public Page<AdminResponse> getAllAdmins(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Admin> adminPage = adminRepository.findAll(pageable);
        return adminPage.map(this::toAdminResponse);
    }

    public AdminResponse toAdminResponse(Admin admin) {
        var adminResponse = new AdminResponse();
        adminResponse.setFirstname(admin.getFirstname());
        adminResponse.setLastname(admin.getLastname());
        adminResponse.setEmail(admin.getEmail());
        return adminResponse;
    }

}
