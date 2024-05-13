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
public class EntryMonthDto {
    private String year;
    private String month;
    private Integer itemCount;
    private Integer entryCount;
}
