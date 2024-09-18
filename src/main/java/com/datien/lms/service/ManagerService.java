package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.Admin;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.AdminRequest;
import com.datien.lms.dto.response.AdminResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.AdminRepository;
import com.datien.lms.service.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManagerService {


    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;

    public Map<Object, Object> createAdmin(AdminRequest request, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.MANAGER) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var admin = new Admin();
            admin.setFirstname(request.getFirstname());
            admin.setLastname(request.getLastname());
            admin.setEmail(request.getEmail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setRole(Role.ADMIN);
            admin.setSex(request.getSex());
            admin.setLoginCount(0);
            admin.setPassword(request.getPhone());
            adminRepository.save(admin);

            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }


    public Map<Object, Object> updateAdminInfo(AdminRequest request, String adminId, Authentication connectedUser) throws IllegalAccessException {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            var admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new RuntimeException("Admin not found."));

            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.MANAGER) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            if (admin.isEnabled()) {
                admin.setFirstname(request.getFirstname());
                admin.setLastname(request.getLastname());
                admin.setEmail(request.getEmail());
                admin.setPassword(passwordEncoder.encode(request.getPassword()));
                admin.setRole(Role.ADMIN);
                admin.setSex(request.getSex());
                admin.setLoginCount(0);
                admin.setPassword(request.getPhone());
                adminRepository.save(admin);
            } else {
                result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, adminMapper.toAdminResponse(admin));
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }


    public Map<Object, Object> deleteAdmin(String adminId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.MANAGER) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            adminRepository.deleteById(adminId);
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }


    public Map<Object, Object> getAllAdmins(int page, int size, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.MANAGER) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<Admin> adminPage = adminRepository.findAll(pageable);
            Page<AdminResponse> adminResponses = adminPage.map(adminMapper::toAdminResponse);

            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, adminResponses);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }


    public Map<Object, Object> getAdminDetail(String adminId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            var admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new RuntimeException("Admin not found."));

            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.MANAGER) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            var response = adminMapper.toAdminResponse(admin);
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, response);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }

}
