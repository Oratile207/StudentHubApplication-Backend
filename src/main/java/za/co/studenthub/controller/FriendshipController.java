package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.Friendship;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.FriendshipStatus;
import za.co.studenthub.dto.FriendDto;
import za.co.studenthub.dto.FriendRequestDto;
import za.co.studenthub.dto.MessageResponse;
import za.co.studenthub.dto.UserSearchDto;
import za.co.studenthub.repository.FriendshipRepository;
import za.co.studenthub.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class FriendshipController {
    
    @Autowired
    private FriendshipRepository friendshipRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByUserEmail(email).orElse(null);
    }
    
    // Get user's friends
    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            List<Friendship> friendships = friendshipRepository.findAcceptedFriendships(currentUser, FriendshipStatus.ACCEPTED);
            List<FriendDto> friendDtos = friendships.stream()
                .map(friendship -> {
                    // Get the friend user (the other user in the relationship)
                    User friend = friendship.getFromUser().equals(currentUser) 
                        ? friendship.getToUser() 
                        : friendship.getFromUser();
                    return mapToFriendDto(friend);
                })
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(friendDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error fetching friends: " + e.getMessage()));
        }
    }
    
    // Send friend request
    @PostMapping("/friend-request")
    public ResponseEntity<MessageResponse> sendFriendRequest(@RequestBody Map<String, Long> request, Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            Long targetUserId = request.get("userId");
            if (targetUserId == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Target user ID is required"));
            }
            
            Optional<User> targetUserOpt = userRepository.findById(targetUserId);
            if (!targetUserOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Target user not found"));
            }
            
            User targetUser = targetUserOpt.get();
            
            // Check if users are the same
            if (currentUser.getUserId().equals(targetUserId)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Cannot send friend request to yourself"));
            }
            
            // Check if friendship already exists
            Optional<Friendship> existingFriendship = friendshipRepository.findByUsers(currentUser, targetUser);
            if (existingFriendship.isPresent()) {
                FriendshipStatus status = existingFriendship.get().getStatus();
                switch (status) {
                    case PENDING:
                        return ResponseEntity.badRequest().body(new MessageResponse("Friend request already sent"));
                    case ACCEPTED:
                        return ResponseEntity.badRequest().body(new MessageResponse("Users are already friends"));
                    case BLOCKED:
                        return ResponseEntity.badRequest().body(new MessageResponse("Cannot send friend request"));
                    case REJECTED:
                        // Allow resending after rejection
                        existingFriendship.get().setStatus(FriendshipStatus.PENDING);
                        existingFriendship.get().setFromUser(currentUser);
                        existingFriendship.get().setToUser(targetUser);
                        friendshipRepository.save(existingFriendship.get());
                        return ResponseEntity.ok(new MessageResponse("Friend request sent"));
                }
            }
            
            // Create new friendship request
            Friendship friendship = Friendship.builder()
                .fromUser(currentUser)
                .toUser(targetUser)
                .status(FriendshipStatus.PENDING)
                .build();
                
            friendshipRepository.save(friendship);
            
            return ResponseEntity.ok(new MessageResponse("Friend request sent"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error sending friend request: " + e.getMessage()));
        }
    }
    
    // Accept friend request
    @PostMapping("/friend-request/accept")
    public ResponseEntity<MessageResponse> acceptFriendRequest(@RequestBody Map<String, Long> request, Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            Long fromUserId = request.get("userId");
            if (fromUserId == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("From user ID is required"));
            }
            
            Optional<User> fromUserOpt = userRepository.findById(fromUserId);
            if (!fromUserOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("From user not found"));
            }
            
            User fromUser = fromUserOpt.get();
            
            // Find pending friendship request
            Optional<Friendship> friendshipOpt = friendshipRepository.findByUsers(currentUser, fromUser);
            if (!friendshipOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Friend request not found"));
            }
            
            Friendship friendship = friendshipOpt.get();
            if (friendship.getStatus() != FriendshipStatus.PENDING || !friendship.getToUser().equals(currentUser)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid friend request"));
            }
            
            friendship.setStatus(FriendshipStatus.ACCEPTED);
            friendshipRepository.save(friendship);
            
            return ResponseEntity.ok(new MessageResponse("Friend request accepted"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error accepting friend request: " + e.getMessage()));
        }
    }
    
    // Reject friend request
    @PostMapping("/friend-request/reject")
    public ResponseEntity<MessageResponse> rejectFriendRequest(@RequestBody Map<String, Long> request, Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            Long fromUserId = request.get("userId");
            if (fromUserId == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("From user ID is required"));
            }
            
            Optional<User> fromUserOpt = userRepository.findById(fromUserId);
            if (!fromUserOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("From user not found"));
            }
            
            User fromUser = fromUserOpt.get();
            
            Optional<Friendship> friendshipOpt = friendshipRepository.findByUsers(currentUser, fromUser);
            if (!friendshipOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Friend request not found"));
            }
            
            Friendship friendship = friendshipOpt.get();
            if (friendship.getStatus() != FriendshipStatus.PENDING || !friendship.getToUser().equals(currentUser)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid friend request"));
            }
            
            friendship.setStatus(FriendshipStatus.REJECTED);
            friendshipRepository.save(friendship);
            
            return ResponseEntity.ok(new MessageResponse("Friend request rejected"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error rejecting friend request: " + e.getMessage()));
        }
    }
    
    // Get pending friend requests
    @GetMapping("/friend-requests")
    public ResponseEntity<?> getFriendRequests(Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            List<Friendship> pendingRequests = friendshipRepository.findByToUserAndStatus(currentUser, FriendshipStatus.PENDING);
            List<FriendRequestDto> requestDtos = pendingRequests.stream()
                .map(this::mapToFriendRequestDto)
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(requestDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error fetching friend requests: " + e.getMessage()));
        }
    }
    
    // Remove friend
    @DeleteMapping("/friends/{userId}")
    public ResponseEntity<MessageResponse> removeFriend(@PathVariable Long userId, Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            Optional<User> friendOpt = userRepository.findById(userId);
            if (!friendOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Friend not found"));
            }
            
            User friend = friendOpt.get();
            
            Optional<Friendship> friendshipOpt = friendshipRepository.findByUsers(currentUser, friend);
            if (!friendshipOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Friendship not found"));
            }
            
            friendshipRepository.delete(friendshipOpt.get());
            
            return ResponseEntity.ok(new MessageResponse("Friend removed"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error removing friend: " + e.getMessage()));
        }
    }
    
    // Search users
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam("q") String query, Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Search query is required"));
            }
            
            // Search users by name or email containing the query (case insensitive)
            List<User> users = userRepository.findByUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCaseOrUserEmailContainingIgnoreCase(
                query.trim(), query.trim(), query.trim());
            
            // Remove current user from results
            users = users.stream()
                .filter(user -> !user.getUserId().equals(currentUser.getUserId()))
                .collect(Collectors.toList());
            
            List<UserSearchDto> userSearchDtos = users.stream()
                .map(user -> mapToUserSearchDto(user, currentUser))
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(userSearchDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error searching users: " + e.getMessage()));
        }
    }
    
    // Block user
    @PostMapping("/block")
    public ResponseEntity<MessageResponse> blockUser(@RequestBody Map<String, Long> request, Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            Long targetUserId = request.get("userId");
            if (targetUserId == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Target user ID is required"));
            }
            
            Optional<User> targetUserOpt = userRepository.findById(targetUserId);
            if (!targetUserOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Target user not found"));
            }
            
            User targetUser = targetUserOpt.get();
            
            Optional<Friendship> existingFriendship = friendshipRepository.findByUsers(currentUser, targetUser);
            if (existingFriendship.isPresent()) {
                existingFriendship.get().setStatus(FriendshipStatus.BLOCKED);
                existingFriendship.get().setFromUser(currentUser);
                existingFriendship.get().setToUser(targetUser);
                friendshipRepository.save(existingFriendship.get());
            } else {
                Friendship friendship = Friendship.builder()
                    .fromUser(currentUser)
                    .toUser(targetUser)
                    .status(FriendshipStatus.BLOCKED)
                    .build();
                friendshipRepository.save(friendship);
            }
            
            return ResponseEntity.ok(new MessageResponse("User blocked"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error blocking user: " + e.getMessage()));
        }
    }
    
    // Unblock user
    @DeleteMapping("/block/{userId}")
    public ResponseEntity<MessageResponse> unblockUser(@PathVariable Long userId, Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);
            if (currentUser == null) {
                return ResponseEntity.status(401).body(new MessageResponse("User not found"));
            }
            
            Optional<User> blockedUserOpt = userRepository.findById(userId);
            if (!blockedUserOpt.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Blocked user not found"));
            }
            
            User blockedUser = blockedUserOpt.get();
            
            Optional<Friendship> friendshipOpt = friendshipRepository.findByUsers(currentUser, blockedUser);
            if (!friendshipOpt.isPresent() || friendshipOpt.get().getStatus() != FriendshipStatus.BLOCKED) {
                return ResponseEntity.badRequest().body(new MessageResponse("User is not blocked"));
            }
            
            friendshipRepository.delete(friendshipOpt.get());
            
            return ResponseEntity.ok(new MessageResponse("User unblocked"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error unblocking user: " + e.getMessage()));
        }
    }
    
    // Helper methods for mapping entities to DTOs
    private FriendDto mapToFriendDto(User user) {
        return FriendDto.builder()
            .id(user.getUserId())
            .name(user.getUserFirstName() + " " + user.getUserLastName())
            .email(user.getUserEmail())
            .isOnline(user.isOnline())
            .status(user.getStatus() != null ? user.getStatus() : "OFFLINE")
            .avatarUrl(user.getAvatar())
            .lastSeen(user.getLastSeen())
            .userRole(user.getUserRole() != null ? user.getUserRole().toString() : "STUDENT")
            .build();
    }
    
    private FriendRequestDto mapToFriendRequestDto(Friendship friendship) {
        User fromUser = friendship.getFromUser();
        User toUser = friendship.getToUser();
        
        return FriendRequestDto.builder()
            .userId(fromUser.getUserId())
            .fromUserId(fromUser.getUserId())
            .fromUserName(fromUser.getUserFirstName() + " " + fromUser.getUserLastName())
            .fromUserEmail(fromUser.getUserEmail())
            .fromUserAvatar(fromUser.getAvatar())
            .toUserId(toUser.getUserId())
            .toUserName(toUser.getUserFirstName() + " " + toUser.getUserLastName())
            .toUserEmail(toUser.getUserEmail())
            .status(friendship.getStatus().toString())
            .createdAt(friendship.getCreatedAt())
            .build();
    }
    
    private UserSearchDto mapToUserSearchDto(User user, User currentUser) {
        Optional<Friendship> friendship = friendshipRepository.findByUsers(currentUser, user);
        
        boolean isFriend = false;
        boolean requestSent = false;
        boolean requestReceived = false;
        
        if (friendship.isPresent()) {
            FriendshipStatus status = friendship.get().getStatus();
            if (status == FriendshipStatus.ACCEPTED) {
                isFriend = true;
            } else if (status == FriendshipStatus.PENDING) {
                if (friendship.get().getFromUser().equals(currentUser)) {
                    requestSent = true;
                } else {
                    requestReceived = true;
                }
            }
        }
        
        return UserSearchDto.builder()
            .id(user.getUserId())
            .name(user.getUserFirstName() + " " + user.getUserLastName())
            .email(user.getUserEmail())
            .isOnline(user.isOnline())
            .isFriend(isFriend)
            .requestSent(requestSent)
            .requestReceived(requestReceived)
            .avatarUrl(user.getAvatar())
            .status(user.getStatus() != null ? user.getStatus() : "OFFLINE")
            .userRole(user.getUserRole() != null ? user.getUserRole().toString() : "STUDENT")
            .build();
    }
}
