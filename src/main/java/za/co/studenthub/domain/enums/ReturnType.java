package za.co.studenthub.domain.enums;

public enum ReturnType {
    FULL_REFUND("💰 Money Back Guarantee"),
    EXCHANGE_ONLY("🔄 Swap It Out"),
    STORE_CREDIT("💳 Future Fun Money"),
    NO_RETURN("🚫 All Sales Final!"),
    LIMITED_TIME_RETURN("⏳ Quick Return Window");

    private final String description;

    ReturnType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}