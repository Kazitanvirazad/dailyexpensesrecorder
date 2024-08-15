package net.expenses.recorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Kazi Tanvir Azad
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String joinedDate;
    private String bio;
    private Integer entryCount;
    private String avatarEncodedImage;
}
