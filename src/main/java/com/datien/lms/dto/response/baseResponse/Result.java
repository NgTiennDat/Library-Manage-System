package com.datien.lms.dto.response.baseResponse;

import com.datien.lms.handlerException.ResponseCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @JsonProperty("responseCode")
    private String code;

    @JsonProperty("isOK")
    private boolean isOk;

    @JsonProperty("responseMessage")
    private String message;

    public static Result OK() {
        return new Result("00", true, "successful");
    }

    public static Result errorFromCode(ResponseCode responseCode) {
        return new Result(responseCode.getCode(), false, responseCode.getMessage());
    }

    public static Result SYSTEM_ERR() {
        return new Result("ERR_001_5001", false, "System error!");
    }
}