package net.expenses.recorder.validation;

import net.expenses.recorder.dto.EntryFormDto;
import net.expenses.recorder.exception.InvalidInputException;
import org.springframework.util.StringUtils;

/**
 * @author Kazi Tanvir Azad
 */
public class EntryValidation {

    public static void validateEntryForm(EntryFormDto entryForm) {
        if (entryForm == null)
            throw new InvalidInputException("Invalid or empty form!");
        if (entryForm.getYear() != null && !(entryForm.getYear() >= 1950 && entryForm.getYear() <= 2099))
            throw new InvalidInputException("Year selection must be from 1950 to 2099");
        if (!StringUtils.hasText(entryForm.getMonth().trim()) || !(entryForm.getMonth().trim().length() >= 3)) {
            throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month. Ex: January -> Jan");
        }
    }
}
