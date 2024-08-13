package com.datien.lms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "firstname is mandatory.")
    private String username;

    @NotBlank(message = "email is mandatory.")
    @Email(message = "request valid email.")
    private String email;

    @NotBlank(message = "password is mandatory.")
    @Size(min = 8, message = "password has at least 8 characters.")
    private String password;
}
