package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.Message;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.repository.MessageRepository;
import za.co.studenthub.repository.UserRepository;
import za.co.studenthub.repository.ChannelRepository;
import za.co.studenthub.dto.MessageRequest;
import za.co.studenthub.dto.WebSocketMessage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(MessageRepository messageRepository, 
                           UserRepository userRepository, 
                           ChannelRepository channelRepository,
                           SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<Page<Message>> getMessages(
            @PathVariable Long channelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = messageRepository.findByChannelChannelIdOrderByTimestampDesc(channelId, pageable);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest request, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User author = userRepository.findByUserEmail(userEmail).orElse(null);
            Channel channel = channelRepository.findById(request.getChannelId()).orElse(null);
            
            if (author == null || channel == null) {
                return ResponseEntity.badRequest().build();
            }

            Message message = Message.builder()
                    .content(request.getContent())
                    .author(author)
                    .channel(channel)
                    .timestamp(LocalDateTime.now())
                    .build();

            Message savedMessage = messageRepository.save(message);

            // Send WebSocket notification
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", savedMessage.getId());
            payload.put("content", savedMessage.getContent());
            payload.put("channelId", savedMessage.getChannel().getChannelId());
            payload.put("timestamp", savedMessage.getTimestamp().toString());
            
            Map<String, Object> authorInfo = new HashMap<>();
            authorInfo.put("id", author.getId());
            authorInfo.put("name", author.getUserFirstName() + " " + author.getUserLastName());
            authorInfo.put("isOnline", true);
            payload.put("author", authorInfo);

            WebSocketMessage wsMessage = new WebSocketMessage("message", payload);
            messagingTemplate.convertAndSend("/topic/channel/" + request.getChannelId(), wsMessage);

            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Message> editMessage(@PathVariable Long messageId, 
                                             @RequestBody MessageRequest request,
                                             Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByUserEmail(userEmail).orElse(null);
            Message message = messageRepository.findById(messageId).orElse(null);
            
            if (message == null || user == null || !message.getAuthor().getId().equals(user.getId())) {
                return ResponseEntity.badRequest().build();
            }

            message.setContent(request.getContent());
            Message updatedMessage = messageRepository.save(message);

            return ResponseEntity.ok(updatedMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User user = userRepository.findByUserEmail(userEmail).orElse(null);
            Message message = messageRepository.findById(messageId).orElse(null);
            
            if (message == null || user == null || !message.getAuthor().getId().equals(user.getId())) {
                return ResponseEntity.badRequest().build();
            }

            messageRepository.delete(message);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
