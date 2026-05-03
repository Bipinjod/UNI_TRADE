package com.unitrade.service;

import com.unitrade.dao.UserDAO;
import com.unitrade.model.User;
import com.unitrade.util.PasswordUtil;
import com.unitrade.util.ValidationUtil;

import java.util.List;

/**
 * UserService - Business logic layer for User operations
 * Handles registration, authentication, approval workflow, and profile management
 */
public class UserService {

    private final UserDAO userDAO;

    /**
     * Constructor
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register a new user with validation and password hashing
     *
     * @param user User object with registration details
     * @param confirmPassword Password confirmation for validation
     * @return Success message or error message
     */
    public String registerUser(User user, String confirmPassword) {
        // Validate required fields
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Full name is required";
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "Email is required";
        }

        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return "Invalid email format";
        }

        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            return "Phone number is required";
        }

        if (!ValidationUtil.isValidPhone(user.getPhone())) {
            return "Invalid phone number format";
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty()) {
            return "Password is required";
        }

        if (user.getPasswordHash().length() < 6) {
            return "Password must be at least 6 characters";
        }

        if (confirmPassword == null || !user.getPasswordHash().equals(confirmPassword)) {
            return "Passwords do not match";
        }

        if (user.getCollegeName() == null || user.getCollegeName().trim().isEmpty()) {
            return "College name is required";
        }

        if (user.getCourseName() == null || user.getCourseName().trim().isEmpty()) {
            return "Course name is required";
        }

        if (user.getAcademicYear() == null || user.getAcademicYear().trim().isEmpty()) {
            return "Academic year is required";
        }

        // Check for duplicate email
        if (userDAO.emailExists(user.getEmail())) {
            return "Email is already registered";
        }

        // Check for duplicate phone
        if (userDAO.phoneExists(user.getPhone())) {
            return "Phone number is already registered";
        }

        // Hash the password
        String plainPassword = user.getPasswordHash();
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        user.setPasswordHash(hashedPassword);

        // Set default values
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        if (user.getApprovalStatus() == null || user.getApprovalStatus().isEmpty()) {
            user.setApprovalStatus("PENDING");
        }
        if (user.getAccountStatus() == null || user.getAccountStatus().isEmpty()) {
            user.setAccountStatus("ACTIVE");
        }

        // Register user
        boolean success = userDAO.registerUser(user);

        if (success) {
            return "Registration successful! Please wait for admin approval.";
        } else {
            return "Registration failed. Please try again.";
        }
    }

    /**
     * Authenticate user login with email and password
     *
     * @param email User's email
     * @param password User's plain text password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticateUser(String email, String password) {
        // Validate inputs
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        // Get user by email
        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            return null; // User not found
        }

        // Verify password
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return null; // Password incorrect
        }

        // Check approval status
        if (!"APPROVED".equals(user.getApprovalStatus())) {
            return null; // User not approved yet
        }

        // Check account status
        if (!"ACTIVE".equals(user.getAccountStatus())) {
            return null; // Account not active
        }

        return user; // Authentication successful
    }

    /**
     * Check if user can login (for detailed error messages)
     *
     * @param email User's email
     * @param password User's plain text password
     * @return Specific error message or "SUCCESS"
     */
    public String checkLoginStatus(String email, String password) {
        // Validate inputs
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }

        if (password == null || password.trim().isEmpty()) {
            return "Password is required";
        }

        // Get user by email
        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            return "Invalid email or password";
        }

        // Verify password
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return "Invalid email or password";
        }

        // Check approval status
        if ("PENDING".equals(user.getApprovalStatus())) {
            return "Your account is pending admin approval";
        }

        if ("REJECTED".equals(user.getApprovalStatus())) {
            return "Your account has been rejected";
        }

        // Check account status
        if ("BLOCKED".equals(user.getAccountStatus())) {
            return "Your account has been blocked";
        }

        if ("INACTIVE".equals(user.getAccountStatus())) {
            return "Your account is inactive";
        }

        return "SUCCESS";
    }

    /**
     * Update user profile information
     *
     * @param user User object with updated information
     * @return Success or error message
     */
    public String updateProfile(User user) {
        // Validate required fields
        if (user.getUserId() <= 0) {
            return "Invalid user ID";
        }

        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Full name is required";
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "Email is required";
        }

        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return "Invalid email format";
        }

        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            return "Phone number is required";
        }

        if (!ValidationUtil.isValidPhone(user.getPhone())) {
            return "Invalid phone number format";
        }

        if (user.getCollegeName() == null || user.getCollegeName().trim().isEmpty()) {
            return "College name is required";
        }

        if (user.getCourseName() == null || user.getCourseName().trim().isEmpty()) {
            return "Course name is required";
        }

        if (user.getAcademicYear() == null || user.getAcademicYear().trim().isEmpty()) {
            return "Academic year is required";
        }

        // Check if email changed and if new email already exists
        User existingUser = userDAO.getUserById(user.getUserId());
        if (existingUser != null && !existingUser.getEmail().equals(user.getEmail())) {
            if (userDAO.emailExists(user.getEmail())) {
                return "Email is already registered";
            }
        }

        // Check if phone changed and if new phone already exists
        if (existingUser != null && !existingUser.getPhone().equals(user.getPhone())) {
            if (userDAO.phoneExists(user.getPhone())) {
                return "Phone number is already registered";
            }
        }

        // Update profile
        boolean success = userDAO.updateUser(user);

        if (success) {
            return "Profile updated successfully";
        } else {
            return "Failed to update profile";
        }
    }

    /**
     * Persist a new BCrypt-hashed password for the given user.
     *
     * @param userId         the user whose password to update
     * @param hashedPassword BCrypt hash of the new password
     * @return true if the DB row was updated
     */
    public boolean updatePassword(int userId, String hashedPassword) {
        return userDAO.updatePassword(userId, hashedPassword);
    }

    /**
     * Get all users with pending approval status
     *
     * @return List of pending users
     */
    public List<User> getPendingUsers() {
        return userDAO.getPendingUsers();
    }

    /**
     * Approve a user (admin action)
     *
     * @param userId User ID to approve
     * @return true if successful, false otherwise
     */
    public boolean approveUser(int userId) {
        return userDAO.updateUserApprovalStatus(userId, "APPROVED");
    }

    /**
     * Reject a user (admin action)
     *
     * @param userId User ID to reject
     * @return true if successful, false otherwise
     */
    public boolean rejectUser(int userId) {
        return userDAO.updateUserApprovalStatus(userId, "REJECTED");
    }

    /**
     * Get all users (admin view)
     *
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Get user by ID
     *
     * @param userId User ID
     * @return User object or null
     */
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    /**
     * Block a user account (admin action)
     *
     * @param userId User ID to block
     * @return true if successful, false otherwise
     */
    public boolean blockUser(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            user.setAccountStatus("BLOCKED");
            return userDAO.updateUser(user);
        }
        return false;
    }

    /**
     * Activate a user account (admin action)
     *
     * @param userId User ID to activate
     * @return true if successful, false otherwise
     */
    public boolean activateUser(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            user.setAccountStatus("ACTIVE");
            return userDAO.updateUser(user);
        }
        return false;
    }
}


