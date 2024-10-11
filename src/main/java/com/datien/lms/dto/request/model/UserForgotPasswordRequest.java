package com.datien.lms.dto.request.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForgotPasswordRequest {
    @NotEmpty(message = "Password is mandatory.")
    @NotBlank(message = "Password is mandatory.")
    @Email(message = "email not valid")
    private String email;
}
