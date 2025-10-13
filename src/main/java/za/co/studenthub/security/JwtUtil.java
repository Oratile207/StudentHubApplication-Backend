package za.co.studenthub.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("=== JWT VALIDATION ===");
            System.out.println("Validating token: " + token.substring(0, 20) + "...");
            System.out.println("JWT Secret: " + secret);
            System.out.println("JWT Expiration: " + expiration);
            
            var claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
                    
            System.out.println("Token validation SUCCESS");
            System.out.println("Subject: " + claims.getPayload().getSubject());
            System.out.println("Issued At: " + claims.getPayload().getIssuedAt());
            System.out.println("Expires At: " + claims.getPayload().getExpiration());
            
            return true;
        } catch (Exception e) {
            System.out.println("Token validation FAILED: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
