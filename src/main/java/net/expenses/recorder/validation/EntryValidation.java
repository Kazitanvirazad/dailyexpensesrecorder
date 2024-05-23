package net.expenses.recorder.validation;

import lombok.extern.slf4j.Slf4j;
import net.expenses.recorder.dto.EntryFormDto;
import net.expenses.recorder.dto.EntryModifyFormDto;
import net.expenses.recorder.exception.InvalidInputException;
import net.expenses.recorder.utils.CommonConstants;
import net.expenses.recorder.utils.CommonUtils;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kazi Tanvir Azad
 */
@Slf4j
public class EntryValidation implements CommonConstants {

    public static void validateEntryForm(EntryFormDto entryForm) {
        if (entryForm == null)
            throw new InvalidInputException("Invalid or empty form!");
        if (!StringUtils.hasText(entryForm.getEntryName()))
            throw new InvalidInputException("Invalid Entry name input. Name must not be empty.");
        if (entryForm.getYear() == null || !(entryForm.getYear() >= ENTRY_MIN_YEAR && entryForm.getYear() <= CommonUtils.ENTRY_MAX_YEAR()))
            throw new InvalidInputException("Year selection must be from " + ENTRY_MIN_YEAR + " to " + CommonUtils.ENTRY_MAX_YEAR());
        if (!(isValidMonthName(entryForm.getMonth())))
            throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month. Ex: January -> Jan");
        if (!isValidDay(entryForm.getDay(), entryForm.getYear(), entryForm.getMonth()))
            throw new InvalidInputException("Invalid Date input. Date should be between 1st" +
                    " to last date of the selected month. Ex: For January -> 1 to 31");
        if (!isValidDateSelection(entryForm.getYear(), entryForm.getMonth(), entryForm.getDay()))
            throw new InvalidInputException("Entry date must be equal or before the current date.");
    }

    public static void validateEntryModifyForm(EntryModifyFormDto entryModifyFormDto) {
        if (entryModifyFormDto == null)
            throw new InvalidInputException("Invalid or empty form!");
        if (!StringUtils.hasText(entryModifyFormDto.getEntryName()))
            throw new InvalidInputException("Invalid Entry name input. Name must not be empty.");
        if (!StringUtils.hasText(entryModifyFormDto.getEntry_id()))
            throw new InvalidInputException("Invalid entry selection.");
        if (entryModifyFormDto.getYear() == null ||
                !(entryModifyFormDto.getYear() >= ENTRY_MIN_YEAR && entryModifyFormDto.getYear() <= CommonUtils.ENTRY_MAX_YEAR()))
            throw new InvalidInputException("Year selection must be from " + ENTRY_MIN_YEAR + " to " + CommonUtils.ENTRY_MAX_YEAR());
        if (!(isValidMonthName(entryModifyFormDto.getMonth())))
            throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month. Ex: January -> Jan");
        if (!isValidDay(entryModifyFormDto.getDay(), entryModifyFormDto.getYear(), entryModifyFormDto.getMonth()))
            throw new InvalidInputException("Invalid Date input. Date should be between 1st" +
                    " to last date of the selected month. Ex: For January -> 1 to 31");
        if (!isValidDateSelection(entryModifyFormDto.getYear(), entryModifyFormDto.getMonth(), entryModifyFormDto.getDay()))
            throw new InvalidInputException("Entry date must be equal or before the current date.");
    }

    public static void validateEntryYear(String year) {
        try {
            int entryYear = Integer.parseInt(year);
            if (!(entryYear >= ENTRY_MIN_YEAR && entryYear <= CommonUtils.ENTRY_MAX_YEAR())) {
                throw new InvalidInputException("Year selection must be from " + ENTRY_MIN_YEAR + " to " + CommonUtils.ENTRY_MAX_YEAR());
            }
        } catch (NumberFormatException exception) {
            throw new InvalidInputException("Year selection must be a valid year from " + ENTRY_MIN_YEAR + " to " + CommonUtils.ENTRY_MAX_YEAR());
        }
    }

    public static boolean isValidDateSelection(int year, String month, int day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        String inputDateString = year + "-" + getMonthIndex(month) + "-" + day;
        try {
            Date inputDate = simpleDateFormat.parse(inputDateString);
            return inputDate.equals(new Date()) || inputDate.before(new Date());
        } catch (ParseException exception) {
            log.error("Exception occurred while parsing input date due to malformed or invalid " +
                    "date input : {}", exception.getMessage());
            throw new InvalidInputException("Invalid date selection.", exception);
        }
    }

    public static boolean isValidDay(int day, int year, String month) {
        int monthLastDay = getMonthLastDate(year, getMonthIndex(month));
        if (monthLastDay < 1)
            throw new InvalidInputException("Invalid month name input. Month name should be at" +
                    " least first three letters of the actual month. Ex: January -> Jan");
        return day > 0 && day <= monthLastDay;
    }

    public static int getMonthLastDate(int year, int month) {
        if (month > 12 || month < 1) {
            return -1;
        }
        if (month == 2) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    return 29;
                } else {
                    return 28;
                }
            } else if (year % 4 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
        if (month < 8 && (month % 2 != 0)) {
            return 31;
        } else if (month > 7 && (month % 2 == 0)) {
            return 31;
        } else {
            return 30;
        }
    }

    public static int getMonthIndex(String monthName) {
        return switch (monthName.toLowerCase().substring(0, 3)) {
            case "jan" -> 1;
            case "feb" -> 2;
            case "mar" -> 3;
            case "apr" -> 4;
            case "may" -> 5;
            case "jun" -> 6;
            case "jul" -> 7;
            case "aug" -> 8;
            case "sep" -> 9;
            case "oct" -> 10;
            case "nov" -> 11;
            case "dec" -> 12;
            default -> 0;
        };
    }

    private static final Set<String> months = new HashSet<>() {
        @Serial
        private static final long serialVersionUID = -5136552895474621215L;

        {
            add("january");
            add("february");
            add("march");
            add("april");
            add("may");
            add("june");
            add("july");
            add("august");
            add("september");
            add("october");
            add("november");
            add("december");
            add("jan");
            add("feb");
            add("mar");
            add("apr");
            add("jun");
            add("jul");
            add("aug");
            add("sep");
            add("oct");
            add("nov");
            add("dec");
        }
    };

    public static boolean isValidMonthName(String monthName) {
        return StringUtils.hasText(monthName) && monthName.length() >= 3 && months.contains(monthName.toLowerCase());
    }
}
