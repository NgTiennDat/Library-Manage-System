package com.datien.lms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {

    @JsonProperty("access-token")
    private String accessToken;

    @JsonProperty("notification")
    private String notification;
}
