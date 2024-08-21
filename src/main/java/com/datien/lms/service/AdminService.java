package com.datien.lms.service;

import com.datien.lms.dto.request.AdminRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    public Object getAdminById(Long adminId) {
        return null;
    }

    public void createAdmin(AdminRequest request, Authentication connectedUser) {
    }

    public void updateAdminInfo(AdminRequest request, Long adminId, Authentication connectedUser) {
    }

    public void deleteAdmin(Long adminId) {
    }
}
