package net.expenses.recorder.validation;

import net.expenses.recorder.dto.UserLoginFormDto;
import net.expenses.recorder.dto.UserRegistrationFormDto;
import net.expenses.recorder.exception.InvalidInputException;
import net.expenses.recorder.exception.UserRegistrationException;
import net.expenses.recorder.utils.CommonConstants;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kazi Tanvir Azad
 */
public class UserValidationHelper implements CommonConstants {
    public static boolean isValidPassword(String password) {
        return StringUtils.hasText(password) &&
                !StringUtils.containsWhitespace(password) &&
                password.length() >= 8;
    }

    public static boolean isValidPasswordWithPattern(String password) {
        if (StringUtils.hasText(password) && !StringUtils.containsWhitespace(password)) {
            Pattern pattern = Pattern.compile(PASSWORD_REGEX);
            Matcher matcher = pattern.matcher(password);
            return matcher.matches();
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        if (StringUtils.hasText(email) && !StringUtils.containsWhitespace(email)) {
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (StringUtils.hasText(phone) && !StringUtils.containsWhitespace(phone)) {
            Pattern pattern = Pattern.compile(PHONE_REGEX);
            Matcher matcher = pattern.matcher(phone);
            return matcher.matches();
        }
        return false;
    }

    public static boolean isValidFirstAndLastName(String name) {
        return StringUtils.hasText(name) && name.length() <= 100 && name.length() > 2;
    }

    public static void validateUserRegistrationForm(UserRegistrationFormDto userRegistrationForm) {
        if (userRegistrationForm == null)
            throw new UserRegistrationException("Invalid or empty form!");
        if (!isValidEmail(userRegistrationForm.getEmail()))
            throw new UserRegistrationException("Invalid Email!");
        if (!isValidPasswordWithPattern(userRegistrationForm.getPassword()))
            throw new UserRegistrationException("Password must contain at least 8 characters, at least one number, " +
                    "at least one uppercase and one lowercase character, " +
                    "no white space and at least one of the following special character ~!@#$%^&()_+={[}]<>");
        if (!isValidPhoneNumber(userRegistrationForm.getPhone()))
            throw new UserRegistrationException("Invalid Phone number!");
        if (!isValidFirstAndLastName(userRegistrationForm.getFirstName()))
            throw new UserRegistrationException("Invalid First Name!");
        if (!isValidFirstAndLastName(userRegistrationForm.getLastName()))
            throw new UserRegistrationException("Invalid Last Name!");
    }

    public static void validateUserLoginForm(UserLoginFormDto userLoginForm) {
        if (userLoginForm == null)
            throw new InvalidInputException("Invalid or empty form!");
        if (!isValidEmail(userLoginForm.getEmail()))
            throw new InvalidInputException("Invalid/Malformed Email!");
        if (!isValidPassword(userLoginForm.getPassword()))
            throw new InvalidInputException("Invalid password!");
    }
}
