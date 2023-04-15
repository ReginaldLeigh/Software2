package software2.software2.helper;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class helperFunctions {

    public static ZoneId getSystemZoneId() {
        ZoneId zoneId = ZoneId.systemDefault();
        return zoneId;
    }
    public static DateFormat UTCtoLocal() {
        DateFormat utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return utc;
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
