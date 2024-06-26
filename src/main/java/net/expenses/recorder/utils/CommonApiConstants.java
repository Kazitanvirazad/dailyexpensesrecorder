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
    String AVATAR_API = BASE_API + BASE + "avatar";
    String REGISTER_API = BASE + "register";
    String LOGIN_API = BASE + "login";
    String LOGOUT_API = BASE + "logout";
    String FETCH_ALL_API = BASE + "fetchall";
    String FETCH_API = BASE + "fetch";
    String DELETE_API = BASE + "delete";
    String CREATE_API = BASE + "create";
    String SORT_API = BASE + "sort";
    String DETAIL_API = BASE + "detail";
    String UPDATE_API = BASE + "update";
    String GROUP_BY_API = BASE + "group-by";
    String FILTER_MATCHER_PATTERN = BASE + "**";
    String[] SPRING_SECURITY_JWT_ENDPOINTS = {
            USER_API + LOGOUT_API + FILTER_MATCHER_PATTERN,
            USER_API + DETAIL_API + FILTER_MATCHER_PATTERN,
            USER_API + UPDATE_API + FILTER_MATCHER_PATTERN,
            CATEGORY_API + FILTER_MATCHER_PATTERN,
            ENTRY_API + FILTER_MATCHER_PATTERN,
            ITEM_API + FILTER_MATCHER_PATTERN,
            AVATAR_API + FILTER_MATCHER_PATTERN,
    };
    String[] ALLOWED_HTTP_METHODS = {"HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"};
}
