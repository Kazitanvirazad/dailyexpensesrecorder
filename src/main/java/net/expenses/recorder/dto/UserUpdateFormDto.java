package net.expenses.recorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Kazi Tanvir Azad
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserUpdateFormDto implements RequestDto, Serializable {
    @Serial
    private static final long serialVersionUID = 2980059226477989229L;
    private String firstName;
    private String lastName;
    private String bio;
    private Integer avatarId;
    private String phone;
}
