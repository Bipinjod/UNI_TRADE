package com.unitrade.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * RememberToken model class representing the remember_tokens table.
 * Handles "Remember Me" functionality for persistent login sessions.
 */
public class RememberToken {

    // Primary Key
    private int tokenId;

    // Foreign Key
    private int userId;

    // Token Information
    private String selector;
    private String validatorHash;
    private LocalDateTime expiresAt;

    // Timestamp
    private Timestamp createdAt;

    /**
     * Default constructor
     */
    public RememberToken() {
    }

    /**
     * Parameterized constructor for creating new token (without ID)
     */
    public RememberToken(int userId, String selector, String validatorHash, LocalDateTime expiresAt) {
        this.userId = userId;
        this.selector = selector;
        this.validatorHash = validatorHash;
        this.expiresAt = expiresAt;
    }

    /**
     * Full parameterized constructor (with ID)
     */
    public RememberToken(int tokenId, int userId, String selector, String validatorHash,
                         LocalDateTime expiresAt, Timestamp createdAt) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.selector = selector;
        this.validatorHash = validatorHash;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getValidatorHash() {
        return validatorHash;
    }

    public void setValidatorHash(String validatorHash) {
        this.validatorHash = validatorHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Convenience method to check if token is expired
     */
    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    /**
     * Convenience method to check if token is valid (not expired)
     */
    public boolean isValid() {
        return !isExpired();
    }

    @Override
    public String toString() {
        return "RememberToken{" +
                "tokenId=" + tokenId +
                ", userId=" + userId +
                ", selector='" + selector + '\'' +
                ", validatorHash='" + validatorHash + '\'' +
                ", expiresAt=" + expiresAt +
                ", createdAt=" + createdAt +
                '}';
    }
}


