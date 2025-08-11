package za.co.studenthub.util;

import java.util.UUID;

public class Helper {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static Long generateId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}