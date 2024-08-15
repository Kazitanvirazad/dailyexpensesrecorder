package net.expenses.recorder.constants;

/**
 * @author Kazi Tanvir Azad
 */
public final class CommonConstants  {
    public static final String SINGLE_WHITESPACE = " ";
    public static final String JWT_BEARER = "Bearer";
    public static final int TOKEN_EXPIRY_SECONDS = 3600;
    public static final String HASH_ALGORITHM = "SHA3-256";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&()_+={[}]<>])(?=\\S+$).{8,}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String PHONE_REGEX = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
    public static final int ENTRY_MIN_YEAR = 2020;
    public static final String MONTH_FORMAT = "MMMM";
    public static final String WEEKDAY_FORMAT = "EEEE";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String DAY_FORMAT = "d";
    public static final String DATE_FORMAT = "yyyy-M-d";
    public static final String USER_JOINED_DATE_FORMAT = "MMMM,yyyy";
}
