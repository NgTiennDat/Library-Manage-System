package com.datien.lms.dto.response.baseResponse;

import com.datien.lms.common.AppConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {
    @NonNull
    private T result;
    private T data;
    private T notification;

    public static <T> ResponseData<T> createResponse(Map<Object, Object> mapData) {
        ResponseData responseData = new ResponseData();
        responseData.setResult(mapData.getOrDefault(AppConstant.RESPONSE_KEY.RESULT, Result.SYSTEM_ERR()));
        responseData.setData(mapData.getOrDefault(AppConstant.RESPONSE_KEY.DATA, null));
        responseData.setNotification(mapData.getOrDefault(AppConstant.RESPONSE_KEY.NOTIFICATION, null));
        return responseData;
    }
}