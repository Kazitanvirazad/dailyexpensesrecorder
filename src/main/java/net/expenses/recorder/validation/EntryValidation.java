package net.expenses.recorder.validation;

import net.expenses.recorder.dto.EntryFormDto;
import net.expenses.recorder.dto.EntryModifyFormDto;
import net.expenses.recorder.exception.InvalidInputException;
import net.expenses.recorder.utils.CommonConstants;
import org.springframework.util.StringUtils;

/**
 * @author Kazi Tanvir Azad
 */
public class EntryValidation implements CommonConstants {

    public static void validateEntryForm(EntryFormDto entryForm) {
        if (entryForm == null)
            throw new InvalidInputException("Invalid or empty form!");
        if (entryForm.getYear() == null || !(entryForm.getYear() >= ENTRY_MIN_YEAR && entryForm.getYear() <= ENTRY_MAX_YEAR))
            throw new InvalidInputException("Year selection must be from " + ENTRY_MIN_YEAR + " to " + ENTRY_MAX_YEAR);
        if (!StringUtils.hasText(entryForm.getMonth().trim()) || !(entryForm.getMonth().trim().length() >= 3)) {
            throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month. Ex: January -> Jan");
        }
    }

    public static void validateEntryModifyForm(EntryModifyFormDto entryModifyFormDto) {
        if (entryModifyFormDto == null)
            throw new InvalidInputException("Invalid or empty form!");
        if (!StringUtils.hasText(entryModifyFormDto.getEntry_id()))
            throw new InvalidInputException("Invalid entry selection.");
        if (entryModifyFormDto.getYear() == null ||
                !(entryModifyFormDto.getYear() >= ENTRY_MIN_YEAR && entryModifyFormDto.getYear() <= ENTRY_MAX_YEAR))
            throw new InvalidInputException("Year selection must be from " + ENTRY_MIN_YEAR + " to " + ENTRY_MAX_YEAR);
        if (!StringUtils.hasText(entryModifyFormDto.getMonth().trim()) || !(entryModifyFormDto.getMonth().trim().length() >= 3)) {
            throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month. Ex: January -> Jan");
        }
    }

    public static void validateEntryYear(String year) {
        try {
            int entryYear = Integer.parseInt(year);
            if (!(entryYear >= ENTRY_MIN_YEAR && entryYear <= ENTRY_MAX_YEAR)) {
                throw new InvalidInputException("Year selection must be from " + ENTRY_MIN_YEAR + " to " + ENTRY_MAX_YEAR);
            }
        } catch (NumberFormatException exception) {
            throw new InvalidInputException("Year selection must be a valid year from " + ENTRY_MIN_YEAR + " to " + ENTRY_MAX_YEAR);
        }
    }
}
