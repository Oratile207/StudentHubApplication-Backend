package za.co.studenthub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;  // Can be mapped to userEmail
    private String password;  // Maps to userPassword
    private String userEmail; // For backward compatibility
    private String userPassword; // For backward compatibility

    // Utility method to get email from either field
    public String getEmailField() {
        return userEmail != null ? userEmail : username;
    }

    // Utility method to get password from either field
    public String getPasswordField() {
        return userPassword != null ? userPassword : password;
    }
}
