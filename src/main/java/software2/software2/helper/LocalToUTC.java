package software2.software2.helper;

import java.time.LocalDateTime;


/**
 * An Interface used in converting the user's local time to UTC
 */
public interface LocalToUTC {
    LocalDateTime convertToUTC(LocalDateTime local);
}
