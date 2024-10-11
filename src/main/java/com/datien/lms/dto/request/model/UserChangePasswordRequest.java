package com.datien.lms.dto.request.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordRequest {
    @NotEmpty(message = "Password is mandatory.")
    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, message = "Password should be 8 characters long minimum.")
    private String currentPassword;
    @NotEmpty(message = "Password is mandatory.")
    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, message = "Password should be 8 characters long minimum.")
    private String newPassword;
    @NotEmpty(message = "Password is mandatory.")
    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, message = "Password should be 8 characters long minimum.")
    private String confirmPassword;
}
