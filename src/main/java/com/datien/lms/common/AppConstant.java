package com.datien.lms.common;

public class AppConstant {

    public static final int REGISTER_TOKEN_LENGTH = 12;
    public static final int REGISTER_TOKEN_MINUTES = 60;

    public static final int OTP_LENGTH = 8;
    public static final int PAGE_LIMIT = 12;
    public static final int SEARCH_PAGE_LIMIT = 8;
    public static final int CATEGORY_PAGE_LIMIT = 8;
    public static final int AVATAR_TOTAL = 3;

    public static final String IMAGE_PREFIX = "/views/user/assets/img/avt/avt-";
    public static final String IMAGE_SUFFIX = ".jpg";

    public interface RESPONSE_KEY {
        String RESULT = "RESULT_KEY";
        String DATA = "DATA_KEY";
        String NOTIFICATION = "NOTIFICATION_KEY";
    }

    public interface STATUS {
        int ACTIVE = 0;
        int IN_ACTIVE = 1;
        String IS_DELETED = "Y";
        String IS_UN_DELETED = "N";
    }

}
