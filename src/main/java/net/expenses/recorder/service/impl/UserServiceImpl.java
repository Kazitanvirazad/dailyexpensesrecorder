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
import net.expenses.recorder.exception.BadCredentialException;
import net.expenses.recorder.exception.InvalidInputException;
import net.expenses.recorder.exception.ServerErrorException;
import net.expenses.recorder.exception.UserLogoutException;
import net.expenses.recorder.exception.UserRegistrationException;
import net.expenses.recorder.repository.UserRepository;
import net.expenses.recorder.security.JWTManager;
import net.expenses.recorder.service.AvatarService;
import net.expenses.recorder.service.UserService;
import net.expenses.recorder.utils.CommonConstants;
import net.expenses.recorder.validation.UserValidationHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

/**
 * @author Kazi Tanvir Azad
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, CommonConstants {
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
            String hashedPassword = createPasswordHash(userLoginFormDto.getPassword());
            User user = getUserByEmail(userLoginFormDto.getEmail());
            if (user == null) {
                throw new InvalidInputException("User does not exists.");
            }
            if (!user.getHashedPassword().equals(hashedPassword)) {
                throw new BadCredentialException("Password mismatch");
            }
            if (user.getLoggedOut()) {
                user.setLoggedOut(false);
                userRepository.save(user);
            }
            String token = jwtManager.generateToken(user);
            return new JwtTokenDto(JWT_BEARER, TOKEN_EXPIRY_SECONDS, token);
        } catch (NoSuchAlgorithmException exception) {
            log.error(exception.getMessage());
            throw new ServerErrorException("Something went wrong.");
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

        if (isUserExistsByEmailOrPhone(userRegistrationFormDto.getEmail(), userRegistrationFormDto.getPhone())) {
            throw new UserRegistrationException("User Registration Failed. User already exists with same email or phone.");
        }
        User user = new User();
        user.setEmail(userRegistrationFormDto.getEmail());
        user.setFirstName(userRegistrationFormDto.getFirstName());
        user.setLastName(userRegistrationFormDto.getLastName());
        String hashedPassword;
        try {
            hashedPassword = createPasswordHash(userRegistrationFormDto.getPassword());
        } catch (NoSuchAlgorithmException exception) {
            log.error(exception.getMessage());
            throw new ServerErrorException("Something went wrong.");
        }
        user.setHashedPassword(hashedPassword);
        user.setPhone(userRegistrationFormDto.getPhone());
        user.setDateCreated(Timestamp.from(Instant.now()));
        user.setLoggedOut(false);
        if (StringUtils.hasText(userRegistrationFormDto.getBio())) {
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
        user.setLoggedOut(true);
        userRepository.save(user);
        return APIResponseDto.builder()
                .setMessage("User is logged out!")
                .build();
    }

    @Override
    public UserDto getUserDetail() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Avatar avatar = user.getAvatar();
        Timestamp dateCreated = user.getDateCreated();
        DateFormat dateFormat = new SimpleDateFormat("MMMM,yyyy");
        String formattedDate = dateFormat.format(dateCreated);
        return new UserDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), formattedDate, user.getBio(), user.getEntryCount(), avatar != null ? avatar.getAvatarEncodedImage() : null);
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
}
