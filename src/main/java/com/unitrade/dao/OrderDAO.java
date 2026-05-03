package com.unitrade.dao;

import com.unitrade.model.Order;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDAO - Data Access Object for Order operations
 * Handles all database operations related to item_orders table
 */
public class OrderDAO {

    /**
     * Create a new order (purchase request) in the database
     *
     * @param order Order object containing order details
     * @return true if creation is successful, false otherwise
     */
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO item_orders (item_id, buyer_id, seller_id, quantity, message, order_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getItemId());
            stmt.setInt(2, order.getBuyerId());
            stmt.setInt(3, order.getSellerId());
            stmt.setInt(4, order.getQuantity());
            stmt.setString(5, order.getMessage());
            stmt.setString(6, order.getOrderStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all orders placed by a specific buyer
     * Includes item title, seller name via JOIN
     *
     * @param buyerId Buyer's user ID
     * @return List of orders placed by the buyer
     */
    public List<Order> getOrdersByBuyerId(int buyerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, i.title AS item_title, " +
                     "u_seller.full_name AS seller_name, " +
                     "u_buyer.full_name AS buyer_name " +
                     "FROM item_orders o " +
                     "LEFT JOIN items i ON o.item_id = i.item_id " +
                     "LEFT JOIN users u_seller ON o.seller_id = u_seller.user_id " +
                     "LEFT JOIN users u_buyer ON o.buyer_id = u_buyer.user_id " +
                     "WHERE o.buyer_id = ? " +
                     "ORDER BY o.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, buyerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs, true));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    /**
     * Get all orders received by a specific seller
     * Includes item title, buyer name via JOIN
     *
     * @param sellerId Seller's user ID
     * @return List of orders received by the seller
     */
    public List<Order> getOrdersBySellerId(int sellerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, i.title AS item_title, " +
                     "u_seller.full_name AS seller_name, " +
                     "u_buyer.full_name AS buyer_name " +
                     "FROM item_orders o " +
                     "LEFT JOIN items i ON o.item_id = i.item_id " +
                     "LEFT JOIN users u_seller ON o.seller_id = u_seller.user_id " +
                     "LEFT JOIN users u_buyer ON o.buyer_id = u_buyer.user_id " +
                     "WHERE o.seller_id = ? " +
                     "ORDER BY o.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sellerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs, true));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    /**
     * Update order status
     * Used by seller to accept/reject or by either party to complete/cancel
     *
     * @param orderId Order ID to update
     * @param status New order status (ACCEPTED, REJECTED, COMPLETED, CANCELLED)
     * @return true if update is successful, false otherwise
     */
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE item_orders SET order_status = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper method to map ResultSet to Order object
     *
     * @param rs ResultSet from database query
     * @param includeJoinedData Whether to include joined data (item_title, buyer_name, seller_name)
     * @return Order object with data from ResultSet
     * @throws SQLException if there's an error reading from ResultSet
     */
    private Order mapResultSetToOrder(ResultSet rs, boolean includeJoinedData) throws SQLException {
        Order order = new Order();

        order.setOrderId(rs.getInt("order_id"));
        order.setItemId(rs.getInt("item_id"));
        order.setBuyerId(rs.getInt("buyer_id"));
        order.setSellerId(rs.getInt("seller_id"));
        order.setQuantity(rs.getInt("quantity"));
        order.setMessage(rs.getString("message"));
        order.setOrderStatus(rs.getString("order_status"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Set optional joined fields if included
        if (includeJoinedData) {
            try {
                order.setItemTitle(rs.getString("item_title"));
            } catch (SQLException e) {
                // Column might not exist in some queries
            }

            try {
                order.setBuyerName(rs.getString("buyer_name"));
            } catch (SQLException e) {
                // Column might not exist in some queries
            }

            try {
                order.setSellerName(rs.getString("seller_name"));
            } catch (SQLException e) {
                // Column might not exist in some queries
            }
        }

        return order;
    }
}


