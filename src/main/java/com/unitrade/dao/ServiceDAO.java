package com.unitrade.dao;

import com.unitrade.model.Service;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceDAO – Data Access Object for the services table.
 * Provides full CRUD operations and flexible query methods for peer service listings.
 */
public class ServiceDAO {

    /**
     * Insert a new service record into the database.
     *
     * @param s Service object with all required fields populated
     * @return true if at least one row was inserted
     */
    public boolean addService(Service s) {
        String sql = "INSERT INTO services (user_id,category_id,title,description,price,availability_status,approval_status) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getUserId()); ps.setInt(2, s.getCategoryId());
            ps.setString(3, s.getTitle()); ps.setString(4, s.getDescription());
            ps.setBigDecimal(5, s.getPrice());
            // Default to AVAILABLE / PENDING if not explicitly set
            ps.setString(6, s.getAvailabilityStatus() != null ? s.getAvailabilityStatus() : "AVAILABLE");
            ps.setString(7, s.getApprovalStatus() != null ? s.getApprovalStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Update an existing service record (category, title, description, price, availability).
     * The approval status is intentionally NOT updated here.
     *
     * @param s Service object with updated values and a valid serviceId
     * @return true if the row was found and updated
     */
    public boolean updateService(Service s) {
        String sql = "UPDATE services SET category_id=?,title=?,description=?,price=?,availability_status=?,updated_at=CURRENT_TIMESTAMP WHERE service_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getCategoryId()); ps.setString(2, s.getTitle());
            ps.setString(3, s.getDescription()); ps.setBigDecimal(4, s.getPrice());
            ps.setString(5, s.getAvailabilityStatus()); ps.setInt(6, s.getServiceId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Delete a service record, enforcing that the caller is the owner.
     *
     * @param serviceId ID of the service to delete
     * @param userId    User ID that must match the service's owner
     * @return true if the record was deleted
     */
    public boolean deleteService(int serviceId, int userId) {
        String sql = "DELETE FROM services WHERE service_id=? AND user_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, serviceId); ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Update only the approval_status column for a service (admin action).
     *
     * @param serviceId ID of the service to update
     * @param status    New approval status (APPROVED or REJECTED)
     * @return true if the row was updated
     */
    public boolean updateApprovalStatus(int serviceId, String status) {
        String sql = "UPDATE services SET approval_status=?, updated_at=CURRENT_TIMESTAMP WHERE service_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, serviceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Retrieve a single service by ID, joined with category name and provider's full name.
     *
     * @param serviceId Primary key of the service
     * @return Service object, or null if not found
     */
    public Service getServiceById(int serviceId) {
        String sql = "SELECT s.*, c.category_name, u.full_name AS provider_name FROM services s " +
                "LEFT JOIN categories c ON s.category_id=c.category_id " +
                "LEFT JOIN users u ON s.user_id=u.user_id WHERE s.service_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Get all services that have been approved by an admin, newest first.
     *
     * @return List of approved Service objects
     */
    public List<Service> getApprovedServices() {
        return listQuery("SELECT s.*, c.category_name, u.full_name AS provider_name FROM services s " +
                "LEFT JOIN categories c ON s.category_id=c.category_id LEFT JOIN users u ON s.user_id=u.user_id " +
                "WHERE s.approval_status='APPROVED' ORDER BY s.created_at DESC");
    }

    /**
     * Get all services awaiting admin approval, newest first.
     *
     * @return List of pending Service objects
     */
    public List<Service> getPendingServices() {
        return listQuery("SELECT s.*, c.category_name, u.full_name AS provider_name FROM services s " +
                "LEFT JOIN categories c ON s.category_id=c.category_id LEFT JOIN users u ON s.user_id=u.user_id " +
                "WHERE s.approval_status='PENDING' ORDER BY s.created_at DESC");
    }

    /**
     * Get all services posted by a specific user, newest first.
     *
     * @param userId The provider/owner user ID
     * @return List of Service objects owned by the user
     */
    public List<Service> getServicesByUserId(int userId) {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT s.*, c.category_name, u.full_name AS provider_name FROM services s " +
                "LEFT JOIN categories c ON s.category_id=c.category_id LEFT JOIN users u ON s.user_id=u.user_id " +
                "WHERE s.user_id=? ORDER BY s.created_at DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Search approved services by keyword (title/description) and/or category.
     *
     * @param keyword    Search string; applied to title and description with LIKE; may be null
     * @param categoryId Category filter; may be null to skip category filtering
     * @return List of matching approved Service objects
     */
    public List<Service> searchServices(String keyword, Integer categoryId) {
        List<Service> list = new ArrayList<>();
        // Build dynamic query depending on which filters are supplied
        StringBuilder sql = new StringBuilder("SELECT s.*, c.category_name, u.full_name AS provider_name FROM services s " +
                "LEFT JOIN categories c ON s.category_id=c.category_id LEFT JOIN users u ON s.user_id=u.user_id " +
                "WHERE s.approval_status='APPROVED'");
        if (keyword != null && !keyword.trim().isEmpty()) sql.append(" AND (s.title LIKE ? OR s.description LIKE ?)");
        if (categoryId != null) sql.append(" AND s.category_id=?");
        sql.append(" ORDER BY s.created_at DESC");
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int idx = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String p = "%" + keyword + "%";
                ps.setString(idx++, p); // title LIKE
                ps.setString(idx++, p); // description LIKE
            }
            if (categoryId != null) ps.setInt(idx, categoryId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Execute a static (no-parameter) SQL query and map each row to a Service.
     *
     * @param sql Full SQL SELECT string with no placeholders
     * @return List of Service objects returned by the query
     */
    private List<Service> listQuery(String sql) {
        List<Service> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Map a single ResultSet row to a Service object.
     * Gracefully handles missing joined columns (category_name, provider_name).
     *
     * @param rs ResultSet positioned at the current row
     * @return Populated Service object
     * @throws SQLException if a required column cannot be read
     */
    private Service map(ResultSet rs) throws SQLException {
        Service s = new Service();
        s.setServiceId(rs.getInt("service_id")); s.setUserId(rs.getInt("user_id"));
        s.setCategoryId(rs.getInt("category_id")); s.setTitle(rs.getString("title"));
        s.setDescription(rs.getString("description")); s.setPrice(rs.getBigDecimal("price"));
        s.setAvailabilityStatus(rs.getString("availability_status")); s.setApprovalStatus(rs.getString("approval_status"));
        s.setCreatedAt(rs.getTimestamp("created_at")); s.setUpdatedAt(rs.getTimestamp("updated_at"));
        // Joined columns may not be present in all queries — ignore missing ones
        try { s.setCategoryName(rs.getString("category_name")); } catch (SQLException ignored) {}
        try { s.setProviderName(rs.getString("provider_name")); } catch (SQLException ignored) {}
        return s;
    }
}
