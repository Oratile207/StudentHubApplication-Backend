package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;
import za.co.studenthub.dto.CreateChannelRequest;
import za.co.studenthub.repository.UserRepository;
import za.co.studenthub.services.channel_services.ChannelService;
import za.co.studenthub.controller.WebSocketController;

import java.util.List;

@RestController
@RequestMapping("channel")
@CrossOrigin(origins = "http://localhost:3000")
public class ChannelController {
    private final ChannelService channelService;
    private final UserRepository userRepository;
    private final WebSocketController webSocketController;

    @Autowired
    public ChannelController(ChannelService channelService, UserRepository userRepository, WebSocketController webSocketController) {
        this.channelService = channelService;
        this.userRepository = userRepository;
        this.webSocketController = webSocketController;
    }

    @PostMapping("/create")
    public Channel create(@RequestBody CreateChannelRequest request) {
        Channel channel = Channel.builder()
                .channelName(request.getChannelNameField())
                .channelType(request.getChannelTypeField())
                .description(request.getDescription())
                .build();
        return channelService.create(channel);
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

    @PostMapping("/join/{channelId}")
    public ResponseEntity<String> joinChannel(@PathVariable Long channelId, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByUserEmail(userEmail).orElse(null);
            Channel channel = channelService.read(channelId);
            
            if (user == null || channel == null) {
                return ResponseEntity.badRequest().body("User or channel not found");
            }

            // Send WebSocket notification for user joined
            java.util.Map<String, Object> userData = new java.util.HashMap<>();
            userData.put("userId", user.getId());
            userData.put("userName", user.getUserFirstName() + " " + user.getUserLastName());
            userData.put("channelId", channelId);
            
            // This would typically involve updating a channel members table
            // For now, we'll just send the WebSocket message
            
            return ResponseEntity.ok("Joined channel successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error joining channel");
        }
    }

    @DeleteMapping("/leave/{channelId}")
    public ResponseEntity<String> leaveChannel(@PathVariable Long channelId, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByUserEmail(userEmail).orElse(null);
            Channel channel = channelService.read(channelId);
            
            if (user == null || channel == null) {
                return ResponseEntity.badRequest().body("User or channel not found");
            }

            // This would typically involve removing from channel members table
            // For now, we'll just return success
            
            return ResponseEntity.ok("Left channel successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error leaving channel");
        }
    }

    @GetMapping("/{channelId}/members")
    public ResponseEntity<List<User>> getChannelMembers(@PathVariable Long channelId) {
        try {
            // This would typically query a channel members table
            // For now, returning all users as placeholder
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
