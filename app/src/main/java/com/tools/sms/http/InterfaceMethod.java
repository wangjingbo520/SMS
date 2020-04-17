package com.tools.sms.http;

/**
 * @author wjb（C）
 * describe
 */
public class InterfaceMethod {

    //dev
    public static final String base_url = "http://192.168.43.79:9090/";

    //pro
   // public static final String base_url = "http://106.13.102.216/";

    public static final String LOGIN = "user/login";
    public static final String EXIT_LOGIN = "user/exitLogin";
    public static final String REGISTER = "user/register";
    public static final String UPDATE_PASSWORD = "user/updatePassWord";
    public static final String QUERY_USER_INFO = "user/querUserIByName";
    public static final String APDATE = "user/checkAppVersion";
    public static final String USER_ADVICE = "user/advice";
    public static final String USER_RELOGIN = "user/reLogin";
    public static final String USER_CHARGE = "user/charge";
    public static final String USER_UPDATE_APK = "user/downloadApk";

}
