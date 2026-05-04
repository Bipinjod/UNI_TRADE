package com.unitrade.dao;

import com.unitrade.model.Wishlist;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * WishlistDAO - Data Access Object for Wishlist operations
 * Handles all database operations related to wishlists table
 */
public class WishlistDAO {

    /**
     * Add an item to user's wishlist
     * Note: Database has UNIQUE constraint on (user_id, item_id) to prevent duplicates
     *
     * @param userId User ID
     * @param itemId Item ID to add to wishlist
     * @return true if addition is successful, false otherwise
     */
    public boolean addToWishlist(int userId, int itemId) {
        String sql = "INSERT INTO wishlists (user_id, item_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, itemId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Duplicate entry will cause SQLIntegrityConstraintViolationException
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove an item from user's wishlist
     *
     * @param userId User ID
     * @param itemId Item ID to remove from wishlist
     * @return true if removal is successful, false otherwise
     */
    public boolean removeFromWishlist(int userId, int itemId) {
        String sql = "DELETE FROM wishlists WHERE user_id = ? AND item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, itemId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if an item is in user's wishlist
     * Useful for displaying wishlist heart icon as filled/unfilled
     *
     * @param userId User ID
     * @param itemId Item ID to check
     * @return true if item is in wishlist, false otherwise
     */
    public boolean isWishlisted(int userId, int itemId) {
        String sql = "SELECT COUNT(*) FROM wishlists WHERE user_id = ? AND item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, itemId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get all wishlist items for a specific user
     * Includes item details (title, price, image_path) via JOIN
     * Only returns items that are approved and not sold
     *
     * @param userId User ID
     * @return List of wishlist items with item details
     */
    public List<Wishlist> getWishlistByUserId(int userId) {
        List<Wishlist> wishlists = new ArrayList<>();
        String sql = "SELECT w.*, i.title AS item_title, i.price, i.image_path " +
                     "FROM wishlists w " +
                     "LEFT JOIN items i ON w.item_id = i.item_id " +
                     "WHERE w.user_id = ? AND i.listing_status = 'APPROVED' " +
                     "ORDER BY w.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    wishlists.add(mapResultSetToWishlist(rs, true));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wishlists;
    }

    /**
     * Helper method to map ResultSet to Wishlist object
     *
     * @param rs ResultSet from database query
     * @param includeJoinedData Whether to include joined data (item_title, price, image_path)
     * @return Wishlist object with data from ResultSet
     * @throws SQLException if there's an error reading from ResultSet
     */
    private Wishlist mapResultSetToWishlist(ResultSet rs, boolean includeJoinedData) throws SQLException {
        Wishlist wishlist = new Wishlist();

        wishlist.setWishlistId(rs.getInt("wishlist_id"));
        wishlist.setUserId(rs.getInt("user_id"));
        wishlist.setItemId(rs.getInt("item_id"));
        wishlist.setCreatedAt(rs.getTimestamp("created_at"));

        // Set optional joined fields if included
        if (includeJoinedData) {
            try {
                wishlist.setItemTitle(rs.getString("item_title"));
            } catch (SQLException e) {
                // Column might not exist in some queries
            }

            try {
                wishlist.setPrice(rs.getBigDecimal("price"));
            } catch (SQLException e) {
                // Column might not exist in some queries
            }

            try {
                wishlist.setImagePath(rs.getString("image_path"));
            } catch (SQLException e) {
                // Column might not exist in some queries
            }
        }

        return wishlist;
    }
}


