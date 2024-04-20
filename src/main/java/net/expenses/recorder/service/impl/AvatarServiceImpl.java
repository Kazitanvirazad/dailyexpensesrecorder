package net.expenses.recorder.service.impl;

import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dao.Avatar;
import net.expenses.recorder.dto.AvatarDto;
import net.expenses.recorder.repository.AvatarRepository;
import net.expenses.recorder.service.AvatarService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Kazi Tanvir Azad
 */
@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {
    private final AvatarRepository avatarRepository;

    @Override
    public Avatar getDefaultAvatar() {
        Optional<List<Avatar>> optionalAvatarList = avatarRepository.getDefaultAvatar();
        List<Avatar> avatarList = optionalAvatarList.orElse(null);
        return !CollectionUtils.isEmpty(avatarList) ? avatarList.getFirst() : null;
    }

    @Override
    public List<AvatarDto> getAvatarList() {
        List<AvatarDto> avatarDtoList = new ArrayList<>();
        Optional<List<Avatar>> optionalAvatarList = avatarRepository.getAvatarList();
        List<Avatar> avatarList = optionalAvatarList.orElse(null);
        if (!CollectionUtils.isEmpty(avatarList)) {
            avatarList.forEach(avatar -> avatarDtoList.add(new AvatarDto(avatar.getAvatarId(), avatar.getAvatarEncodedImage())));
        }
        return avatarDtoList;
    }
}
