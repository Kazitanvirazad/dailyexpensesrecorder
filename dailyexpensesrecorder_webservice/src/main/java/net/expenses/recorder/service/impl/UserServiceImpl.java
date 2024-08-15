package net.expenses.recorder.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.expenses.recorder.dao.Avatar;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.APIResponseDto;
import net.expenses.recorder.dto.JwtTokenDto;
import net.expenses.recorder.dto.ResponseDto;
import net.expenses.recorder.dto.UserDto;
import net.expenses.recorder.dto.UserLoginFormDto;
import net.expenses.recorder.dto.UserRegistrationFormDto;
import net.expenses.recorder.dto.UserUpdateFormDto;
import net.expenses.recorder.exception.BadCredentialException;
import net.expenses.recorder.exception.InvalidInputException;
import net.expenses.recorder.exception.ServerErrorException;
import net.expenses.recorder.exception.UserLogoutException;
import net.expenses.recorder.exception.UserRegistrationException;
import net.expenses.recorder.repository.UserRepository;
import net.expenses.recorder.security.JWTManager;
import net.expenses.recorder.service.AvatarService;
import net.expenses.recorder.service.UserService;
import net.expenses.recorder.validation.UserValidationHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Optional;

import static net.expenses.recorder.constants.CommonConstants.HASH_ALGORITHM;
import static net.expenses.recorder.constants.CommonConstants.JWT_BEARER;
import static net.expenses.recorder.constants.CommonConstants.TOKEN_EXPIRY_SECONDS;
import static net.expenses.recorder.constants.CommonConstants.USER_JOINED_DATE_FORMAT;
import static net.expenses.recorder.constants.StaticTextResource.DEFAULT_BIO;
import static net.expenses.recorder.constants.StaticTextResource.SERVER_ERROR_GENERIC_MESSAGE;
import static net.expenses.recorder.utils.CommonUtils.getCurrentTimeStamp;
import static net.expenses.recorder.utils.CommonUtils.getFormattedDate;

/**
 * @author Kazi Tanvir Azad
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JWTManager jwtManager;
    private final AvatarService avatarService;

    @Override
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.getReferenceByEmail(email);
        return optionalUser.orElse(null);
    }

    @Override
    public User getUserByPhone(String phone) {
        Optional<User> optionalUser = userRepository.getReferenceByPhone(phone);
        return optionalUser.orElse(null);
    }

    @Transactional
    @Override
    public ResponseDto userLogin(UserLoginFormDto userLoginFormDto) {
        // Validates Login form
        UserValidationHelper.validateUserLoginForm(userLoginFormDto);

        try {
            User user = getUserByEmail(userLoginFormDto.getEmail().trim());
            if (user == null) {
                throw new InvalidInputException("User does not exists.");
            }
            String hashedPassword = createPasswordHash(userLoginFormDto.getPassword().trim());
            if (!user.getHashedPassword().equals(hashedPassword)) {
                throw new BadCredentialException("Password mismatch");
            }
            if (user.getLoggedOut()) {
                user.setLoggedOut(false);
                userRepository.setLoggedOutFalse(user.getUserId());
            }
            String token = jwtManager.generateToken(user);
            return new JwtTokenDto(JWT_BEARER, TOKEN_EXPIRY_SECONDS, token);
        } catch (NoSuchAlgorithmException exception) {
            log.error(exception.getMessage());
            throw new ServerErrorException(SERVER_ERROR_GENERIC_MESSAGE);
        }
    }

    @Override
    public boolean isUserExistsByEmailOrPhone(String email, String phone) {
        Optional<User> optionalUser = userRepository.getReferenceByEmailOrPhone(email, phone);
        return optionalUser.isPresent();
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        return getUserByEmail(email) != null;
    }

    @Override
    public boolean isUserExistsByPhone(String phone) {
        return getUserByPhone(phone) != null;
    }

    @Transactional
    @Override
    public ResponseDto submitUserRegistration(UserRegistrationFormDto userRegistrationFormDto) {
        // Validates Registration form
        UserValidationHelper.validateUserRegistrationForm(userRegistrationFormDto);

        if (isUserExistsByEmailOrPhone(userRegistrationFormDto.getEmail().trim(), userRegistrationFormDto.getPhone().trim())) {
            throw new UserRegistrationException("User Registration Failed. User already exists with same email or phone.");
        }
        User user = new User();
        user.setEmail(userRegistrationFormDto.getEmail().trim());
        user.setFirstName(userRegistrationFormDto.getFirstName().trim());
        user.setLastName(userRegistrationFormDto.getLastName().trim());
        String hashedPassword;
        try {
            hashedPassword = createPasswordHash(userRegistrationFormDto.getPassword().trim());
        } catch (NoSuchAlgorithmException exception) {
            log.error(exception.getMessage());
            throw new ServerErrorException(SERVER_ERROR_GENERIC_MESSAGE);
        }
        user.setHashedPassword(hashedPassword);
        user.setPhone(userRegistrationFormDto.getPhone().trim());

        Timestamp currentTimeStamp = getCurrentTimeStamp();

        user.setDateCreated(currentTimeStamp);
        user.setEntryCount(0);
        user.setLoggedOut(false);
        if (StringUtils.hasText(userRegistrationFormDto.getBio().trim())) {
            user.setBio(userRegistrationFormDto.getBio());
        } else {
            user.setBio(DEFAULT_BIO);
        }

        Avatar avatar = avatarService.getDefaultAvatar();
        user.setAvatar(avatar);
        userRepository.save(user);

        String token = jwtManager.generateToken(user);
        return new JwtTokenDto(JWT_BEARER, TOKEN_EXPIRY_SECONDS, token);
    }

    @Transactional
    @Override
    public ResponseDto userLogout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getLoggedOut()) {
            throw new UserLogoutException("User is already logged out!");
        }
        userRepository.setLoggedOutTrue(user.getUserId());
        return APIResponseDto.builder()
                .setMessage("User is logged out!")
                .build();
    }

    @Override
    public UserDto getUserDetail() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Avatar avatar = user.getAvatar();
        String formattedDate = getFormattedDate(user.getDateCreated(), USER_JOINED_DATE_FORMAT);
        return new UserDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(),
                formattedDate, user.getBio(), user.getEntryCount(), avatar != null ?
                avatar.getAvatarEncodedImage() : null);
    }

    @Transactional
    @Override
    public void incrementEntry(User user) {
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        int incrementedEntryCount = user.getEntryCount() + 1;
        userRepository.incrementEntry(incrementedEntryCount, user.getUserId());
    }

    @Transactional
    @Override
    public ResponseDto updateUser(UserUpdateFormDto userUpdateFormDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserValidationHelper.validateUserUpdateForm(userUpdateFormDto);
        updateUser(user, userUpdateFormDto);
        userRepository.save(user);
        return APIResponseDto.builder()
                .setMessage("User updated successfully!")
                .build();
    }

    private String createPasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] hashedPassword = messageDigest.digest();
        StringBuilder hashedString = new StringBuilder();
        for (byte b : hashedPassword) {
            hashedString.append(String.format("%02x", b));
        }
        return hashedString.toString();
    }

    private void updateUser(User user, UserUpdateFormDto userUpdateFormDto) {
        if (user != null && userUpdateFormDto != null) {
            if (userUpdateFormDto.getFirstName() != null &&
                    !userUpdateFormDto.getFirstName().trim().equals(user.getFirstName())) {
                user.setFirstName(userUpdateFormDto.getFirstName().trim());
            }
            if (userUpdateFormDto.getLastName() != null &&
                    !userUpdateFormDto.getLastName().trim().equals(user.getLastName())) {
                user.setLastName(userUpdateFormDto.getLastName().trim());
            }
            if (userUpdateFormDto.getBio() != null &&
                    !userUpdateFormDto.getBio().trim().equals(user.getBio())) {
                user.setBio(userUpdateFormDto.getBio().trim());
            }


            if (userUpdateFormDto.getAvatarId() != null &&
                    !user.getAvatar().getAvatarId().equals(userUpdateFormDto.getAvatarId())) {
                Avatar avatar = avatarService.getAvatarById(userUpdateFormDto.getAvatarId());
                user.setAvatar(avatar);
            }
            if (userUpdateFormDto.getPhone() != null
                    && !user.getPhone().equals(userUpdateFormDto.getPhone().trim())) {
                if (isUserExistsByPhone(userUpdateFormDto.getPhone().trim())) {
                    throw new InvalidInputException("Phone number is not available. Try another number.");
                }
                user.setPhone(userUpdateFormDto.getPhone());
            }
        }
    }
}
