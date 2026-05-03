package com.unitrade.util;

import java.util.regex.Pattern;

/**
 * ValidationUtil - Utility class for input validation.
 * Provides reusable validation methods for email, phone, and required fields.
 */
public class ValidationUtil {

    // Email regex pattern (RFC 5322 simplified)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Phone regex — allows digits, optional + prefix, 7–15 digits
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\+?[0-9]{7,15}$"
    );

    /**
     * Check if a string is null or blank (empty / whitespace only).
     *
     * @param value the string to check
     * @return true if null or blank
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validate email format.
     *
     * @param email the email string
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number format.
     *
     * @param phone the phone string
     * @return true if valid phone format
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        // Strip spaces and dashes for validation
        String cleaned = phone.trim().replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(cleaned).matches();
    }

    /**
     * Check minimum length of a string.
     *
     * @param value the string to check
     * @param minLength minimum required length
     * @return true if value is at least minLength characters
     */
    public static boolean hasMinLength(String value, int minLength) {
        if (isEmpty(value)) {
            return false;
        }
        return value.trim().length() >= minLength;
    }

    /**
     * Check if a string exceeds maximum length.
     *
     * @param value the string to check
     * @param maxLength maximum allowed length
     * @return true if value length is within the max
     */
    public static boolean isWithinMaxLength(String value, int maxLength) {
        if (value == null) {
            return true;
        }
        return value.trim().length() <= maxLength;
    }

    /**
     * Validate that a string can be parsed as a positive number.
     *
     * @param value the string to check
     * @return true if value is a valid positive number
     */
    public static boolean isValidPrice(String value) {
        if (isEmpty(value)) {
            return false;
        }
        try {
            double price = Double.parseDouble(value.trim());
            return price >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

