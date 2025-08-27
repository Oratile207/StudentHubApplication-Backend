package za.co.studenthub.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import za.co.studenthub.controller.WebSocketController;
import za.co.studenthub.domain.User;
import za.co.studenthub.repository.UserRepository;
import za.co.studenthub.security.JwtUtil;
import za.co.studenthub.services.WebSocketSessionService;
import za.co.studenthub.services.user_services.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private WebSocketSessionService sessionService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private WebSocketController webSocketController;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("New WebSocket connection: {}", event.getMessage());
        
        try {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
            String sessionId = headerAccessor.getSessionId();
            
            // Extract token from connection headers
            String token = extractTokenFromHeaders(headerAccessor);
            
            if (token != null && jwtUtil.validateToken(token)) {
                String userEmail = jwtUtil.extractEmail(token);
                User user = userRepository.findByUserEmail(userEmail).orElse(null);
                
                if (user != null) {
                    // Update user online status
                    user.setOnline(true);
                    user.setLastSeen(LocalDateTime.now());
                    user.setStatus("ONLINE");
                    userService.update(user);
                    
                    // Store session info
                    sessionService.addUserSession(sessionId, user);
                    
                    logger.info("User {} connected via WebSocket with session {}", user.getUserEmail(), sessionId);
                    
                    // Broadcast user status change
                    webSocketController.broadcastUserStatusChange(user);
                } else {
                    logger.warn("User not found for token in WebSocket connection");
                }
            } else {
                logger.warn("Invalid or missing token in WebSocket connection");
            }
            
        } catch (Exception e) {
            logger.error("Failed to handle WebSocket connection: ", e);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        try {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
            String sessionId = headerAccessor.getSessionId();
            
            logger.info("WebSocket disconnection for session: {}", sessionId);
            
            // Get user from session
            User user = sessionService.getUserBySession(sessionId);
            if (user != null) {
                // Get user's channels before cleaning up session
                List<Long> userChannels = sessionService.getUserChannels(sessionId);
                
                // Clean up session first
                sessionService.removeUserSession(sessionId);
                
                // Check if user has other active sessions
                if (!sessionService.isUserOnline(user.getUserId())) {
                    // User is completely offline now
                    user.setOnline(false);
                    user.setLastSeen(LocalDateTime.now());
                    user.setStatus("OFFLINE");
                    userService.update(user);
                    
                    // Broadcast user left from all channels
                    for (Long channelId : userChannels) {
                        webSocketController.broadcastUserLeft(channelId, user);
                    }
                    
                    // Broadcast user status change
                    webSocketController.broadcastUserStatusChange(user);
                    
                    logger.info("User {} went offline after WebSocket disconnection", user.getUserEmail());
                } else {
                    logger.info("User {} still has other active sessions", user.getUserEmail());
                }
            }
            
        } catch (Exception e) {
            logger.error("Error handling WebSocket disconnection: ", e);
        }
    }
    
    private String extractTokenFromHeaders(StompHeaderAccessor headerAccessor) {
        try {
            // Try to extract token from various header locations
            List<String> authHeaders = headerAccessor.getNativeHeader("Authorization");
            if (authHeaders != null && !authHeaders.isEmpty()) {
                String authHeader = authHeaders.get(0);
                if (authHeader.startsWith("Bearer ")) {
                    return authHeader.substring(7);
                }
            }
            
            // Try other common header names
            List<String> tokenHeaders = headerAccessor.getNativeHeader("token");
            if (tokenHeaders != null && !tokenHeaders.isEmpty()) {
                return tokenHeaders.get(0);
            }
            
            // Try query parameter (for cases where headers can't be set)
            List<String> tokenParams = headerAccessor.getNativeHeader("token-param");
            if (tokenParams != null && !tokenParams.isEmpty()) {
                return tokenParams.get(0);
            }
            
        } catch (Exception e) {
            logger.error("Error extracting token from WebSocket headers: ", e);
        }
        
        return null;
    }
}
