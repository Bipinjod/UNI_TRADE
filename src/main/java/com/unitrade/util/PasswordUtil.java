package com.unitrade.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordUtil - Utility class for password hashing and verification.
 * Uses BCrypt (jBCrypt library) for secure one-way password hashing.
 */
public class PasswordUtil {

    // BCrypt log rounds (cost factor) — 12 is a good balance of security and speed
    private static final int LOG_ROUNDS = 12;

    /**
     * Hash a plain-text password using BCrypt.
     *
     * @param plainPassword the raw password entered by the user
     * @return the hashed password string (includes salt)
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * Verify a plain-text password against a BCrypt hash.
     *
     * @param plainPassword the raw password entered by the user
     * @param hashedPassword the stored BCrypt hash from the database
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Invalid hash format
            return false;
        }
    }
}

