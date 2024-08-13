package com.datien.lms.dto.request;

import com.datien.lms.dao.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    @NotBlank(message =  "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum.")
    private String password;

    private Role role;
}