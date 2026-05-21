package com.unitrade.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Wishlist model class representing the wishlists table.
 * Handles user's saved/favorite items with optional joined data.
 */
public class Wishlist {

    // Primary Key
    private int wishlistId;

    // Foreign Keys
    private int userId;
    private int itemId;

    // Timestamp
    private Timestamp createdAt;

    // Optional fields for joined queries
    private String itemTitle;
    private BigDecimal price;
    private String imagePath;
    private String listingStatus;

    /**
     * Default constructor
     */
    public Wishlist() {
    }

    /**
     * Parameterized constructor for creating new wishlist entry (without ID)
     */
    public Wishlist(int userId, int itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    /**
     * Full parameterized constructor (with ID)
     */
    public Wishlist(int wishlistId, int userId, int itemId, Timestamp createdAt) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.itemId = itemId;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Optional fields (for joined queries)

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    @Override
    public String toString() {
        return "Wishlist{" +
                "wishlistId=" + wishlistId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", createdAt=" + createdAt +
                ", itemTitle='" + itemTitle + '\'' +
                ", price=" + price +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}


