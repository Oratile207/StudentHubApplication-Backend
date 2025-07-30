package za.co.studenthub.util;
import java.util.UUID;

public class Helper {

    /**
     * Checks if a given string is null or empty (after trimming whitespace).
     *
     * @param str The string to check.
     * @return {@code true} if the string is null or empty, {@code false} otherwise.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Generates a unique {@code Long} ID.
     * This implementation uses {@link UUID} to generate a unique value and returns the most significant bits.
     *
     * @return A unique {@code Long} identifier.
     */
    public static Long generateId() {
        // Generate a unique ID using UUID and return the most significant bits as a Long
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}