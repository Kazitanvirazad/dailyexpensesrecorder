package net.expenses.recorder.controller;

import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dto.APIResponseDto;
import net.expenses.recorder.dto.ResponseDto;
import net.expenses.recorder.dto.UserDto;
import net.expenses.recorder.dto.UserLoginFormDto;
import net.expenses.recorder.dto.UserRegistrationFormDto;
import net.expenses.recorder.dto.UserUpdateFormDto;
import net.expenses.recorder.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.expenses.recorder.constants.ApiConstants.DETAIL_API;
import static net.expenses.recorder.constants.ApiConstants.LOGIN_API;
import static net.expenses.recorder.constants.ApiConstants.LOGOUT_API;
import static net.expenses.recorder.constants.ApiConstants.REGISTER_API;
import static net.expenses.recorder.constants.ApiConstants.UPDATE_API;
import static net.expenses.recorder.constants.ApiConstants.USER_API;

/**
 * @author Kazi Tanvir Azad
 */
@RestController
@RequestMapping(path = USER_API)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(path = REGISTER_API, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> submitUserRegistration(@RequestBody UserRegistrationFormDto userRegistrationFormDto) {
        ResponseDto responseDto = userService.submitUserRegistration(userRegistrationFormDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping(path = LOGIN_API, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> loginUser(@RequestBody UserLoginFormDto userLoginFormDto) {
        ResponseDto responseDto = userService.userLogin(userLoginFormDto);
        return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
    }

    @PostMapping(path = LOGOUT_API, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> logoutUser() {
        ResponseDto responseDto = userService.userLogout();
        return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
    }

    @GetMapping(path = DETAIL_API, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> getUser() {
        UserDto userDto = userService.getUserDetail();
        return ResponseEntity.ok(APIResponseDto.builder()
                .setData(userDto)
                .build());
    }

    @PutMapping(path = UPDATE_API, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> updateUser(@RequestBody UserUpdateFormDto userUpdateFormDto) {
        return new ResponseEntity<>(userService.updateUser(userUpdateFormDto), HttpStatus.ACCEPTED);
    }
}
