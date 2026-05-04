package com.unitrade.model;

import java.sql.Timestamp;

/**
 * Order model class representing the item_orders table.
 * Handles purchase requests/orders with optional joined data.
 */
public class Order {

    // Primary Key
    private int orderId;

    // Foreign Keys
    private int itemId;
    private int buyerId;
    private int sellerId;

    // Order Information
    private int quantity;
    private String message;
    private String orderStatus; // ENUM: 'PENDING', 'ACCEPTED', 'REJECTED', 'COMPLETED', 'CANCELLED'

    // Timestamps
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Optional fields for joined queries
    private String itemTitle;
    private String buyerName;
    private String sellerName;

    /**
     * Default constructor
     */
    public Order() {
    }

    /**
     * Parameterized constructor for creating new order (without ID)
     */
    public Order(int itemId, int buyerId, int sellerId, int quantity,
                 String message, String orderStatus) {
        this.itemId = itemId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.message = message;
        this.orderStatus = orderStatus;
    }

    /**
     * Full parameterized constructor (with ID)
     */
    public Order(int orderId, int itemId, int buyerId, int sellerId, int quantity,
                 String message, String orderStatus, Timestamp createdAt, Timestamp updatedAt) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.message = message;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    /**
     * Convenience method to check if order is pending
     */
    public boolean isPending() {
        return "PENDING".equals(this.orderStatus);
    }

    /**
     * Convenience method to check if order is accepted
     */
    public boolean isAccepted() {
        return "ACCEPTED".equals(this.orderStatus);
    }

    /**
     * Convenience method to check if order is completed
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(this.orderStatus);
    }

    /**
     * Convenience method to check if order is cancelled
     */
    public boolean isCancelled() {
        return "CANCELLED".equals(this.orderStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", itemId=" + itemId +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", quantity=" + quantity +
                ", message='" + message + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", itemTitle='" + itemTitle + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
}


