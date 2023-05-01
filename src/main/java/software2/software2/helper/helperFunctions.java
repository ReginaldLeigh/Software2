package software2.software2.helper;
import java.util.Locale;
import java.util.ResourceBundle;

public class helperFunctions {

    public static Locale getLocale() {
        return Locale.getDefault();
    }


    public static ResourceBundle getResourceBundle() {
        Locale locale = helperFunctions.getLocale();
        return ResourceBundle.getBundle("/software2/software2/language", Locale.forLanguageTag(locale.getLanguage()));
    }
}
