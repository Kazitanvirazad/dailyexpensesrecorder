package net.expenses.recorder.service.impl;

import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dao.Avatar;
import net.expenses.recorder.repository.AvatarRepository;
import net.expenses.recorder.service.AvatarService;
import org.springframework.stereotype.Service;

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
        Optional<Avatar> optionalAvatar = avatarRepository.getDefaultAvatar();
        return optionalAvatar.orElse(null);
    }
}
