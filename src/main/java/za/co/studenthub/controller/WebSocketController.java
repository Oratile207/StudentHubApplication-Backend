package za.co.studenthub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.Message;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.dto.OnlineUserDto;
import za.co.studenthub.dto.WebSocketMessage;
import za.co.studenthub.repository.ChannelMembershipRepository;
import za.co.studenthub.repository.UserRepository;
import za.co.studenthub.repository.MessageRepository;
import za.co.studenthub.repository.ChannelRepository;
import za.co.studenthub.services.WebSocketSessionService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final ChannelMembershipRepository membershipRepository;
    private final WebSocketSessionService sessionService;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate, 
                             UserRepository userRepository,
                             ChannelMembershipRepository membershipRepository,
                             WebSocketSessionService sessionService,
                             MessageRepository messageRepository,
                             ChannelRepository channelRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.sessionService = sessionService;
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
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

    @MessageMapping("/user-left")
    public void handleUserLeft(@Payload Map<String, Object> userData) {
        Long channelId = Long.valueOf(userData.get("channelId").toString());
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userData.get("userId"));
        payload.put("userName", userData.get("userName"));
        
        WebSocketMessage message = new WebSocketMessage("user_left", payload);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
    }

    @MessageMapping("/ping")
    public void handlePing(@Payload Map<String, Object> pingData) {
        Long channelId = Long.valueOf(pingData.get("channelId").toString());
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("timestamp", java.time.Instant.now().toString());
        
        WebSocketMessage message = new WebSocketMessage("ping", payload);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
    }

    @MessageMapping("/send-message")
    public void handleSendMessage(@Payload Map<String, Object> messageData, SimpMessageHeaderAccessor headerAccessor) {
        try {
            System.out.println("=== HANDLING SEND MESSAGE ===");
            System.out.println("Message data: " + messageData);
            
            Long channelId = Long.valueOf(messageData.get("channelId").toString());
            Long userId = Long.valueOf(messageData.get("userId").toString());
            String content = messageData.get("content").toString();
            
            // Get user and channel from database
            Optional<User> userOpt = userRepository.findById(userId);
            Optional<Channel> channelOpt = channelRepository.findById(channelId);
            
            if (userOpt.isEmpty() || channelOpt.isEmpty()) {
                logger.error("User or channel not found: userId={}, channelId={}", userId, channelId);
                return;
            }
            
            User user = userOpt.get();
            Channel channel = channelOpt.get();
            
            // Create and save the message to database
            Message newMessage = Message.builder()
                .content(content)
                .author(user)
                .channel(channel)
                .timestamp(LocalDateTime.now())
                .build();
            
            Message savedMessage = messageRepository.save(newMessage);
            System.out.println("Message saved to database with ID: " + savedMessage.getId());
            
            // Create payload for WebSocket broadcast
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", savedMessage.getId());
            payload.put("content", savedMessage.getContent());
            payload.put("channelId", channelId);
            payload.put("timestamp", savedMessage.getTimestamp().toString());
            
            // Author details
            Map<String, Object> authorDetails = new HashMap<>();
            authorDetails.put("id", user.getUserId());
            authorDetails.put("name", user.getUserFirstName() + " " + user.getUserLastName());
            authorDetails.put("avatar", user.getAvatar());
            authorDetails.put("isOnline", user.isOnline());
            payload.put("author", authorDetails);
            
            payload.put("edited", savedMessage.isEdited());
            if (savedMessage.getEditedAt() != null) {
                payload.put("editedAt", savedMessage.getEditedAt().toString());
            }
            
            // Broadcast message to all channel subscribers
            WebSocketMessage wsMessage = new WebSocketMessage("message", payload);
            messagingTemplate.convertAndSend("/topic/channel/" + channelId, wsMessage);
            
            System.out.println("Message broadcasted to channel: " + channelId);
            logger.info("Message sent by user {} to channel {}: {}", user.getUserEmail(), channelId, content);
            
        } catch (Exception e) {
            logger.error("Error handling send message: ", e);
            e.printStackTrace();
            sendErrorToUser(headerAccessor.getSessionId(), "Failed to send message");
        }
    }

    @MessageMapping("/join-channel")
    public void handleJoinChannel(@Payload Map<String, Object> joinData, SimpMessageHeaderAccessor headerAccessor) {
        try {
            Long channelId = Long.valueOf(joinData.get("channelId").toString());
            String sessionId = headerAccessor.getSessionId();
            
            // Update user session with channel
            sessionService.addUserToChannel(sessionId, channelId);
            
            // Get user from session or joinData
            Long userId = Long.valueOf(joinData.get("userId").toString());
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // Update user online status
                user.setOnline(true);
                user.setLastSeen(LocalDateTime.now());
                userRepository.save(user);
                
                // Broadcast user joined
                Map<String, Object> payload = new HashMap<>();
                payload.put("userId", user.getUserId());
                payload.put("userName", user.getUserFirstName() + " " + user.getUserLastName());
                payload.put("channelId", channelId);
                payload.put("avatar", user.getAvatar());
                payload.put("status", user.getStatus());
                
                WebSocketMessage message = new WebSocketMessage("user_joined", payload);
                messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
                
                // Send updated online users list
                broadcastOnlineUsers(channelId);
                
                logger.info("User {} joined channel {}", user.getUserEmail(), channelId);
            }
            
        } catch (Exception e) {
            logger.error("Error handling join channel: ", e);
            sendErrorToUser(headerAccessor.getSessionId(), "Failed to join channel");
        }
    }

    @MessageMapping("/leave-channel")
    public void handleLeaveChannel(@Payload Map<String, Object> leaveData) {
        Long channelId = Long.valueOf(leaveData.get("channelId").toString());
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", leaveData.get("userId"));
        payload.put("userName", leaveData.get("userName"));
        payload.put("channelId", channelId);
        
        WebSocketMessage message = new WebSocketMessage("leave_channel", payload);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
    }

    public void sendOnlineUsersUpdate(Long channelId, Object users) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("channelId", channelId);
        payload.put("users", users);
        
        WebSocketMessage message = new WebSocketMessage("online_users", payload);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
    }
    
    // Broadcast online users for a channel
    public void broadcastOnlineUsers(Long channelId) {
        try {
            List<User> onlineUsers = userRepository.findOnlineUsersByChannel(channelId);
            
            List<OnlineUserDto> onlineUserDtos = onlineUsers.stream()
                .map(this::mapToOnlineUserDto)
                .collect(Collectors.toList());
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("users", onlineUserDtos);
            payload.put("count", onlineUserDtos.size());
            
            WebSocketMessage message = new WebSocketMessage("online_users", payload);
            messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
            
            logger.debug("Broadcasting {} online users to channel {}", onlineUserDtos.size(), channelId);
        } catch (Exception e) {
            logger.error("Error broadcasting online users: ", e);
        }
    }
    
    // Broadcast user status change
    public void broadcastUserStatusChange(User user) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", user.getUserId());
            payload.put("userName", user.getUserFirstName() + " " + user.getUserLastName());
            payload.put("status", user.getStatus());
            payload.put("isOnline", user.isOnline());
            payload.put("lastSeen", user.getLastSeen());
            
            WebSocketMessage message = new WebSocketMessage("user_status_change", payload);
            messagingTemplate.convertAndSend("/topic/user-status", message);
            
            logger.debug("Broadcasting status change for user {}: {}", user.getUserEmail(), user.getStatus());
        } catch (Exception e) {
            logger.error("Error broadcasting user status change: ", e);
        }
    }
    
    // Send error to specific user session
    public void sendErrorToUser(String sessionId, String errorMessage) {
        try {
            WebSocketMessage errorMsg = new WebSocketMessage("error", Map.of("message", errorMessage));
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/errors", errorMsg);
        } catch (Exception e) {
            logger.error("Error sending error message to user: ", e);
        }
    }
    
    // Broadcast user joined channel
    public void broadcastUserJoined(Long channelId, User user) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", user.getUserId());
            payload.put("userName", user.getUserFirstName() + " " + user.getUserLastName());
            payload.put("channelId", channelId);
            payload.put("avatar", user.getAvatar());
            
            WebSocketMessage message = new WebSocketMessage("user_joined", payload);
            messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
            
            // Also update online users
            broadcastOnlineUsers(channelId);
        } catch (Exception e) {
            logger.error("Error broadcasting user joined: ", e);
        }
    }
    
    // Broadcast user left channel
    public void broadcastUserLeft(Long channelId, User user) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", user.getUserId());
            payload.put("userName", user.getUserFirstName() + " " + user.getUserLastName());
            payload.put("channelId", channelId);
            
            WebSocketMessage message = new WebSocketMessage("user_left", payload);
            messagingTemplate.convertAndSend("/topic/channel/" + channelId, message);
            
            // Also update online users
            broadcastOnlineUsers(channelId);
        } catch (Exception e) {
            logger.error("Error broadcasting user left: ", e);
        }
    }
    
    private OnlineUserDto mapToOnlineUserDto(User user) {
        return OnlineUserDto.builder()
            .id(user.getUserId())
            .name(user.getUserFirstName() + " " + user.getUserLastName())
            .status(user.getStatus() != null ? user.getStatus() : "OFFLINE")
            .lastSeen(user.getLastSeen())
            .avatarUrl(user.getAvatar())
            .isOnline(user.isOnline())
            .userRole(user.getUserRole() != null ? user.getUserRole().toString() : "STUDENT")
            .build();
    }
}
