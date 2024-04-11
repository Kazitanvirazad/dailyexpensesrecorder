package net.expenses.recorder.utils;

/**
 * @author Kazi Tanvir Azad
 */
public interface CommonApiConstants extends CommonConstants {
    String BASE = "/";
    String BASE_APP = BASE + "app";
    String BASE_API = BASE + "api";
    String STATUS_API = BASE + "status";
    String USER_API = BASE_API + "user";
    String REGISTER_API = BASE + "register";
    String LOGIN_API = BASE + "login";
    String LOGOUT_API = BASE + "logout";
    String FILTER_MATCHER_PATTERN = BASE + "**";
    String[] SPRING_SECURITY_JWT_ENDPOINTS = {
            BASE + "test" + FILTER_MATCHER_PATTERN,
            USER_API + LOGOUT_API + FILTER_MATCHER_PATTERN,
//            USER_API + LOGIN_API + FILTER_MATCHER_PATTERN
    };
}
