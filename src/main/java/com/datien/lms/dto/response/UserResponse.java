package com.datien.lms.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
   @NotBlank(message = "firstname is mandatory.")
   private String firstname;

   @NotBlank(message = "lastname is mandatory.")
   private String lastname;

   @NotBlank(message = "email is mandatory.")
   @Email(message = "request valid email.")
   private String email;

   private String notification;
}
