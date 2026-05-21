package com.unitrade.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Service model – represents a row in the services table.
 * A Service is a skill or task that a student offers to peers (e.g. tutoring, design work).
 * Approval by an admin is required before a service becomes publicly visible.
 */
public class Service {

    // ── Primary Key ──────────────────────────────────────────────────────────
    private int serviceId;

    // ── Foreign Keys ─────────────────────────────────────────────────────────
    /** ID of the student (provider) who offers this service. */
    private int userId;
    /** ID of the category this service belongs to. */
    private int categoryId;

    // ── Core Fields ───────────────────────────────────────────────────────────
    private String title;
    private String description;
    private BigDecimal price;
    /** Whether the provider is currently taking orders. ENUM: AVAILABLE, UNAVAILABLE */
    private String availabilityStatus;
    /** Admin moderation status. ENUM: PENDING, APPROVED, REJECTED */
    private String approvalStatus;

    // ── Timestamps ────────────────────────────────────────────────────────────
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ── Joined Display Fields (not stored in DB) ──────────────────────────────
    /** Resolved from the categories table via JOIN. */
    private String categoryName;
    /** Provider's full name resolved from the users table via JOIN. */
    private String providerName;

    /** Default no-arg constructor. */
    public Service() {}

    // ── Getters / Setters ───────────────────────────────────��─────────────────

    /** @return Primary key of this service. */
    public int getServiceId() { return serviceId; }
    /** @param serviceId Primary key to set. */
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    /** @return ID of the user/provider who owns this service. */
    public int getUserId() { return userId; }
    /** @param userId Owner's user ID. */
    public void setUserId(int userId) { this.userId = userId; }

    /** @return ID of the associated category. */
    public int getCategoryId() { return categoryId; }
    /** @param categoryId Category foreign key. */
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    /** @return Service title/name. */
    public String getTitle() { return title; }
    /** @param title Service title. */
    public void setTitle(String title) { this.title = title; }

    /** @return Detailed description of the service. */
    public String getDescription() { return description; }
    /** @param description Service description. */
    public void setDescription(String description) { this.description = description; }

    /** @return Asking price for the service. */
    public BigDecimal getPrice() { return price; }
    /** @param price Service price. */
    public void setPrice(BigDecimal price) { this.price = price; }

    /** @return Availability status (AVAILABLE or UNAVAILABLE). */
    public String getAvailabilityStatus() { return availabilityStatus; }
    /** @param availabilityStatus New availability status. */
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    /** @return Admin approval status (PENDING, APPROVED, REJECTED). */
    public String getApprovalStatus() { return approvalStatus; }
    /** @param approvalStatus New approval status. */
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    /** @return Timestamp when this record was created. */
    public Timestamp getCreatedAt() { return createdAt; }
    /** @param createdAt Creation timestamp. */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    /** @return Timestamp of the last update to this record. */
    public Timestamp getUpdatedAt() { return updatedAt; }
    /** @param updatedAt Last-updated timestamp. */
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    /** @return Category name resolved via JOIN (may be null if not joined). */
    public String getCategoryName() { return categoryName; }
    /** @param categoryName Joined category name. */
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    /** @return Provider's full name resolved via JOIN (may be null if not joined). */
    public String getProviderName() { return providerName; }
    /** @param providerName Joined provider name. */
    public void setProviderName(String providerName) { this.providerName = providerName; }

    // ── Convenience methods ───────────────────────────────────────────────────

    /**
     * @return true if this service has been approved by an admin.
     */
    public boolean isApproved() { return "APPROVED".equals(approvalStatus); }

    /**
     * @return true if the provider is currently accepting orders for this service.
     */
    public boolean isAvailable() { return "AVAILABLE".equals(availabilityStatus); }
}
