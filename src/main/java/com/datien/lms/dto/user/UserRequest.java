package com.datien.lms.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
