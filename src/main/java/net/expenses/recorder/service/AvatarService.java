package net.expenses.recorder.service;

import net.expenses.recorder.dao.Avatar;
import net.expenses.recorder.dto.AvatarDto;

import java.util.List;

/**
 * @author Kazi Tanvir Azad
 */
public interface AvatarService {
    Avatar getDefaultAvatar();

    List<AvatarDto> getAvatarList();
}
