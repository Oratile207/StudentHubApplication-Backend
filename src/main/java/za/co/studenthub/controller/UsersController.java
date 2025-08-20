package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.User;
import za.co.studenthub.repository.UserRepository;
import za.co.studenthub.dto.StatusUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UsersController {

    private final UserRepository userRepository;

    @Autowired
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/online")
    public ResponseEntity<List<User>> getOnlineUsers(@RequestParam(required = false) Long channelId) {
        try {
            List<User> allUsers = userRepository.findAll();
            List<User> onlineUsers = allUsers.stream()
                    .filter(User::isOnline)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(onlineUsers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/status")
    public ResponseEntity<String> updateUserStatus(@RequestBody StatusUpdateRequest request, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByUserEmail(userEmail).orElse(null);
            
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            if (request.getStatus() != null) {
                user.setStatus(request.getStatus());
            }
            
            if (request.getIsOnline() != null) {
                user.setOnline(request.getIsOnline());
            }

            userRepository.save(user);
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status");
        }
    }
}
