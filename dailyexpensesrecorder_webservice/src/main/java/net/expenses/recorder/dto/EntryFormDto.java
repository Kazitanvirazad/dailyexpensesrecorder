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
public final class EntryFormDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5441026627024856288L;
    private String entryName;
    private String description;
    private Integer year;
    private String month;
    private Integer day;
}