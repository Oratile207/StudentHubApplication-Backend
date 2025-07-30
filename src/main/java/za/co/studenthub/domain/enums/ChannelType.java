package za.co.studenthub.domain.enums;


public enum ChannelType {
    PUBLIC_FORUM("🗣️ Open Discussion", "ALL_MEMBERS_POST", "VIEW_ONLY_GUEST"),
    PRIVATE_GROUP("🤫 Secret Society", "MEMBERS_ONLY_POST", "NO_GUEST_VIEW"),
    ACADEMIC_HELP("🤓 Study Corner", "MODERATED_POST", "VIEW_ONLY_STUDENT"),
    SOCIAL_LOUNGE("🥳 Hangout Spot", "ALL_MEMBERS_POST", "VIEW_ONLY_STUDENT_GUEST"),
    EVENT_ANNOUNCEMENT("📢 What's Happening!", "ADMIN_ONLY_POST", "ALL_MEMBERS_VIEW");

    private final String description;
    private final String defaultPostPermission; // Example: "ALL_MEMBERS_POST", "ADMIN_ONLY_POST"
    private final String defaultViewPermission; // Example: "VIEW_ONLY_GUEST", "NO_GUEST_VIEW"

    ChannelType(String description, String defaultPostPermission, String defaultViewPermission) {
        this.description = description;
        this.defaultPostPermission = defaultPostPermission;
        this.defaultViewPermission = defaultViewPermission;
    }

    public String getDescription() {
        return description;
    }

    public String getDefaultPostPermission() {
        return defaultPostPermission;
    }

    public String getDefaultViewPermission() {
        return defaultViewPermission;
    }
}