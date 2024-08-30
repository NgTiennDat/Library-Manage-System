//package com.datien.lms.dto.response;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class LoginResponse {
//    @ApiModelProperty(
//            value = "Session sử dụng trong hệ thống",
//            name = "sessionId",
//            dataType = "String",
//            example = "de6b306c-d3ef-43f5-91c5-d2d3173a1769")
//    @JsonProperty("sessionId")
//    private String sessionId;
//
//    @ApiModelProperty(
//            value = "Thông tin tài khoản người dùng",
//            name = "userInfo",
//            dataType = "Object")
//    @JsonProperty("userInfo")
//    private UserInfoDto userInfoDto;
//
//    @ApiModelProperty(
//            value = "Thông tin danh sách các tiện ích phía trên cùng của APP",
//            name = "topUtilities",
//            dataType = "List")
//    @JsonProperty("topUtilities")
//    private List<UtilityDto> topUtilities;
//
//    @ApiModelProperty(
//            value = "Thông tin danh sách các tiện ích ở giữa của APP",
//            name = "topUtilities",
//            dataType = "List")
//    @JsonProperty("middleUtilities")
//    private List<UtilityDto> middleUtilities;
//
//    @ApiModelProperty(
//            value = "Số lần login được phép login sai tối đa",
//            name = "maxErrNo",
//            dataType = "Integer")
//    @JsonProperty("maxErrNo")
//    private int maxErrNo;
//
//    @ApiModelProperty(
//            value = "Số lần login fail",
//            name = "errCount",
//            dataType = "Integer")
//    @JsonProperty("errCount")
//    private int errCount;
//}
