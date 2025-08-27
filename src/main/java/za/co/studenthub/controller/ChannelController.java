package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.ChannelMembership;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.ChannelRole;
import za.co.studenthub.dto.CreateChannelRequest;
import za.co.studenthub.dto.MessageResponse;
import za.co.studenthub.repository.ChannelMembershipRepository;
import za.co.studenthub.repository.UserRepository;
import za.co.studenthub.services.channel_services.ChannelService;

import java.time.LocalDateTime;
import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("channel")
@CrossOrigin(origins = "http://localhost:3000")
public class ChannelController {
    private final ChannelService channelService;
    private final UserRepository userRepository;
    private final ChannelMembershipRepository membershipRepository;
    
    @Autowired
    public ChannelController(ChannelService channelService, UserRepository userRepository, 
                           ChannelMembershipRepository membershipRepository) {
        this.channelService = channelService;
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateChannelRequest request, Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(401).body(new MessageResponse("Authentication required"));
            }
            
            String userEmail = authentication.getName();
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.status(401).body(new MessageResponse("Invalid authentication"));
            }
            
            User admin = userRepository.findByUserEmail(userEmail).orElse(null);
            
            if (admin == null) {
                return ResponseEntity.status(404).body(new MessageResponse("User not found with email: " + userEmail));
            }
            
            if (request == null || request.getChannelNameField() == null || request.getChannelNameField().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Channel name is required"));
            }
            
            Channel channel = Channel.builder()
                    .channelName(request.getChannelNameField())
                    .channelType(request.getChannelTypeField())
                    .description(request.getDescription())
                    .adminCreatedChannel(admin)
                    .channelMembers(new java.util.HashSet<>())
                    .build();
                    
            // Add the admin as the first member
            channel.getChannelMembers().add(admin);
            
            Channel savedChannel = channelService.create(channel);
            return ResponseEntity.ok(savedChannel);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error creating channel: " + e.getMessage()));
        }
    }

    @GetMapping("/read/{id}")
    public Channel read(@PathVariable Long id) {
        return channelService.read(id);
    }

    @PutMapping("/update")
    public Channel update(@RequestBody Channel channel) {
        return channelService.update(channel);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        channelService.delete(id);
    }

    @GetMapping("/getAll")
    public List<Channel> getAll() {
        return channelService.getAll();
    }

    // Enhanced join channel with proper error handling and membership tracking
    @PostMapping("/join/{channelId}")
    public ResponseEntity<MessageResponse> joinChannel(@PathVariable Long channelId, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByUserEmail(userEmail).orElse(null);
            
            if (user == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            Optional<Channel> channelOpt = channelService.read(channelId) != null ? 
                Optional.of(channelService.read(channelId)) : Optional.empty();
            if (!channelOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Channel not found"));
            }
            
            Channel channel = channelOpt.get();
            
            // Check if already a member using the new membership system
            Optional<ChannelMembership> existingMembership = 
                membershipRepository.findByUserAndChannel(user, channel);
            
            if (existingMembership.isPresent()) {
                if (existingMembership.get().getIsActive()) {
                    return ResponseEntity.badRequest()
                        .body(new MessageResponse("Already a member of this channel"));
                } else {
                    // Reactivate membership
                    ChannelMembership membership = existingMembership.get();
                    membership.setIsActive(true);
                    membership.setLeftAt(null);
                    membership.setJoinedAt(LocalDateTime.now()); // Update join time
                    membershipRepository.save(membership);
                    
                    return ResponseEntity.ok(new MessageResponse("Rejoined channel successfully"));
                }
            } else {
                // Create new membership
                ChannelMembership membership = ChannelMembership.builder()
                    .user(user)
                    .channel(channel)
                    .role(ChannelRole.MEMBER)
                    .isActive(true)
                    .build();
                
                membershipRepository.save(membership);
                
                // Also add to the old system for backward compatibility
                channelService.addUserToChannel(channelId, user);
                
                return ResponseEntity.ok(new MessageResponse("Joined channel successfully"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(new MessageResponse("Failed to join channel: " + e.getMessage()));
        }
    }

    @DeleteMapping("/leave/{channelId}")
    public ResponseEntity<String> leaveChannel(@PathVariable Long channelId, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByUserEmail(userEmail).orElse(null);
            
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Channel updatedChannel = channelService.removeUserFromChannel(channelId, user);
            if (updatedChannel == null) {
                return ResponseEntity.badRequest().body("Channel not found or user not in channel");
            }

            // Send WebSocket notification for user left
            java.util.Map<String, Object> userData = new java.util.HashMap<>();
            userData.put("userId", user.getId());
            userData.put("userName", user.getUserFirstName() + " " + user.getUserLastName());
            userData.put("channelId", channelId);
            userData.put("action", "left");
            
            return ResponseEntity.ok("Left channel successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error leaving channel: " + e.getMessage());
        }
    }

    @GetMapping("/{channelId}/members")
    public ResponseEntity<List<User>> getChannelMembers(@PathVariable Long channelId) {
        try {
            Channel channel = channelService.read(channelId);
            if (channel == null) {
                return ResponseEntity.notFound().build();
            }
            
            List<User> members = new java.util.ArrayList<>();
            if (channel.getChannelMembers() != null) {
                members.addAll(channel.getChannelMembers());
            }
            
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/my-channels")
    public ResponseEntity<List<Channel>> getMyChannels(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByUserEmail(userEmail).orElse(null);
            
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            
            List<Channel> channels = channelService.getChannelsForUser(user);
            return ResponseEntity.ok(channels);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/check-membership/{channelId}")
    public ResponseEntity<Boolean> checkMembership(@PathVariable Long channelId, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByUserEmail(userEmail).orElse(null);
            
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            
            boolean isMember = channelService.isUserMemberOfChannel(channelId, user);
            return ResponseEntity.ok(isMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
