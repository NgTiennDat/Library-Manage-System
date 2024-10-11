package com.datien.lms.dto.request.model;

import com.datien.lms.dto.request.base.BaseRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthRequest extends BaseRequest {
    @NotBlank(message =  "email is mandatory")
    @Email(message = "please, put valid email.")
    @JsonProperty("username")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum.")
    @JsonProperty("password")
    private String password;
}
