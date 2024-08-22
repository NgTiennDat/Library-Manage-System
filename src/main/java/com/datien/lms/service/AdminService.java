package com.datien.lms.service;

import com.datien.lms.dao.Admin;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.Student;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.AdminRequest;
import com.datien.lms.dto.response.AdminResponse;
import com.datien.lms.dto.response.StudentResponse;
import com.datien.lms.exception.OperationNotPermittedException;
import com.datien.lms.repository.AdminRepository;
import com.datien.lms.repository.StudentRepository;
import com.datien.lms.service.mapper.AdminMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;
    private final StudentRepository studentRepository;

    public StudentResponse getStudentById(Long studentId, Authentication connectedUser) throws AccessDeniedException {
        User user = (User) connectedUser.getPrincipal();
        if(user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied because your role is not ADMIN");
        }

        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found."));

        return this.toStudentResponse(student);
    }

    public void createUser(AdminRequest request, Authentication connectedUser) throws AccessDeniedException {
        var admin = new Admin();

        User user = (User) connectedUser.getPrincipal();
        if(user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied because your role is not ADMIN");
        }

        admin.setFirstname(request.getFirstname());
        admin.setLastname(request.getLastname());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        adminRepository.save(admin);
    }

    public AdminResponse updateAdminInfo(
            AdminRequest request,
            Long adminId,
            Authentication connectedUser
    ) throws AccessDeniedException, IllegalAccessException {

        User user = (User) connectedUser.getPrincipal();
        if(user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied because your role is not ADMIN");
        }

        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found."));

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
        return adminMapper.toAdminResponse(admin);
    }

    public void deleteStudent(Long studentId, Authentication connectedUser) throws AccessDeniedException {
        User user = (User) connectedUser.getPrincipal();
        if(user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied because your role is not ADMIN");
        }
        studentRepository.deleteById(studentId);
    }

    private StudentResponse toStudentResponse(Student student) {
        var studentResponse = new StudentResponse();
        studentResponse.setFirstname(student.getFirstname());
        studentResponse.setLastname(student.getLastname());
        studentResponse.setSex(String.valueOf(student.getSex()));
        return studentResponse;
    }

    public Page<StudentResponse> getAllStudent(int page, int size, Authentication connectedUser) throws AccessDeniedException {
        User user = (User) connectedUser.getPrincipal();
        if(user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied because your role is not ADMIN");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentRepository.findAll(pageable);
        return students.map(this::toStudentResponse);
    }
}
