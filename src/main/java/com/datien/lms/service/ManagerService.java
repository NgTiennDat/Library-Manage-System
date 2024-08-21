package com.datien.lms.service;

import com.datien.lms.dao.Admin;
import com.datien.lms.dao.Manager;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.AdminRequest;
import com.datien.lms.dto.response.AdminResponse;
import com.datien.lms.exception.OperationNotPermittedException;
import com.datien.lms.repo.AdminRepository;
import com.datien.lms.service.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {


    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;

    public void createAdmin(AdminRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        if(user.getRole() != Role.MANAGER) {
            throw new OperationNotPermittedException("You dont have permission to update admin!");
        }
        var admin = new Admin();
        admin.setFirstname(request.getFirstname());
        admin.setLastname(request.getLastname());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        adminRepository.save(admin);
    }

    public void updateAdminInfo(AdminRequest request, Long adminId, Authentication connectedUser) throws IllegalAccessException {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found."));

        User user = (User) connectedUser.getPrincipal();
        if(user.getRole() != Role.MANAGER) {
            throw new OperationNotPermittedException("You dont have permission to update admin!");
        }
        if(admin.isEnabled()) {
            admin.setFirstname(request.getFirstname());
            admin.setLastname(request.getLastname());
            admin.setEmail(request.getEmail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setRole(Role.ADMIN);
        } else {
            throw new IllegalAccessException("Admin is not active.");
        }
        adminRepository.save(admin);
    }

    public void deleteAdmin(Long adminId) {
        adminRepository.deleteById(adminId);
    }

    public AdminResponse getAdminDetail(Long adminId, Authentication connectedUser) {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found."));

        User user = (User) connectedUser.getPrincipal();
        if(user.getRole() != Role.MANAGER) {
            throw new OperationNotPermittedException("You dont have permission to update admin!");
        }

        var response = adminMapper.toAdminResponse(admin);
        adminRepository.save(admin);
        return response;
    }

    public Page<AdminResponse> getAllAdmins(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        if(user.getRole() != Role.MANAGER) {
            throw new OperationNotPermittedException("You dont have permission to update admin!");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Admin> adminPage = adminRepository.findAll(pageable);
        return adminPage.map(adminMapper::toAdminResponse);
    }



}
