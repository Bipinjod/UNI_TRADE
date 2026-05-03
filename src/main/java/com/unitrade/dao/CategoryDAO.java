package com.unitrade.dao;

import com.unitrade.model.Category;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CategoryDAO - Data Access Object for Category operations.
 * Now supports category_type (ITEM, SERVICE, REQUEST).
 */
public class CategoryDAO {

    /**
     * Add a new category to the database
     *
     * @param category Category object to add
     * @return true if addition is successful, false otherwise
     */
    public boolean addCategory(Category category) {
        String sql = "INSERT INTO categories (category_name, category_type, description, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getCategoryType() != null ? category.getCategoryType() : "ITEM");
            stmt.setString(3, category.getDescription());
            stmt.setString(4, category.getStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an existing category
     *
     * @param category Category object with updated information
     * @return true if update is successful, false otherwise
     */
    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET category_name=?, category_type=?, description=?, status=? WHERE category_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getCategoryType() != null ? category.getCategoryType() : "ITEM");
            stmt.setString(3, category.getDescription());
            stmt.setString(4, category.getStatus());
            stmt.setInt(5, category.getCategoryId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a category from the database
     * Note: This will fail if there are items linked to this category (foreign key constraint)
     *
     * @param categoryId Category ID to delete
     * @return true if deletion is successful, false otherwise
     */
    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get category by ID
     *
     * @param categoryId Category ID to search
     * @return Category object if found, null otherwise
     */
    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get all categories from database
     *
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        return queryList("SELECT * FROM categories ORDER BY category_type, category_name");
    }

    /**
     * Get all active categories (status = 'ACTIVE')
     *
     * @return List of active categories
     */
    public List<Category> getActiveCategories() {
        return queryList("SELECT * FROM categories WHERE status='ACTIVE' ORDER BY category_type, category_name");
    }

    /** Get active categories filtered by type (ITEM, SERVICE, REQUEST) */
    public List<Category> getActiveCategoriesByType(String type) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE status='ACTIVE' AND category_type=? ORDER BY category_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Category> getCategoriesByType(String type) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE category_type=? ORDER BY category_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── helpers ──
    private List<Category> queryList(String sql) {
        List<Category> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setCategoryId(rs.getInt("category_id"));
        c.setCategoryName(rs.getString("category_name"));
        c.setCategoryType(rs.getString("category_type"));
        c.setDescription(rs.getString("description"));
        c.setStatus(rs.getString("status"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
