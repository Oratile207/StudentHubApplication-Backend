package za.co.studenthub.factory;

import za.co.studenthub.domain.EntrepreneurUserProfile;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.util.Helper;

public class UserFactory {
    public static User createUser(UserRole role, String firstName, String lastName, String email, String password,
                                  String studentNumber, String staffNumber, String biography,
                                  boolean isCommercePortfolioEnabled, String sessionUrl) {
        if (role == null || Helper.isNullOrEmpty(firstName) || Helper.isNullOrEmpty(lastName) ||
                Helper.isNullOrEmpty(email) || Helper.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("Role, firstName, lastName, email, and password are required");
        }

        User.UserBuilder builder = User.builder()
                .userFirstName(firstName)
                .userLastName(lastName)
                .userEmail(email)
                .userPassword(password)
                .userRole(role);

        switch (role) {
            case STUDENT:
                builder.studentNumber(studentNumber);
                break;
            case ADMIN:
            case IT_SUPPORT_STAFF:
                builder.staffNumber(staffNumber);
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
                builder.entrepreneurProfile(entrepreneurProfile);
                break;
        }

        return builder.build();
    }
}
