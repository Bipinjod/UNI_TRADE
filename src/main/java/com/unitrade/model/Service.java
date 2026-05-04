package com.unitrade.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Service model – represents the services table (peer services).
 */
public class Service {

    private int serviceId;
    private int userId;
    private int categoryId;
    private String title;
    private String description;
    private BigDecimal price;
    private String availabilityStatus; // AVAILABLE, UNAVAILABLE
    private String approvalStatus;     // PENDING, APPROVED, REJECTED
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // joined helpers
    private String categoryName;
    private String providerName;

    public Service() {}

    // ── getters / setters ──

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    public boolean isApproved() { return "APPROVED".equals(approvalStatus); }
    public boolean isAvailable() { return "AVAILABLE".equals(availabilityStatus); }
}

