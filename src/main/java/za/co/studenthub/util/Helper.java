package za.co.studenthub.util;
import java.util.UUID;

public class Helper {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static Long generateId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
/*
    public static User validateUser(User user) {
        UserRole userRole = user.getUserRole();
        switch (userRole) {
            case STUDENT:
                user.studentNumber(studentNumber);
                break;
            case ADMIN:
            case IT_SUPPORT_STAFF:
                user.staffNumber(staffNumber);
                break;
            case FACULTY_MEMBER:
            case GUEST:
                break;
            case ENTREPRENEUR:
                EntrepreneurUserProfile entrepreneurProfile = EntrepreneurUserProfile.builder()
                        .isCommercePortfolioEnabled(isCommercePortfolioEnabled)
                        .sessionUrl(sessionUrl)
                        .biography(biography)
                        .build();
                user.entrepreneurProfile(entrepreneurProfile);
                break;
        }
        return user.build();
    }

 */
}