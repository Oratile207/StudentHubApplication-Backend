package za.co.studenthub.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtHandshakeInterceptor.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        System.out.println("=== JWT HANDSHAKE INTERCEPTOR CALLED ===");
        System.out.println("WebSocket handshake attempt for URI: " + request.getURI());
        logger.info("WebSocket handshake attempt for URI: {}", request.getURI());
        
        // Extract token from query parameter
        URI uri = request.getURI();
        String token = UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .getFirst("token");

        System.out.println("Extracted token: " + (token != null ? "present (" + token.substring(0, 20) + "...)" : "null"));
        logger.info("Extracted token: {}", token != null ? "present" : "null");

        if (token != null) {
            boolean isValid = jwtUtil.validateToken(token);
            System.out.println("Token validation result: " + isValid);
            
            if (isValid) {
                String username = jwtUtil.extractEmail(token);
                attributes.put("username", username);
                attributes.put("token", token);
                System.out.println("WebSocket handshake successful for user: " + username);
                logger.info("WebSocket handshake successful for user: {}", username);
                return true;
            } else {
                System.out.println("Token validation failed");
                logger.warn("Token validation failed");
            }
        }
        
        System.out.println("WebSocket handshake REJECTED - invalid or missing token");
        logger.warn("WebSocket handshake failed - invalid or missing token");
        return false;
    }

    @SuppressWarnings("null")
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // No additional processing needed
    }
}
