package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.Admin;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.Student;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.AdminRequest;
import com.datien.lms.dto.request.StudentRequest;
import com.datien.lms.dto.response.StudentResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.AdminRepository;
import com.datien.lms.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.datien.lms.dao.Role.STUDENT;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;

    public Map<Object, Object> getStudentById(String studentId, Authentication authentication) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            User user = (User) authentication.getPrincipal();
            if(user.getRole() ==  STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }
            var student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("No student found with Id" + studentId));
            var responseStudent = this.toStudentResponse(student);

            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, responseStudent);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> createUser(StudentRequest request, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        String notification = "";
        try {
            var student = new Student();

            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() == STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }
            String userId = UUID.randomUUID().toString();
            student.setId(userId);
            student.setFirstname(request.getFirstname());
            student.setLastname(request.getLastname());
            student.setEmail(request.getEmail());
            student.setPassword(passwordEncoder.encode(request.getPassword()));
            student.setPhone(request.getPhone());
            student.setIsDeleted("N");
            student.setLoginCount(0);
            student.setSex(request.getSex());
            student.setEnabled(request.isEnabled());
            student.setRole(request.getRole());

            if(request.getRole() != STUDENT) {
                result = new Result(ResponseCode.ROLE_NOT_VALID.getCode(), false, ResponseCode.ROLE_NOT_VALID.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            studentRepository.save(student);

            notification = "Create user successfully";
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> updateAdminInfo(
            AdminRequest request,
            String adminId,
            Authentication connectedUser
    ) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            User user = (User) connectedUser.getPrincipal();
            if(user.getRole() ==  STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            var admin = adminRepository.findById(adminId).orElseThrow(EntityNotFoundException::new);
            if(admin.isEnabled()) {
                admin.setFirstname(request.getFirstname());
                admin.setLastname(request.getLastname());
                admin.setEmail(request.getEmail());
                admin.setPassword(passwordEncoder.encode(request.getPassword()));
                admin.setRole(Role.ADMIN);
                admin.setLoginCount(0);
                admin.setIsDeleted(request.getIsDeleted());
                admin.setSex(request.getSex());
                admin.setPhone(request.getPhone());
                admin.setEnabled(request.isEnabled());
                adminRepository.save(admin);
            } else {
                result = new Result(ResponseCode.ACCOUNT_LOCKED.getCode(), false, ResponseCode.ACCOUNT_LOCKED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            notification = "Successfully updated admin info.";

        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> deleteStudent(String studentId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() == STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            studentRepository.deleteById(studentId);
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }

    private StudentResponse toStudentResponse(Student student) {
        var studentResponse = new StudentResponse();
        studentResponse.setFirstname(student.getFirstname());
        studentResponse.setLastname(student.getLastname());
        studentResponse.setSex(String.valueOf(student.getSex()));
        return studentResponse;
    }

    public Map<Object, Object> getAllStudent(int page, int size, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.ADMIN) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<Student> students = studentRepository.findAll(pageable);
            Page<StudentResponse> studentResponses = students.map(this::toStudentResponse);

            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, studentResponses);
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }

}
