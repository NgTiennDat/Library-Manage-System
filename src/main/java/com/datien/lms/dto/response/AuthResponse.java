package com.datien.lms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("access-token")
    private String accessToken;

}
