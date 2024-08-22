package com.datien.lms.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AdminResponse {
    private String firstname;
    private String lastname;
    private String sex;
}
