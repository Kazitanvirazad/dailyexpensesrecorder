package net.expenses.recorder.service;

import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.ResponseDto;
import net.expenses.recorder.dto.UserDto;
import net.expenses.recorder.dto.UserLoginFormDto;
import net.expenses.recorder.dto.UserRegistrationFormDto;

/**
 * @author Kazi Tanvir Azad
 */
public interface UserService {
    User getUserByEmail(String email);

    User getUserByPhone(String phone);

    ResponseDto userLogin(UserLoginFormDto userLoginFormDto);

    boolean isUserExistsByEmailOrPhone(String email, String phone);

    boolean isUserExistsByEmail(String email);

    boolean isUserExistsByPhone(String phone);

    ResponseDto submitUserRegistration(UserRegistrationFormDto userRegistrationFormDto);

    ResponseDto userLogout();

    UserDto getUserDetail();
}
