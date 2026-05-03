package com.unitrade.service;

import com.unitrade.dao.ItemDAO;
import com.unitrade.dao.OrderDAO;
import com.unitrade.model.Item;
import com.unitrade.model.Order;

import java.util.List;

/**
 * OrderService - Business logic layer for Order operations
 * Handles order creation, validation, and status management
 */
public class OrderService {

    private final OrderDAO orderDAO;
    private final ItemDAO itemDAO;

    /**
     * Constructor
     */
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.itemDAO = new ItemDAO();
    }

    /**
     * Create a new order (purchase request) with validation
     *
     * @param order Order object containing order details
     * @return Success or error message
     */
    public String createOrder(Order order) {
        // Validate required fields
        if (order.getItemId() <= 0) {
            return "Invalid item ID";
        }

        if (order.getBuyerId() <= 0) {
            return "Invalid buyer ID";
        }

        if (order.getSellerId() <= 0) {
            return "Invalid seller ID";
        }

        // Buyer cannot be the seller
        if (order.getBuyerId() == order.getSellerId()) {
            return "You cannot purchase your own item";
        }

        // Validate quantity
        if (order.getQuantity() <= 0) {
            return "Quantity must be at least 1";
        }

        if (order.getQuantity() > 10) {
            return "Quantity cannot exceed 10";
        }

        // Validate message length if provided
        if (order.getMessage() != null && order.getMessage().length() > 255) {
            return "Message is too long (max 255 characters)";
        }

        // Check if item exists and is available
        Item item = itemDAO.getItemById(order.getItemId());
        if (item == null) {
            return "Item not found";
        }

        if (!"APPROVED".equals(item.getListingStatus())) {
            return "This item is not available for purchase";
        }

        // Verify seller ID matches item owner
        if (item.getUserId() != order.getSellerId()) {
            return "Invalid seller information";
        }

        // Set default order status if not set
        if (order.getOrderStatus() == null || order.getOrderStatus().isEmpty()) {
            order.setOrderStatus("PENDING");
        }

        // Create order
        boolean success = orderDAO.createOrder(order);

        if (success) {
            return "Order request sent successfully!";
        } else {
            return "Failed to create order. Please try again.";
        }
    }

    /**
     * Get all orders placed by a buyer
     *
     * @param buyerId Buyer's user ID
     * @return List of orders placed by the buyer
     */
    public List<Order> getBuyerOrders(int buyerId) {
        if (buyerId <= 0) {
            return List.of(); // Return empty list
        }
        return orderDAO.getOrdersByBuyerId(buyerId);
    }

    /**
     * Get all orders received by a seller
     *
     * @param sellerId Seller's user ID
     * @return List of orders received by the seller
     */
    public List<Order> getSellerOrders(int sellerId) {
        if (sellerId <= 0) {
            return List.of(); // Return empty list
        }
        return orderDAO.getOrdersBySellerId(sellerId);
    }

    /**
     * Update order status with validation
     *
     * @param orderId Order ID to update
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateOrderStatus(int orderId, String status) {
        if (orderId <= 0) {
            return false;
        }

        if (status == null || status.trim().isEmpty()) {
            return false;
        }

        // Validate status value
        String upperStatus = status.toUpperCase();
        if (!upperStatus.equals("PENDING") && !upperStatus.equals("ACCEPTED") &&
            !upperStatus.equals("REJECTED") && !upperStatus.equals("COMPLETED") &&
            !upperStatus.equals("CANCELLED")) {
            return false;
        }

        return orderDAO.updateOrderStatus(orderId, upperStatus);
    }

    /**
     * Accept an order (seller action)
     *
     * @param orderId Order ID to accept
     * @param userId User ID (must be the seller)
     * @return Success or error message
     */
    public String acceptOrder(int orderId, int userId) {
        if (orderId <= 0 || userId <= 0) {
            return "Invalid order or user ID";
        }

        // Note: In a real application, you would verify the user is the seller
        // For now, we'll just update the status

        boolean success = orderDAO.updateOrderStatus(orderId, "ACCEPTED");
        if (success) {
            return "Order accepted successfully";
        } else {
            return "Failed to accept order";
        }
    }

    /**
     * Reject an order (seller action)
     *
     * @param orderId Order ID to reject
     * @param userId User ID (must be the seller)
     * @return Success or error message
     */
    public String rejectOrder(int orderId, int userId) {
        if (orderId <= 0 || userId <= 0) {
            return "Invalid order or user ID";
        }

        boolean success = orderDAO.updateOrderStatus(orderId, "REJECTED");
        if (success) {
            return "Order rejected";
        } else {
            return "Failed to reject order";
        }
    }

    /**
     * Complete an order (mark as completed)
     *
     * @param orderId Order ID to complete
     * @param userId User ID (seller or buyer)
     * @return Success or error message
     */
    public String completeOrder(int orderId, int userId) {
        if (orderId <= 0 || userId <= 0) {
            return "Invalid order or user ID";
        }

        boolean success = orderDAO.updateOrderStatus(orderId, "COMPLETED");
        if (success) {
            return "Order marked as completed";
        } else {
            return "Failed to complete order";
        }
    }

    /**
     * Cancel an order (buyer or seller action)
     *
     * @param orderId Order ID to cancel
     * @param userId User ID (must be buyer or seller)
     * @return Success or error message
     */
    public String cancelOrder(int orderId, int userId) {
        if (orderId <= 0 || userId <= 0) {
            return "Invalid order or user ID";
        }

        boolean success = orderDAO.updateOrderStatus(orderId, "CANCELLED");
        if (success) {
            return "Order cancelled successfully";
        } else {
            return "Failed to cancel order";
        }
    }

    /**
     * Validate order message
     *
     * @param message Message text
     * @return true if valid, false otherwise
     */
    public boolean validateOrderMessage(String message) {
        if (message == null) {
            return true; // Message is optional
        }
        return message.length() <= 255;
    }

    /**
     * Check if an order can be accepted (must be in PENDING status)
     *
     * @param order Order object
     * @return true if can be accepted, false otherwise
     */
    public boolean canAcceptOrder(Order order) {
        if (order == null) {
            return false;
        }
        return "PENDING".equals(order.getOrderStatus());
    }

    /**
     * Check if an order can be completed (must be in ACCEPTED status)
     *
     * @param order Order object
     * @return true if can be completed, false otherwise
     */
    public boolean canCompleteOrder(Order order) {
        if (order == null) {
            return false;
        }
        return "ACCEPTED".equals(order.getOrderStatus());
    }

    /**
     * Check if an order can be cancelled (must be PENDING or ACCEPTED)
     *
     * @param order Order object
     * @return true if can be cancelled, false otherwise
     */
    public boolean canCancelOrder(Order order) {
        if (order == null) {
            return false;
        }
        String status = order.getOrderStatus();
        return "PENDING".equals(status) || "ACCEPTED".equals(status);
    }

    /**
     * Get order status display text
     *
     * @param status Order status
     * @return Display-friendly status text
     */
    public String getOrderStatusDisplay(String status) {
        if (status == null) {
            return "Unknown";
        }

        switch (status.toUpperCase()) {
            case "PENDING":
                return "Pending";
            case "ACCEPTED":
                return "Accepted";
            case "REJECTED":
                return "Rejected";
            case "COMPLETED":
                return "Completed";
            case "CANCELLED":
                return "Cancelled";
            default:
                return status;
        }
    }

    /**
     * Get order status CSS class for display
     *
     * @param status Order status
     * @return CSS class name
     */
    public String getOrderStatusClass(String status) {
        if (status == null) {
            return "badge-secondary";
        }

        switch (status.toUpperCase()) {
            case "PENDING":
                return "badge-warning";
            case "ACCEPTED":
                return "badge-primary";
            case "REJECTED":
                return "badge-danger";
            case "COMPLETED":
                return "badge-success";
            case "CANCELLED":
                return "badge-secondary";
            default:
                return "badge-secondary";
        }
    }
}


