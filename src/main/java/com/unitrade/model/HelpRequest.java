package com.unitrade.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * HelpRequest model – represents a row in the help_requests table.
 * A HelpRequest is posted by a student who needs assistance from peers
 * (e.g. tutoring, project help). Requires admin approval before becoming visible.
 */
public class HelpRequest {

    // ── Primary Key ───────────────────────────────────────────────────────────
    private int requestId;

    // ── Foreign Keys ─────────────────────────────────────────────────────────
    /** ID of the student who posted this request. */
    private int userId;
    /** ID of the category this request belongs to. */
    private int categoryId;

    // ��─ Request Details ───────────────────────────────────────────────────────
    private String title;
    private String description;
    /** Maximum budget the requester is willing to pay. */
    private BigDecimal budget;
    /** How urgent this request is. ENUM: LOW, MEDIUM, HIGH */
    private String urgencyLevel;
    /** Lifecycle status of the request. ENUM: OPEN, CLOSED, FULFILLED */
    private String requestStatus;
    /** Admin moderation status. ENUM: PENDING, APPROVED, REJECTED */
    private String approvalStatus;

    // ── Timestamps ────────────────────────────────────────────────────────────
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ── Joined / Computed Display Fields (not stored in DB) ───────────────────
    /** Category name resolved from the categories table via JOIN. */
    private String categoryName;
    /** Full name of the poster resolved from the users table via JOIN. */
    private String posterName;
    /** Total number of responses submitted for this request (computed). */
    private int responseCount;

    /** Default no-arg constructor. */
    public HelpRequest() {}

    // ── Getters / Setters ─────────────────────────────────────────────────────

    /** @return Primary key of this help request. */
    public int getRequestId() { return requestId; }
    /** @param requestId Primary key to set. */
    public void setRequestId(int requestId) { this.requestId = requestId; }

    /** @return ID of the user who posted this request. */
    public int getUserId() { return userId; }
    /** @param userId Poster's user ID. */
    public void setUserId(int userId) { this.userId = userId; }

    /** @return ID of the associated category. */
    public int getCategoryId() { return categoryId; }
    /** @param categoryId Category foreign key. */
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    /** @return Short title of the help request. */
    public String getTitle() { return title; }
    /** @param title Request title. */
    public void setTitle(String title) { this.title = title; }

    /** @return Detailed description of what help is needed. */
    public String getDescription() { return description; }
    /** @param description Request description. */
    public void setDescription(String description) { this.description = description; }

    /** @return Maximum budget offered by the requester. */
    public BigDecimal getBudget() { return budget; }
    /** @param budget Offered budget amount. */
    public void setBudget(BigDecimal budget) { this.budget = budget; }

    /** @return Urgency level (LOW, MEDIUM, HIGH). */
    public String getUrgencyLevel() { return urgencyLevel; }
    /** @param urgencyLevel Urgency level. */
    public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    /** @return Current status of the request (OPEN, CLOSED, FULFILLED). */
    public String getRequestStatus() { return requestStatus; }
    /** @param requestStatus New request status. */
    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }

    /** @return Admin moderation status (PENDING, APPROVED, REJECTED). */
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

    /** @return Poster's full name resolved via JOIN (may be null if not joined). */
    public String getPosterName() { return posterName; }
    /** @param posterName Joined poster name. */
    public void setPosterName(String posterName) { this.posterName = posterName; }

    /** @return Total number of responses submitted for this request. */
    public int getResponseCount() { return responseCount; }
    /** @param responseCount Computed response count. */
    public void setResponseCount(int responseCount) { this.responseCount = responseCount; }
}
