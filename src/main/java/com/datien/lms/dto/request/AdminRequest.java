package com.datien.lms.dto.request;

import com.datien.lms.dao.Role;
import com.datien.lms.dao.SEX;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRequest {

    @NotBlank(message = "firstname is mandatory.")
    private String firstname;

    @NotBlank(message = "lastname is mandatory.")
    private String lastname;

    @NotBlank(message = "email is mandatory.")
    @Email(message = "request valid email.")
    private String email;

    @NotBlank(message = "password is mandatory.")
    @Size(min = 8, message = "password has at least 8 characters.")
    private String password;

    @NotBlank(message = "phone is mandatory.")
    @Size(min = 10, message = "password has at least 10 characters.")
    private String phone;

    @NotBlank(message = "login count is mandatory and equal to 0.")
    private int loginCount;

    @NotNull(message = "sex is mandatory.")
    private SEX sex;

    private boolean enabled;

    @NotNull(message = "sex is mandatory.")
    private Role role;
}
