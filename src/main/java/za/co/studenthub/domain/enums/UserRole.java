package za.co.studenthub.domain.enums;

public enum UserRole {
    ADMIN("👑 The Overlord", "ADMIN_PROFILE", true, "STAFF"),
    STUDENT("📚 The Knowledge Seeker", "STUDENT_PROFILE", false, "STUDENT"),
    FACULTY_MEMBER("🧑‍🏫 The Wisdom Giver", "STUDENT_PROFILE", false, "FACULTY"),
    IT_SUPPORT_STAFF("🛠️ The Problem Solver", "STUDENT_PROFILE", true, "STAFF"),
    GUEST("🚶‍♂️ The Curious Visitor", "GUEST_PROFILE", false, null),
    ENTREPRENEUR("💡 The Idea Generator", "ENTREPRENEUR_PROFILE", false, "STUDENT");

    private final String description;
    private final String profileType;
    private final boolean isStaff;
    private final String numberType; // e.g., "STUDENT" or "STAFF" for number fields

    UserRole(String description, String profileType, boolean isStaff, String numberType) {
        this.description = description;
        this.profileType = profileType;
        this.isStaff = isStaff;
        this.numberType = numberType;
    }

    public String getDescription() { return description; }
    public String getProfileType() { return profileType; }
    public boolean isStaff() { return isStaff; }
    public String getNumberType() { return numberType; }
}