package net.expenses.recorder.controller;

import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dto.ResponseDto;
import net.expenses.recorder.dto.UserRegistrationFormDto;
import net.expenses.recorder.service.UserService;
import net.expenses.recorder.utils.CommonApiConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kazi Tanvir Azad
 */
@RestController
@RequestMapping(path = CommonApiConstants.USER_API)
@RequiredArgsConstructor
public class UserController implements CommonApiConstants {
    private final UserService userService;

    @PostMapping(path = REGISTER_API, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> submitUserRegistration(@RequestBody UserRegistrationFormDto userRegistrationFormDto) {
        ResponseDto responseDto = userService.submitUserRegistration(userRegistrationFormDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
