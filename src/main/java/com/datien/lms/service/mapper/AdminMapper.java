package com.datien.lms.service.mapper;

import com.datien.lms.dao.Admin;
import com.datien.lms.dto.response.AdminResponse;
import org.springframework.stereotype.Service;

@Service
public class AdminMapper {

    public AdminResponse toAdminResponse(Admin admin) {
        var adminResponse = new AdminResponse();
        adminResponse.setFirstname(admin.getFirstname());
        adminResponse.setLastname(admin.getLastname());
        adminResponse.setEmail(admin.getEmail());
        return adminResponse;
    }
}
