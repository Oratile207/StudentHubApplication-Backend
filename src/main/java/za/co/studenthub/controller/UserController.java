package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.dto.AuthResponse;
import za.co.studenthub.dto.LoginRequest;
import za.co.studenthub.factory.UserFactory;
import za.co.studenthub.security.JwtUtil;
import za.co.studenthub.services.user_services.user.UserService;
import za.co.studenthub.util.UserResponseFormatter;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if (userService.findByUserEmail(user.getUserEmail()) != null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Email already exists");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            UserRole role = user.getUserRole() != null ? user.getUserRole() : UserRole.STUDENT;
            User newUser = UserFactory.createUser(
                    role,
                    user.getUserFirstName(),
                    user.getUserLastName(),
                    user.getUserEmail(),
                    passwordEncoder.encode(user.getUserPassword()),
                    user.getStudentNumber(),
                    user.getStaffNumber(),
                    user.getEntrepreneurProfile() != null ? user.getEntrepreneurProfile().getBiography() : null,
                    user.getEntrepreneurProfile() != null && user.getEntrepreneurProfile().isCommercePortfolioEnabled(),
                    user.getEntrepreneurProfile() != null ? user.getEntrepreneurProfile().getSessionUrl() : null
            );
            User savedUser = userService.create(newUser);
            String token = jwtUtil.generateToken(savedUser.getUserEmail());
            
            // Return both token and user object
            AuthResponse response = UserResponseFormatter.createAuthResponse(token, savedUser);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmailField();
            String password = loginRequest.getPasswordField();
            
            if (email == null || password == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Email and password are required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            User existingUser = userService.findByUserEmail(email);
            if (existingUser != null && passwordEncoder.matches(password, existingUser.getUserPassword())) {
                // Update last seen when user logs in
                existingUser.setLastSeen(java.time.LocalDateTime.now());
                existingUser.setOnline(true);
                userService.update(existingUser);
                
                String token = jwtUtil.generateToken(existingUser.getUserEmail());
                
                // Return both token and user object
                AuthResponse response = UserResponseFormatter.createAuthResponse(token, existingUser);
                return ResponseEntity.ok(response);
            }
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Server error");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Other methods remain unchanged
    @GetMapping("/get/{id}")
    public ResponseEntity<User> read(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            User user = userService.read(id);
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user, @RequestHeader("Authorization") String token) {
        try {
            User updatedUser = userService.update(user);
            return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll(@RequestHeader("Authorization") String token) {
        try {
            List<User> users = userService.getAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}