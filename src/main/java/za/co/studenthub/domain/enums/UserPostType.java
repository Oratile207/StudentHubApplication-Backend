package za.co.studenthub.domain.enums;
public enum UserPostType {
    TEXT_POST("💬 Just Words"),
    IMAGE_SHARE("🖼️ Look at This!"),
    VIDEO_CLIP("🎬 Watch This!"),
    DOCUMENT_ATTACHMENT("📎 Read This File"),
    POLL_QUESTION("📊 Cast Your Vote!"),
    EVENT_REMINDER("🗓️ Don't Forget!");

    private final String description;

    UserPostType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}