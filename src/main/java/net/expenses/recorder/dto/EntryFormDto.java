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
public final class EntryFormDto implements RequestDto, Serializable {
    @Serial
    private static final long serialVersionUID = -1804801581526861783L;
    private String description;
    private Integer year;
    private String month;
}
