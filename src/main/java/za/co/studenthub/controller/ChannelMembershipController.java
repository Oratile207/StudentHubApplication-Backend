package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.ChannelMembership;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.ChannelRole;
import za.co.studenthub.dto.ChannelMemberDto;
import za.co.studenthub.dto.MembershipStatusDto;
import za.co.studenthub.dto.MessageResponse;
import za.co.studenthub.dto.OnlineUserDto;
import za.co.studenthub.repository.ChannelMembershipRepository;
import za.co.studenthub.repository.ChannelRepository;
import za.co.studenthub.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/channel-membership")
@CrossOrigin(origins = "http://localhost:3000")
public class ChannelMembershipController {
    
    @Autowired
    private ChannelMembershipRepository membershipRepository;
    
    @Autowired
    private ChannelRepository channelRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByUserEmail(email).orElse(null);
    }
    

    
    // Get channel members with online status
    @GetMapping("/{channelId}/members")
    public ResponseEntity<?> getChannelMembers(@PathVariable Long channelId) {
        try {
            Optional<Channel> channelOpt = channelRepository.findById(channelId);
            if (!channelOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Channel not found"));
            }
            
            Channel channel = channelOpt.get();
            List<ChannelMembership> memberships = 
                membershipRepository.findByChannelAndIsActiveTrueOrderByJoinedAtAsc(channel);
            
            List<ChannelMemberDto> memberDtos = memberships.stream()
                .map(this::mapToChannelMemberDto)
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(memberDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error fetching channel members: " + e.getMessage()));
        }
    }
    
    // Check membership status
    @GetMapping("/check-membership/{channelId}")
    public ResponseEntity<?> checkMembership(@PathVariable Long channelId, Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            Optional<Channel> channelOpt = channelRepository.findById(channelId);
            if (!channelOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Channel not found"));
            }
            
            Channel channel = channelOpt.get();
            Optional<ChannelMembership> membershipOpt = 
                membershipRepository.findByUserAndChannel(user, channel);
            
            MembershipStatusDto statusDto;
            if (membershipOpt.isPresent()) {
                ChannelMembership membership = membershipOpt.get();
                statusDto = MembershipStatusDto.builder()
                    .isMember(membership.getIsActive())
                    .role(membership.getRole().toString())
                    .joinedAt(membership.getJoinedAt())
                    .isActive(membership.getIsActive())
                    .build();
            } else {
                statusDto = MembershipStatusDto.builder()
                    .isMember(false)
                    .role(null)
                    .joinedAt(null)
                    .isActive(false)
                    .build();
            }
            
            return ResponseEntity.ok(statusDto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error checking membership: " + e.getMessage()));
        }
    }
    
    // Get online users (global or by channel)
    @GetMapping("/online-users")
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
            
            List<OnlineUserDto> onlineUserDtos = onlineUsers.stream()
                .map(this::mapToOnlineUserDto)
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(onlineUserDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error fetching online users: " + e.getMessage()));
        }
    }
    
    // Get user's channels
    @GetMapping("/my-channels")
    public ResponseEntity<?> getMyChannels(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            List<ChannelMembership> memberships = 
                membershipRepository.findByUserAndIsActiveTrueOrderByJoinedAtDesc(user);
            
            List<Channel> channels = memberships.stream()
                .map(ChannelMembership::getChannel)
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(channels);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error fetching user channels: " + e.getMessage()));
        }
    }
    
    // Update user role in channel (admin only)
    @PatchMapping("/{channelId}/members/{userId}/role")
    public ResponseEntity<MessageResponse> updateMemberRole(
            @PathVariable Long channelId, 
            @PathVariable Long userId, 
            @RequestBody java.util.Map<String, String> request,
            Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            Optional<Channel> channelOpt = channelRepository.findById(channelId);
            if (!channelOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Channel not found"));
            }
            
            Channel channel = channelOpt.get();
            
            // Check if current user is admin of the channel
            if (!membershipRepository.isUserModeratorOrAdmin(currentUser, channel)) {
                return ResponseEntity.status(403).body(new MessageResponse("Not authorized to modify roles"));
            }
            
            Optional<User> targetUserOpt = userRepository.findById(userId);
            if (!targetUserOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Target user not found"));
            }
            
            User targetUser = targetUserOpt.get();
            
            Optional<ChannelMembership> membershipOpt = 
                membershipRepository.findByUserAndChannelAndIsActiveTrue(targetUser, channel);
            
            if (!membershipOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("User is not a member of this channel"));
            }
            
            String roleString = request.get("role");
            if (roleString == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Role is required"));
            }
            
            ChannelRole newRole;
            try {
                newRole = ChannelRole.valueOf(roleString.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid role"));
            }
            
            ChannelMembership membership = membershipOpt.get();
            membership.setRole(newRole);
            membershipRepository.save(membership);
            
            return ResponseEntity.ok(new MessageResponse("Member role updated successfully"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error updating member role: " + e.getMessage()));
        }
    }
    
    // Helper methods for mapping entities to DTOs
    private ChannelMemberDto mapToChannelMemberDto(ChannelMembership membership) {
        User user = membership.getUser();
        
        return ChannelMemberDto.builder()
            .id(user.getUserId())
            .name(user.getUserFirstName() + " " + user.getUserLastName())
            .email(user.getUserEmail())
            .isOnline(user.isOnline())
            .role(membership.getRole().toString())
            .joinedAt(membership.getJoinedAt())
            .avatarUrl(user.getAvatar())
            .status(user.getStatus() != null ? user.getStatus() : "OFFLINE")
            .userRole(user.getUserRole() != null ? user.getUserRole().toString() : "STUDENT")
            .build();
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
