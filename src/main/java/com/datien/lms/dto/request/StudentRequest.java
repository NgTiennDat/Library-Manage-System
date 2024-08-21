package com.datien.lms.dto.request;

import lombok.Data;

@Data
public class StudentRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String sex;
    private boolean enabled;
}
