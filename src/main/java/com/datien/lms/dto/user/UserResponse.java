package com.datien.lms.dto.user;

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
