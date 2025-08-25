package za.co.studenthub.util;

import za.co.studenthub.domain.Message;

import java.util.HashMap;
import java.util.Map;

public class MessageResponseFormatter {
    
    public static Map<String, Object> formatMessageForFrontend(Message message) {
        if (message == null) {
            return null;
        }
        
        Map<String, Object> messageInfo = new HashMap<>();
        messageInfo.put("id", message.getId());
        messageInfo.put("content", message.getContent());
        messageInfo.put("channelId", message.getChannel().getChannelId());
        messageInfo.put("timestamp", message.getTimestamp().toString());
        messageInfo.put("isEdited", message.isEdited());
        messageInfo.put("editedAt", message.getEditedAt() != null ? message.getEditedAt().toString() : null);
        
        // Embedded author details
        if (message.getAuthor() != null) {
            Map<String, Object> authorInfo = new HashMap<>();
            authorInfo.put("id", message.getAuthor().getId());
            authorInfo.put("username", message.getAuthor().getUserEmail());
            authorInfo.put("displayName", 
                message.getAuthor().getUserFirstName() + " " + message.getAuthor().getUserLastName());
            authorInfo.put("avatar", 
                message.getAuthor().getAvatar() != null ? message.getAuthor().getAvatar() : 
                "https://ui-avatars.com/api/?name=" + message.getAuthor().getUserFirstName() + "&background=7289da&color=fff");
            authorInfo.put("isOnline", message.getAuthor().isOnline());
            messageInfo.put("author", authorInfo);
        }
        
        return messageInfo;
    }
}
