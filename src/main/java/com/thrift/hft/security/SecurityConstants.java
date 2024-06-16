package com.thrift.hft.security;

public class SecurityConstants {

    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String CLAIM_USERID = "userId";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_FULLNAME = "fullname";
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String CLAIM_ROLE_ID = "roleId";
    public static final String CLAIM_USER_ROLE_ID = "userRoleId";
    public static final String CLAIM_USER_TYPE = "userType";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String HEADER_IS_REFRESH_TOKEN = "isRefreshToken";
    public static final String TRUE = "true";
    public static final String CLAIMS = "claims";

    public static final String FILTER_LOGIN_URL = "/delcap/v1/auth/login";
    public static final String EMAIL = "email";
    public static final String FILTER_REGISTER_URL ="/hustleFreeThrift/v1/user/register";
    public static final String URL_API_DOCS = "/v2/api-docs";
    public static final String URL_SWAGGER_UI = "/swagger-ui/**";
    public static final String URL_API_DOCS_ALL = "/v2/api-docs/**";
    public static final String URL_SWAGGER_RESOURCES = "/swagger-resources/**";

    public static final String FILTER_FORGOT_PASSWORD_SUPERADMIN_URL = "/delcap/v1/user/forgot-password/super-admin";
    public static final String FILTER_SWAGGER_URL = "swagger";
    public static final String FILTER_SWAGGER_API_DOCS_URL = "/delcap/v2/api-docs";
    public static final String FILTER_VALIDATE_OTP = "/delcap/v1/notification/otp/validate";
    public static final String FILTER_VALIDATE_SMS_OTP = "/delcap/v1/notification/sms/otp/validate";
    public static final String FILTER_RESEND_SMS_OTP = "/delcap/v1/notification/otp/resend";
    public static final String FILTER_COMMUNICATION_SERVICE = "/delcap/v1/communication/";
}
