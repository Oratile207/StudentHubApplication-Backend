package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.User;
import za.co.studenthub.domain.enums.UserRole;
import za.co.studenthub.factory.UserFactory;
import za.co.studenthub.security.JwtUtil;
import za.co.studenthub.services.user_services.user.UserService;

import java.util.List;

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
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            if (userService.findByUserEmail(user.getUserEmail()) != null) { // Updated to findByUserEmail
                return ResponseEntity.badRequest().body("Email already exists");
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
            userService.create(newUser);
            String token = jwtUtil.generateToken(newUser.getUserEmail());
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            User existingUser = userService.findByUserEmail(user.getUserEmail()); // Updated to findByUserEmail
            if (existingUser != null && passwordEncoder.matches(user.getUserPassword(), existingUser.getUserPassword())) {
                String token = jwtUtil.generateToken(existingUser.getUserEmail());
                return ResponseEntity.ok(token);
            }
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error");
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