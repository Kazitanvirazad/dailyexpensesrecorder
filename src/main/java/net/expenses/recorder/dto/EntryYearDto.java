package net.expenses.recorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kazi Tanvir Azad
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EntryYearDto {
    private String year;
    private Integer itemCount;
    private Integer entryCount;
}
