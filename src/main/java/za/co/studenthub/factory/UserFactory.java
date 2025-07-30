package za.co.studenthub.factory;


import za.co.studenthub.domain.*;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.util.Helper;

public class UserFactory {
    public static User createUser(UserRole role, String firstName, String lastName, String email, String password, String studentNumber, String staffNumber) {
        if (role == null || Helper.isNullOrEmpty(firstName) || Helper.isNullOrEmpty(lastName) || Helper.isNullOrEmpty(email) || Helper.isNullOrEmpty(password)) {
            return null;
        }

        Long id = Helper.generateId();

        switch (role) {
            case STUDENT:
                return Students.builder()
                        .userId(id)
                        .userFirstName(firstName)
                        .userLastName(lastName)
                        .userEmail(email)
                        .userPassword(password)
                        .userRole(role)
                        .studentNumber(studentNumber)
                        .build();
            case ADMIN:
                return Admin.builder()
                        .userId(id)
                        .userFirstName(firstName)
                        .userLastName(lastName)
                        .userEmail(email)
                        .userPassword(password)
                        .userRole(role)
                        .staffNumber(staffNumber)
                        .build();
            case FACULTY_MEMBER:
                return FacultyMembers.builder()
                        .userId(id)
                        .userFirstName(firstName)
                        .userLastName(lastName)
                        .userEmail(email)
                        .userPassword(password)
                        .userRole(role)
                        .build();
            case IT_SUPPORT_STAFF:
                return ITSupportStaff.builder()
                        .userId(id)
                        .userFirstName(firstName)
                        .userLastName(lastName)
                        .userEmail(email)
                        .userPassword(password)
                        .userRole(role)
                        .build();
            case GUEST:
                return GuestProfile.builder()
                        .userId(id)
                        .userFirstName(firstName)
                        .userLastName(lastName)
                        .userEmail(email)
                        .userPassword(password)
                        .userRole(role)
                        .build();
            default:
                return null;
        }
    }

}
