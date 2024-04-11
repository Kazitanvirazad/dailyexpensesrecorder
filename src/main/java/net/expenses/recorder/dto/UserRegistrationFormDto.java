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
public class UserRegistrationFormDto implements RequestDto, Serializable {
    @Serial
    private static final long serialVersionUID = 7475042033491116377L;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String password;
}
