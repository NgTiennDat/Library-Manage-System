package com.datien.lms.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
   private String firstname;
   private String lastname;
   private String email;
}
