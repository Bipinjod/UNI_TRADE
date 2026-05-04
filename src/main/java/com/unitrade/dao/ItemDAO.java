package com.unitrade.dao;

import com.unitrade.model.Item;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ItemDAO - Data Access Object for Item operations
 * Handles all database operations related to items table
 */
public class ItemDAO {

    /**
     * Add a new item to the database
     *
     * @param item Item object to add
     * @return true if addition is successful, false otherwise
     */
    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (user_id, category_id, title, description, price, " +
                     "item_condition, image_path, listing_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getUserId());
            stmt.setInt(2, item.getCategoryId());
            stmt.setString(3, item.getTitle());
            stmt.setString(4, item.getDescription());
            stmt.setBigDecimal(5, item.getPrice());
            stmt.setString(6, item.getItemCondition());
            stmt.setString(7, item.getImagePath());
            stmt.setString(8, item.getListingStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an existing item
     *
     * @param item Item object with updated information
     * @return true if update is successful, false otherwise
     */
    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET category_id = ?, title = ?, description = ?, price = ?, " +
                     "item_condition = ?, image_path = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getCategoryId());
            stmt.setString(2, item.getTitle());
            stmt.setString(3, item.getDescription());
            stmt.setBigDecimal(4, item.getPrice());
            stmt.setString(5, item.getItemCondition());
            stmt.setString(6, item.getImagePath());
            stmt.setInt(7, item.getItemId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete an item from the database
     * Only the owner (userId matches) can delete their own items
     *
     * @param itemId Item ID to delete
     * @param userId User ID of the item owner
     * @return true if deletion is successful, false otherwise
     */
    public boolean deleteItem(int itemId, int userId) {
        String sql = "DELETE FROM items WHERE item_id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            stmt.setInt(2, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get item by ID with optional joined data (category name and seller name)
     *
     * @param itemId Item ID to search
     * @return Item object if found, null otherwise
     */
    public Item getItemById(int itemId) {
        String sql = "SELECT i.*, c.category_name, u.full_name AS seller_name " +
                     "FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "LEFT JOIN users u ON i.user_id = u.user_id " +
                     "WHERE i.item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToItem(rs, true);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get all approved items (listing_status = 'APPROVED')
     * Includes category name and seller name via JOIN
     *
     * @return List of approved items
     */
    public List<Item> getAllApprovedItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, c.category_name, u.full_name AS seller_name " +
                     "FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "LEFT JOIN users u ON i.user_id = u.user_id " +
                     "WHERE i.listing_status = 'APPROVED' " +
                     "ORDER BY i.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                items.add(mapResultSetToItem(rs, true));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Get all items posted by a specific user
     * Includes category name via JOIN
     *
     * @param userId User ID to filter by
     * @return List of items posted by the user
     */
    public List<Item> getItemsByUserId(int userId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, c.category_name " +
                     "FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "WHERE i.user_id = ? " +
                     "ORDER BY i.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs, true));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Search items by keyword and/or category
     * Searches in title and description fields
     * Only returns approved items
     *
     * @param keyword Search keyword (can be null to skip keyword filter)
     * @param categoryId Category ID to filter by (can be null to skip category filter)
     * @return List of matching items
     */
    public List<Item> searchItems(String keyword, Integer categoryId) {
        List<Item> items = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT i.*, c.category_name, u.full_name AS seller_name " +
            "FROM items i " +
            "LEFT JOIN categories c ON i.category_id = c.category_id " +
            "LEFT JOIN users u ON i.user_id = u.user_id " +
            "WHERE i.listing_status = 'APPROVED'"
        );

        // Add keyword filter if provided
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (i.title LIKE ? OR i.description LIKE ?)");
        }

        // Add category filter if provided
        if (categoryId != null) {
            sql.append(" AND i.category_id = ?");
        }

        sql.append(" ORDER BY i.created_at DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            // Set keyword parameter if provided
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }

            // Set category parameter if provided
            if (categoryId != null) {
                stmt.setInt(paramIndex, categoryId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs, true));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Get all items with pending approval status
     * For admin moderation
     *
     * @return List of pending items
     */
    public List<Item> getPendingItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, c.category_name, u.full_name AS seller_name " +
                     "FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "LEFT JOIN users u ON i.user_id = u.user_id " +
                     "WHERE i.listing_status = 'PENDING' " +
                     "ORDER BY i.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                items.add(mapResultSetToItem(rs, true));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Update item's listing status
     * Used for admin approval/rejection or marking as sold
     *
     * @param itemId Item ID to update
     * @param status New listing status (APPROVED, REJECTED, SOLD, INACTIVE)
     * @return true if update is successful, false otherwise
     */
    public boolean updateItemStatus(int itemId, String status) {
        String sql = "UPDATE items SET listing_status = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, itemId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper method to map ResultSet to Item object
     *
     * @param rs ResultSet from database query
     * @param includeJoinedData Whether to include joined data (category_name, seller_name)
     * @return Item object with data from ResultSet
     * @throws SQLException if there's an error reading from ResultSet
     */
    private Item mapResultSetToItem(ResultSet rs, boolean includeJoinedData) throws SQLException {
        Item item = new Item();

        item.setItemId(rs.getInt("item_id"));
        item.setUserId(rs.getInt("user_id"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setTitle(rs.getString("title"));
        item.setDescription(rs.getString("description"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setItemCondition(rs.getString("item_condition"));
        item.setImagePath(rs.getString("image_path"));
        item.setListingStatus(rs.getString("listing_status"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        item.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Set optional joined fields if included
        if (includeJoinedData) {
            try {
                item.setCategoryName(rs.getString("category_name"));
            } catch (SQLException e) {
                // Column might not exist in some queries
            }

            try {
                item.setSellerName(rs.getString("seller_name"));
            } catch (SQLException e) {
                // Column might not exist in some queries
            }
        }

        return item;
    }
}


