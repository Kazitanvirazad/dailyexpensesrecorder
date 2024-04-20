package net.expenses.recorder.controller;

import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dto.APIResponseDto;
import net.expenses.recorder.dto.ResponseDto;
import net.expenses.recorder.service.AvatarService;
import net.expenses.recorder.utils.CommonApiConstants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kazi Tanvir Azad
 */
@RestController
@RequestMapping(path = CommonApiConstants.AVATAR_API)
@RequiredArgsConstructor
public class AvatarController implements CommonApiConstants {
    private final AvatarService avatarService;

    @GetMapping(path = FETCH_ALL_API, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> fetchAllAvatar() {
        return ResponseEntity.ok(APIResponseDto.builder()
                .setData(avatarService.getAvatarList())
                .build());
    }
}
