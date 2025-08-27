package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.User;
import za.co.studenthub.dto.MessageResponse;
import za.co.studenthub.dto.OnlineUserDto;
import za.co.studenthub.dto.UserStatusDto;
import za.co.studenthub.repository.UserRepository;
import za.co.studenthub.services.user_services.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserStatusController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByUserEmail(email).orElse(null);
    }
    
    // Update user status
    @PatchMapping("/status")
    public ResponseEntity<MessageResponse> updateStatus(@RequestBody UserStatusDto request, Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            String status = request.getStatus();
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Status is required"));
            }
            
            // Validate status
            String normalizedStatus = status.toUpperCase().trim();
            if (!isValidStatus(normalizedStatus)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid status. Valid statuses: ONLINE, AWAY, BUSY, INVISIBLE, OFFLINE"));
            }
            
            user.setStatus(normalizedStatus);
            user.setLastSeen(LocalDateTime.now());
            
            // Set isOnline flag based on status
            boolean isOnline = "ONLINE".equals(normalizedStatus) || 
                              "AWAY".equals(normalizedStatus) || 
                              "BUSY".equals(normalizedStatus);
            user.setOnline(isOnline);
            
            userService.update(user);
            
            return ResponseEntity.ok(new MessageResponse("Status updated to " + normalizedStatus));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error updating status: " + e.getMessage()));
        }
    }
    
    // Get online users
    @GetMapping("/online")
    public ResponseEntity<?> getOnlineUsers(@RequestParam(required = false) Long channelId) {
        try {
            List<User> onlineUsers;
            
            if (channelId != null) {
                // Get online users in specific channel
                onlineUsers = userRepository.findOnlineUsersByChannel(channelId);
            } else {
                // Get all online users
                onlineUsers = userRepository.findByIsOnlineTrue();
            }
            
            List<OnlineUserDto> dtos = onlineUsers.stream()
                .map(this::mapToOnlineUserDto)
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error fetching online users: " + e.getMessage()));
        }
    }
    
    // Get current user status
    @GetMapping("/status")
    public ResponseEntity<?> getCurrentStatus(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            OnlineUserDto userDto = mapToOnlineUserDto(user);
            return ResponseEntity.ok(userDto);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error fetching user status: " + e.getMessage()));
        }
    }
    
    // Set user offline (for logout)
    @PostMapping("/offline")
    public ResponseEntity<MessageResponse> setOffline(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            user.setStatus("OFFLINE");
            user.setOnline(false);
            user.setLastSeen(LocalDateTime.now());
            userService.update(user);
            
            return ResponseEntity.ok(new MessageResponse("User set to offline"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error setting offline status: " + e.getMessage()));
        }
    }
    
    // Set user online (for login)
    @PostMapping("/online")
    public ResponseEntity<MessageResponse> setOnline(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            user.setStatus("ONLINE");
            user.setOnline(true);
            user.setLastSeen(LocalDateTime.now());
            userService.update(user);
            
            return ResponseEntity.ok(new MessageResponse("User set to online"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error setting online status: " + e.getMessage()));
        }
    }
    
    private boolean isValidStatus(String status) {
        return "ONLINE".equals(status) || 
               "AWAY".equals(status) || 
               "BUSY".equals(status) || 
               "INVISIBLE".equals(status) || 
               "OFFLINE".equals(status);
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
