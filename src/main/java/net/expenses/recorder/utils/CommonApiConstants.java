package net.expenses.recorder.utils;

/**
 * @author Kazi Tanvir Azad
 */
public interface CommonApiConstants extends CommonConstants {
    String BASE = "/";
    String BASE_APP = BASE + "app";
    String BASE_API = BASE + "api";
    String STATUS_API = BASE + "status";
    String FILTER_MATCHER_PATTERN = BASE + "**";
    String[] SPRING_SECURITY_JWT_ENDPOINTS = {
            BASE_API + "/test" + FILTER_MATCHER_PATTERN
    };
}
