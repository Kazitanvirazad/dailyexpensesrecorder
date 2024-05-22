package net.expenses.recorder.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * @author Kazi Tanvir Azad
 */
public interface CommonUtils {
    default Timestamp getCurrentTimeStamp() {
        ZonedDateTime systemZonedDateTime = ZonedDateTime.of(LocalDateTime.now(), TimeZone.getDefault().toZoneId());
        return Timestamp.valueOf(systemZonedDateTime.toLocalDateTime());
    }

    default String getFormattedDate(Date date, String format) {
        DateFormat yearFormat = new SimpleDateFormat(format);
        return yearFormat.format(date);
    }
}
