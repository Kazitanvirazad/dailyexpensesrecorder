package net.expenses.recorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Kazi Tanvir Azad
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EntryDto {
    private String entryId;
    private Date creationTime;
    private String month;
    private String year;
    private Double totalAmount;
    private String desc;
    private Date lastModified;
}
