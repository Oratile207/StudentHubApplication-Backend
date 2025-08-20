package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import za.co.studenthub.dto.WebSocketMessage;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/typing")
    public void handleTyping(@Payload Map<String, Object> typingData) {
        Long channelId = Long.valueOf(typingData.get("channelId").toString());
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", typingData.get("userId"));
        payload.put("userName", typingData.get("userName"));
        payload.put("channelId", channelId);
        payload.put("isTyping", typingData.get("isTyping"));
        
        WebSocketMessage message = new WebSocketMessage("typing", payload);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
    }

    @MessageMapping("/user-joined")
    public void handleUserJoined(@Payload Map<String, Object> userData) {
        Long channelId = Long.valueOf(userData.get("channelId").toString());
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userData.get("userId"));
        payload.put("userName", userData.get("userName"));
        
        WebSocketMessage message = new WebSocketMessage("user_joined", payload);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
    }

    public void sendOnlineUsersUpdate(Long channelId, Object users) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("channelId", channelId);
        payload.put("users", users);
        
        WebSocketMessage message = new WebSocketMessage("online_users", payload);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
    }
}
