package pe.com.nttdata.sgbankingmovements.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private static final String DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private DateUtils(){}

    public static String localDateTimeToString(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    public static String getCurrentDateTimeAsString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }


}
