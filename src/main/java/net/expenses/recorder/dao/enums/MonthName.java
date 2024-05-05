package net.expenses.recorder.dao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kazi Tanvir Azad
 */
@AllArgsConstructor
@Getter
public enum MonthName {
    JANUARY("JANUARY"),
    FEBRUARY("FEBRUARY"),
    MARCH("MARCH"),
    APRIL("APRIL"),
    MAY("MAY"),
    JUNE("JUNE"),
    JULY("JULY"),
    AUGUST("AUGUST"),
    SEPTEMBER("SEPTEMBER"),
    OCTOBER("OCTOBER"),
    NOVEMBER("NOVEMBER"),
    DECEMBER("DECEMBER"),
    NOT_SPECIFIED("NOT_SPECIFIED");

    private final String month;
}
