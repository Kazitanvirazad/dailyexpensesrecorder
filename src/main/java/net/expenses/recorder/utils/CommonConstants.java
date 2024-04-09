package net.expenses.recorder.utils;

/**
 * @author Kazi Tanvir Azad
 */
public interface CommonConstants {
    String SINGLE_WHITESPACE = " ";
    String JWT_BEARER = "Bearer";
    int TOKEN_EXPIRY_SECONDS = 3600;
    String HASH_ALGORITHM = "SHA3-256";
    String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&()_+={[}]<>])(?=\\S+$).{8,}$";
    String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    String PHONE_REGEX = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
}
