package util;

/**
 * Utility class for input validation
 */
public class ValidationUtil {
    
    /**
     * Validate name input - only letters, spaces, and hyphens allowed
     * @param name The name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Allow only letters, spaces, hyphens, and apostrophes
        return name.matches("^[a-zA-Z\\s'-]+$");
    }
    
    /**
     * Validate phone number - only digits, spaces, hyphens, and parentheses
     * @param phone The phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Phone is optional
        }
        // Allow only digits, spaces, hyphens, parentheses, and plus sign
        return phone.matches("^[0-9\\s()+-]+$");
    }
    
    /**
     * Validate email format
     * @param email The email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Email is optional
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
