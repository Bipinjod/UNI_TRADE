package com.unitrade.dao;

import com.unitrade.model.Service;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceDAO – CRUD for the services table.
 */
public class ServiceDAO {

    public boolean addService(Service s) {
        String sql = "INSERT INTO services (user_id,category_id,title,description,price,availability_status,approval_status) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getUserId()); ps.setInt(2, s.getCategoryId());
            ps.setString(3, s.getTitle()); ps.setString(4, s.getDescription());
            ps.setBigDecimal(5, s.getPrice());
            ps.setString(6, s.getAvailabilityStatus() != null ? s.getAvailabilityStatus() : "AVAILABLE");
            ps.setString(7, s.getApprovalStatus() != null ? s.getApprovalStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateService(Service s) {
        String sql = "UPDATE services SET category_id=?,title=?,description=?,price=?,availability_status=?,updated_at=CURRENT_TIMESTAMP WHERE service_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getCategoryId()); ps.setString(2, s.getTitle());
            ps.setString(3, s.getDescription()); ps.setBigDecimal(4, s.getPrice());
            ps.setString(5, s.getAvailabilityStatus()); ps.setInt(6, s.getServiceId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteService(int serviceId, int userId) {
        String sql = "DELETE FROM services WHERE service_id=? AND user_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, serviceId); ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateApprovalStatus(int serviceId, String status) {
        String sql = "UPDATE services SET approval_status=?, updated_at=CURRENT_TIMESTAMP WHERE service_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, serviceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

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

    public List<Service> getApprovedServices() {
        return listQuery("SELECT s.*, c.category_name, u.full_name AS provider_name FROM services s " +
                "LEFT JOIN categories c ON s.category_id=c.category_id LEFT JOIN users u ON s.user_id=u.user_id " +
                "WHERE s.approval_status='APPROVED' ORDER BY s.created_at DESC");
    }

    public List<Service> getPendingServices() {
        return listQuery("SELECT s.*, c.category_name, u.full_name AS provider_name FROM services s " +
                "LEFT JOIN categories c ON s.category_id=c.category_id LEFT JOIN users u ON s.user_id=u.user_id " +
                "WHERE s.approval_status='PENDING' ORDER BY s.created_at DESC");
    }

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

    public List<Service> searchServices(String keyword, Integer categoryId) {
        List<Service> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT s.*, c.category_name, u.full_name AS provider_name FROM services s " +
                "LEFT JOIN categories c ON s.category_id=c.category_id LEFT JOIN users u ON s.user_id=u.user_id " +
                "WHERE s.approval_status='APPROVED'");
        if (keyword != null && !keyword.trim().isEmpty()) sql.append(" AND (s.title LIKE ? OR s.description LIKE ?)");
        if (categoryId != null) sql.append(" AND s.category_id=?");
        sql.append(" ORDER BY s.created_at DESC");
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int idx = 1;
            if (keyword != null && !keyword.trim().isEmpty()) { String p = "%" + keyword + "%"; ps.setString(idx++, p); ps.setString(idx++, p); }
            if (categoryId != null) ps.setInt(idx, categoryId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // helpers
    private List<Service> listQuery(String sql) {
        List<Service> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Service map(ResultSet rs) throws SQLException {
        Service s = new Service();
        s.setServiceId(rs.getInt("service_id")); s.setUserId(rs.getInt("user_id"));
        s.setCategoryId(rs.getInt("category_id")); s.setTitle(rs.getString("title"));
        s.setDescription(rs.getString("description")); s.setPrice(rs.getBigDecimal("price"));
        s.setAvailabilityStatus(rs.getString("availability_status")); s.setApprovalStatus(rs.getString("approval_status"));
        s.setCreatedAt(rs.getTimestamp("created_at")); s.setUpdatedAt(rs.getTimestamp("updated_at"));
        try { s.setCategoryName(rs.getString("category_name")); } catch (SQLException ignored) {}
        try { s.setProviderName(rs.getString("provider_name")); } catch (SQLException ignored) {}
        return s;
    }
}

