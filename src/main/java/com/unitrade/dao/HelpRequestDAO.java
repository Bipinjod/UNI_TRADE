package com.unitrade.dao;

import com.unitrade.model.HelpRequest;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HelpRequestDAO – CRUD for the help_requests table.
 */
public class HelpRequestDAO {

    public boolean addRequest(HelpRequest r) {
        String sql = "INSERT INTO help_requests (user_id,category_id,title,description,budget,urgency_level,request_status,approval_status) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getUserId()); ps.setInt(2, r.getCategoryId());
            ps.setString(3, r.getTitle()); ps.setString(4, r.getDescription());
            ps.setBigDecimal(5, r.getBudget());
            ps.setString(6, r.getUrgencyLevel() != null ? r.getUrgencyLevel() : "MEDIUM");
            ps.setString(7, r.getRequestStatus() != null ? r.getRequestStatus() : "OPEN");
            ps.setString(8, r.getApprovalStatus() != null ? r.getApprovalStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateRequest(HelpRequest r) {
        String sql = "UPDATE help_requests SET category_id=?,title=?,description=?,budget=?,urgency_level=?,updated_at=CURRENT_TIMESTAMP WHERE request_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getCategoryId()); ps.setString(2, r.getTitle());
            ps.setString(3, r.getDescription()); ps.setBigDecimal(4, r.getBudget());
            ps.setString(5, r.getUrgencyLevel()); ps.setInt(6, r.getRequestId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteRequest(int requestId, int userId) {
        String sql = "DELETE FROM help_requests WHERE request_id=? AND user_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, requestId); ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateApprovalStatus(int requestId, String status) {
        String sql = "UPDATE help_requests SET approval_status=?, updated_at=CURRENT_TIMESTAMP WHERE request_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateRequestStatus(int requestId, String status) {
        String sql = "UPDATE help_requests SET request_status=?, updated_at=CURRENT_TIMESTAMP WHERE request_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public HelpRequest getRequestById(int requestId) {
        String sql = BASE_SELECT + " WHERE r.request_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<HelpRequest> getApprovedRequests() {
        return listQuery(BASE_SELECT + " WHERE r.approval_status='APPROVED' AND r.request_status='OPEN' ORDER BY r.created_at DESC");
    }

    public List<HelpRequest> getPendingRequests() {
        return listQuery(BASE_SELECT + " WHERE r.approval_status='PENDING' ORDER BY r.created_at DESC");
    }

    public List<HelpRequest> getRequestsByUserId(int userId) {
        List<HelpRequest> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE r.user_id=? ORDER BY r.created_at DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // helpers
    private static final String BASE_SELECT =
            "SELECT r.*, c.category_name, u.full_name AS poster_name FROM help_requests r " +
            "LEFT JOIN categories c ON r.category_id=c.category_id LEFT JOIN users u ON r.user_id=u.user_id";

    private List<HelpRequest> listQuery(String sql) {
        List<HelpRequest> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private HelpRequest map(ResultSet rs) throws SQLException {
        HelpRequest r = new HelpRequest();
        r.setRequestId(rs.getInt("request_id")); r.setUserId(rs.getInt("user_id"));
        r.setCategoryId(rs.getInt("category_id")); r.setTitle(rs.getString("title"));
        r.setDescription(rs.getString("description")); r.setBudget(rs.getBigDecimal("budget"));
        r.setUrgencyLevel(rs.getString("urgency_level")); r.setRequestStatus(rs.getString("request_status"));
        r.setApprovalStatus(rs.getString("approval_status"));
        r.setCreatedAt(rs.getTimestamp("created_at")); r.setUpdatedAt(rs.getTimestamp("updated_at"));
        try { r.setCategoryName(rs.getString("category_name")); } catch (SQLException ignored) {}
        try { r.setPosterName(rs.getString("poster_name")); } catch (SQLException ignored) {}
        return r;
    }
}

