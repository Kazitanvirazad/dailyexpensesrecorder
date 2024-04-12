package net.expenses.recorder.utils;

/**
 * @author Kazi Tanvir Azad
 */
public interface CommonApiConstants extends CommonConstants {
    String BASE = "/";
    String BASE_APP = BASE + "app";
    String BASE_API = BASE + "api";
    String STATUS_API = BASE + "status";
    String USER_API = BASE_API + BASE + "user";
    String CATEGORY_API = BASE_API + BASE + "category";
    String ENTRY_API = BASE_API + BASE + "entry";
    String ITEM_API = BASE_API + BASE + "item";
    String REGISTER_API = BASE + "register";
    String LOGIN_API = BASE + "login";
    String LOGOUT_API = BASE + "logout";
    String FETCH_ALL_API = BASE + "fetchall";
    String FETCH_API = BASE + "fetch";
    String DELETE_API = BASE + "delete";
    String NEW_API = BASE + "new";
    String SORT_API = BASE + "sort";
    String FILTER_MATCHER_PATTERN = BASE + "**";
    String[] SPRING_SECURITY_JWT_ENDPOINTS = {
            USER_API + "/test" + FILTER_MATCHER_PATTERN,
            USER_API + LOGOUT_API + FILTER_MATCHER_PATTERN,
            USER_API + CATEGORY_API + FILTER_MATCHER_PATTERN,
            USER_API + ENTRY_API + FILTER_MATCHER_PATTERN,
            USER_API + ITEM_API + FILTER_MATCHER_PATTERN
    };
}
