package com.datien.lms.dto.request.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResetPasswordRequest {

    @NotEmpty(message = "Password is mandatory.")
    @NotBlank(message = "Password is mandatory.")
    @Email(message = "email not valid")
    private String email;

    @NotBlank(message = "email is mandatory")
    @Size(min = 6, max = 6, message = "otp length must be 6")
    private String activateCode;

    @NotEmpty(message = "Password is mandatory.")
    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, message = "Password should be 8 characters long minimum.")
    private String newPassword;
    @NotEmpty(message = "Password is mandatory.")
    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, message = "Password should be 8 characters long minimum.")
    private String confirmNewPassword;

}
