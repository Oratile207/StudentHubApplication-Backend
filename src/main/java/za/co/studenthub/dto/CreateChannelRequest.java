package za.co.studenthub.dto;

import lombok.Data;
import za.co.studenthub.domain.enums.ChannelType;

@Data
public class CreateChannelRequest {
    private String channelName;
    private String name;  // Alternative field name for frontend compatibility
    private ChannelType channelType;
    private String description;
    private Boolean isPrivate;  // Frontend format
    
    // Utility methods
    public String getChannelNameField() {
        return channelName != null ? channelName : name;
    }
    
    public ChannelType getChannelTypeField() {
        if (channelType != null) {
            return channelType;
        }
        if (isPrivate != null) {
            return isPrivate ? ChannelType.PRIVATE_GROUP : ChannelType.PUBLIC_FORUM;
        }
        return ChannelType.PUBLIC_FORUM; // Default
    }
}
