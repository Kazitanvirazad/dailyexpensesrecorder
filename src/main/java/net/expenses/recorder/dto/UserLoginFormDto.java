package net.expenses.recorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Kazi Tanvir Azad
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public final class UserLoginFormDto implements RequestDto, Serializable {
    @Serial
    private static final long serialVersionUID = -6485354785780808525L;
    private String email;
    private String password;
}
