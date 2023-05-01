package software2.software2.helper;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A class of functions used to retrieve ResourceBundles
 */
public class helperFunctions {
    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("/software2/software2/language", Locale.forLanguageTag(Locale.getDefault().getLanguage()));
    }
}
