package software2.software2.helper;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class helperFunctions {

    public static ZoneId getSystemZoneId() {
        ZoneId zoneId = ZoneId.systemDefault();
        return zoneId;
    }

    public static LocalDateTime convertToEST(LocalDateTime local) {
        ZonedDateTime zonedLocal = local.atZone(ZoneId.systemDefault());
        LocalDateTime timeEst = zonedLocal.withZoneSameInstant(ZoneId.of("America/New_York")).toLocalDateTime();
        return timeEst ;
    }

    public static LocalDateTime convertToUTC(LocalDateTime local) {
        ZonedDateTime zonedLocal = local.atZone(ZoneId.systemDefault());
        LocalDateTime timeUTC = zonedLocal.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return timeUTC;
    }

    public static Locale getLocale() {
        return Locale.getDefault();
    }

    public static String getLocalLanguage() {
        return getLocale().getLanguage();
    }

    public static String getLocalCountry() {
        return getLocale().getCountry();
    }

    public static ResourceBundle getResourceBundle() {
        Locale locale = helperFunctions.getLocale();
        ResourceBundle labels = ResourceBundle.getBundle("/software2/software2/language", Locale.forLanguageTag(locale.getLanguage()));
        return labels;
    }


}
