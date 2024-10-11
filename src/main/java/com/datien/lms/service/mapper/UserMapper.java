package com.datien.lms.service.mapper;

import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.model.UserRequest;
import com.datien.lms.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toUser(UserRequest request) {
        var user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.STUDENT);
        user.setPhone("");
        return user; // Trả về đối tượng User đã tạo
    }

    public UserResponse toUserResponse(User user) {
        if (user != null) {
            var userResponse = new UserResponse();

            userResponse.setLastname(user.getLastname());
            userResponse.setEmail(user.getEmail());
            userResponse.setNotification("Successfully register account!");
            return userResponse;
        }
        return null; // Xử lý trường hợp user == null (tuỳ theo yêu cầu của ứng dụng)
    }
}
