package net.expenses.recorder.controller;

import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dto.APIResponseDto;
import net.expenses.recorder.dto.ResponseDto;
import net.expenses.recorder.service.AvatarService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.expenses.recorder.constants.ApiConstants.AVATAR_API;
import static net.expenses.recorder.constants.ApiConstants.FETCH_ALL_API;
import static net.expenses.recorder.constants.ApiConstants.FETCH_API;

/**
 * @author Kazi Tanvir Azad
 */
@RestController
@RequestMapping(path = AVATAR_API)
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarService avatarService;

    @GetMapping(path = FETCH_ALL_API, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> fetchAllAvatar() {
        return ResponseEntity.ok(APIResponseDto.builder()
                .setData(avatarService.getAvatarList())
                .build());
    }

    @GetMapping(path = FETCH_API, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> fetchAvatar() {
        return ResponseEntity.ok(APIResponseDto.builder()
                .setData(avatarService.fetchUserAvatar())
                .build());
    }
}
