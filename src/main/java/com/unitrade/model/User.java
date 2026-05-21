package com.unitrade.model;

import java.sql.Timestamp;

/**
 * User model class representing the users table.
 * Handles user account information including authentication, approval workflow, and profile details.
 */
public class User {

    // Primary Key
    private int userId;

    // User Information
    private String fullName;
    private String email;
    private String phone;
    private String passwordHash;

    // Role and Status
    private String role; // ENUM: 'ADMIN', 'USER'
    private String approvalStatus; // ENUM: 'PENDING', 'APPROVED', 'REJECTED'
    private String accountStatus; // ENUM: 'ACTIVE', 'INACTIVE', 'BLOCKED'

    // Academic Information
    private String collegeName;
    private String courseName;
    private String academicYear;

    // Profile
    private String profileImage;

    // Timestamps
    private Timestamp createdAt;
    private Timestamp updatedAt;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Parameterized constructor for creating new user (without ID)
     */
    public User(String fullName, String email, String phone, String passwordHash,
                String role, String approvalStatus, String accountStatus,
                String collegeName, String courseName, String academicYear) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
        this.approvalStatus = approvalStatus;
        this.accountStatus = accountStatus;
        this.collegeName = collegeName;
        this.courseName = courseName;
        this.academicYear = academicYear;
    }

    /**
     * Full parameterized constructor (with ID)
     */
    public User(int userId, String fullName, String email, String phone, String passwordHash,
                String role, String approvalStatus, String accountStatus,
                String collegeName, String courseName, String academicYear,
                String profileImage, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
        this.approvalStatus = approvalStatus;
        this.accountStatus = accountStatus;
        this.collegeName = collegeName;
        this.courseName = courseName;
        this.academicYear = academicYear;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Convenience method to check if user is admin
     */
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }

    /**
     * Convenience method to check if user is approved
     */
    public boolean isApproved() {
        return "APPROVED".equals(this.approvalStatus);
    }

    /**
     * Convenience method to check if account is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.accountStatus);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}


