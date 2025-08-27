package za.co.studenthub.services;

import org.springframework.stereotype.Service;
import za.co.studenthub.domain.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionService {
    
    // Session ID -> User mapping
    private final Map<String, User> sessionUsers = new ConcurrentHashMap<>();
    
    // Session ID -> Set of Channel IDs
    private final Map<String, Set<Long>> sessionChannels = new ConcurrentHashMap<>();
    
    // User ID -> Set of Session IDs (for multi-device support)
    private final Map<Long, Set<String>> userSessions = new ConcurrentHashMap<>();
    
    public void addUserSession(String sessionId, User user) {
        sessionUsers.put(sessionId, user);
        
        userSessions.computeIfAbsent(user.getUserId(), k -> ConcurrentHashMap.newKeySet())
                   .add(sessionId);
    }
    
    public void removeUserSession(String sessionId) {
        User user = sessionUsers.remove(sessionId);
        sessionChannels.remove(sessionId);
        
        if (user != null) {
            Set<String> sessions = userSessions.get(user.getUserId());
            if (sessions != null) {
                sessions.remove(sessionId);
                if (sessions.isEmpty()) {
                    userSessions.remove(user.getUserId());
                }
            }
        }
    }
    
    public User getUserBySession(String sessionId) {
        return sessionUsers.get(sessionId);
    }
    
    public void addUserToChannel(String sessionId, Long channelId) {
        sessionChannels.computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet())
                      .add(channelId);
    }
    
    public void removeUserFromChannel(String sessionId, Long channelId) {
        Set<Long> channels = sessionChannels.get(sessionId);
        if (channels != null) {
            channels.remove(channelId);
        }
    }
    
    public List<Long> getUserChannels(String sessionId) {
        Set<Long> channels = sessionChannels.get(sessionId);
        return channels != null ? new ArrayList<>(channels) : new ArrayList<>();
    }
    
    public Set<String> getSessionsForUser(Long userId) {
        return userSessions.getOrDefault(userId, Collections.emptySet());
    }
    
    public int getOnlineUserCount() {
        return userSessions.size();
    }
    
    public List<User> getAllOnlineUsers() {
        return new ArrayList<>(new HashSet<>(sessionUsers.values()));
    }
    
    public boolean isUserOnline(Long userId) {
        Set<String> sessions = userSessions.get(userId);
        return sessions != null && !sessions.isEmpty();
    }
    
    public void clearAllSessions() {
        sessionUsers.clear();
        sessionChannels.clear();
        userSessions.clear();
    }
}
