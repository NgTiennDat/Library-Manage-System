package com.datien.lms.common;

import com.datien.lms.handlerException.ResponseCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    @JsonProperty("responseCode")
    private String code;

    @JsonProperty("isOk")
    private boolean isOk;

    @JsonProperty("responseMessage")
    private String message;

    public static Result OK(String message) {
        return new Result("OK", true, "200");
    }

    public static Result errorFromCode(ResponseCode responseCode) {
        return new Result(responseCode.getCode(), false, responseCode.getMessage());
    }

    public static Result SYSTEM_ERROR() {
        return new Result("ERR_501", false, "System error");
    }
}
