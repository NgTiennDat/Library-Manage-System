package com.datien.lms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    @JsonProperty("access-token")
    private String accessToken;

    @JsonProperty("notification")
    private String notification;
}
