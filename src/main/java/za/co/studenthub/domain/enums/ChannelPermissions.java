package za.co.studenthub.domain.enums;

public enum ChannelPermissions {
    ALL_MEMBERS_POST("All members can post"),
    MEMBERS_ONLY_POST("Only members can post"),
    MODERATED_POST("Posts require moderation"),
    ADMIN_ONLY_POST("Only admins can post"),
    VIEW_ONLY_GUEST("Guests can view"),
    NO_GUEST_VIEW("Guests cannot view"),
    VIEW_ONLY_STUDENT("Only students can view"),
    VIEW_ONLY_STUDENT_GUEST("Students and guests can view"),
    ALL_MEMBERS_VIEW("All members can view");

    private final String description;

    ChannelPermissions(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}