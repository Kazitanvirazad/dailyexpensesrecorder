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
public final class EntryModifyFormDto implements RequestDto, Serializable {

    @Serial
    private static final long serialVersionUID = -6525264093073202847L;
    private String entryName;
    private String entry_id;
    private Integer day;
    private String description;
    private Integer year;
    private String month;
}
