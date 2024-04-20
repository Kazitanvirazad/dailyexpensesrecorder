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
    private static final long serialVersionUID = 5552272657420147131L;
}
