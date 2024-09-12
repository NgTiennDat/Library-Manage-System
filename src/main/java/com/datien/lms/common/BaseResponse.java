package com.datien.lms.common;

import lombok.Data;

@Data
public class BaseResponse {
    Result result;
    Object data;
    String notification;
}
