package com.datien.lms.dto.request.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseRequest {
    @JsonProperty("deviceId")
    @NotNull
    @NotEmpty
    private String deviceId;

    @JsonProperty("sessionId")
    private String sessionId;
}
