package com.datien.lms.dto.request;

import com.datien.lms.dao.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

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

    private Role role;
}
