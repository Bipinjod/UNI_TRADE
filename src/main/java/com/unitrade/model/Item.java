package com.unitrade.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Item model class representing the items table.
 * Handles marketplace item listings with optional joined data.
 */
public class Item {

    // Primary Key
    private int itemId;

    // Foreign Keys
    private int userId;
    private int categoryId;

    // Item Information
    private String title;
    private String description;
    private BigDecimal price;
    private String itemCondition; // ENUM: 'NEW', 'LIKE_NEW', 'GOOD', 'USED'
    private String imagePath;
    private String listingStatus; // ENUM: 'PENDING', 'APPROVED', 'REJECTED', 'SOLD', 'INACTIVE'

    // Timestamps
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Optional fields for joined queries
    private String categoryName;
    private String sellerName;

    /**
     * Default constructor
     */
    public Item() {
    }

    /**
     * Parameterized constructor for creating new item (without ID)
     */
    public Item(int userId, int categoryId, String title, String description,
                BigDecimal price, String itemCondition, String imagePath, String listingStatus) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.itemCondition = itemCondition;
        this.imagePath = imagePath;
        this.listingStatus = listingStatus;
    }

    /**
     * Full parameterized constructor (with ID)
     */
    public Item(int itemId, int userId, int categoryId, String title, String description,
                BigDecimal price, String itemCondition, String imagePath, String listingStatus,
                Timestamp createdAt, Timestamp updatedAt) {
        this.itemId = itemId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.itemCondition = itemCondition;
        this.imagePath = imagePath;
        this.listingStatus = listingStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
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

    // Optional fields (for joined queries)

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    /**
     * Convenience method to check if item is approved
     */
    public boolean isApproved() {
        return "APPROVED".equals(this.listingStatus);
    }

    /**
     * Convenience method to check if item is sold
     */
    public boolean isSold() {
        return "SOLD".equals(this.listingStatus);
    }

    /**
     * Convenience method to check if item is available
     */
    public boolean isAvailable() {
        return "APPROVED".equals(this.listingStatus);
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", itemCondition='" + itemCondition + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", listingStatus='" + listingStatus + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", categoryName='" + categoryName + '\'' +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
}


