package net.expenses.recorder.constants;

/**
 * @author Kazi Tanvir Azad
 */
public final class ApiConstants {
    public static final String BASE = "/";
    public static final String BASE_APP = BASE + "app";
    public static final String BASE_API = BASE + "api";
    public static final String STATUS_API = BASE + "status";
    public static final String USER_API = BASE_API + BASE + "user";
    public static final String CATEGORY_API = BASE_API + BASE + "category";
    public static final String ENTRY_API = BASE_API + BASE + "entry";
    public static final String ITEM_API = BASE_API + BASE + "item";
    public static final String AVATAR_API = BASE_API + BASE + "avatar";
    public static final String REGISTER_API = BASE + "register";
    public static final String LOGIN_API = BASE + "login";
    public static final String LOGOUT_API = BASE + "logout";
    public static final String FETCH_ALL_API = BASE + "fetchall";
    public static final String FETCH_API = BASE + "fetch";
    public static final String DELETE_API = BASE + "delete";
    public static final String CREATE_API = BASE + "create";
    public static final String SORT_API = BASE + "sort";
    public static final String DETAIL_API = BASE + "detail";
    public static final String UPDATE_API = BASE + "update";
    public static final String GROUP_BY_API = BASE + "group-by";
    public static final String FILTER_MATCHER_PATTERN = BASE + "**";
    public static final String[] SPRING_SECURITY_JWT_ENDPOINTS = {
            USER_API + LOGOUT_API + FILTER_MATCHER_PATTERN,
            USER_API + DETAIL_API + FILTER_MATCHER_PATTERN,
            USER_API + UPDATE_API + FILTER_MATCHER_PATTERN,
            CATEGORY_API + FILTER_MATCHER_PATTERN,
            ENTRY_API + FILTER_MATCHER_PATTERN,
            ITEM_API + FILTER_MATCHER_PATTERN,
            AVATAR_API + FILTER_MATCHER_PATTERN,
    };
    public static final String[] ALLOWED_HTTP_METHODS = {"HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"};
}
