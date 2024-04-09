package net.expenses.recorder.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.expenses.recorder.auth.JWTManager;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.JwtTokenDto;
import net.expenses.recorder.dto.ResponseDto;
import net.expenses.recorder.dto.UserLoginFormDto;
import net.expenses.recorder.dto.UserRegistrationFormDto;
import net.expenses.recorder.exception.BadCredentialException;
import net.expenses.recorder.exception.InvalidInputException;
import net.expenses.recorder.exception.ServerErrorException;
import net.expenses.recorder.exception.UserRegistrationException;
import net.expenses.recorder.repository.UserRepository;
import net.expenses.recorder.service.UserService;
import net.expenses.recorder.utils.CommonConstants;
import net.expenses.recorder.validation.UserValidationHelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

/**
 * @author Kazi Tanvir Azad
 */
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, CommonConstants {
    private final UserRepository userRepository;
    private final JWTManager jwtManager;

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
            userRepository.save(user);

            String token = jwtManager.generateToken(user);
            return new JwtTokenDto(JWT_BEARER, TOKEN_EXPIRY_SECONDS, token);
        }
        throw new UserRegistrationException("User Registration Failed. User already exists with same email or phone.");
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