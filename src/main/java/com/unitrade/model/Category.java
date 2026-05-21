package com.unitrade.model;

import java.sql.Timestamp;

/**
 * Category model class representing the categories table.
 * Supports ITEM, SERVICE, and REQUEST category types.
 */
public class Category {

    // Primary Key
    private int categoryId;

    // Category Information
    private String categoryName;
    private String categoryType; // ENUM: 'ITEM','SERVICE','REQUEST'
    private String description;
    private String status; // ENUM: 'ACTIVE','INACTIVE'

    // Timestamp
    private Timestamp createdAt;

    /**
     * Default constructor
     */
    public Category() {
    }

    /**
     * Parameterized constructor for creating new category (without ID)
     */
    public Category(String categoryName, String categoryType, String description, String status) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.description = description;
        this.status = status;
    }

    /**
     * Full parameterized constructor (with ID)
     */
    public Category(int categoryId, String categoryName, String categoryType, String description,
                    String status, Timestamp createdAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Convenience method to check if category is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryType='" + categoryType + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
