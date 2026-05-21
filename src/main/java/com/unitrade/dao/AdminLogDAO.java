package com.unitrade.dao;

import com.unitrade.model.AdminLog;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminLogDAO - Data Access Object for the admin_logs table.
 * <p>
 * Provides methods to record admin actions and retrieve recent audit log
 * entries for display in the admin dashboard.
 * </p>
 */
public class AdminLogDAO {

    /**
     * Insert a new admin action log entry into the database.
     *
     * @param log AdminLog populated with adminId, actionType, targetTable,
     *            targetId, and actionDescription
     * @return {@code true} if at least one row was inserted successfully
     */
    public boolean addLog(AdminLog log) {
        String sql = "INSERT INTO admin_logs (admin_id,action_type,target_table,target_id,action_description) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, log.getAdminId()); ps.setString(2, log.getActionType());
            ps.setString(3, log.getTargetTable()); ps.setInt(4, log.getTargetId());
            ps.setString(5, log.getActionDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Retrieve the most recent admin log entries, joined with the admin's full name.
     *
     * @param limit maximum number of log entries to return
     * @return List of {@link AdminLog} objects ordered newest first, at most {@code limit} items
     */
    public List<AdminLog> getRecentLogs(int limit) {
        List<AdminLog> list = new ArrayList<>();
        String sql = "SELECT l.*, u.full_name AS admin_name FROM admin_logs l LEFT JOIN users u ON l.admin_id=u.user_id ORDER BY l.created_at DESC LIMIT ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Map a single ResultSet row to an {@link AdminLog} object.
     * Gracefully handles the optional {@code admin_name} joined column.
     *
     * @param rs ResultSet positioned at the current row
     * @return Populated AdminLog object
     * @throws SQLException if a required column cannot be read
     */
    private AdminLog map(ResultSet rs) throws SQLException {
        AdminLog l = new AdminLog();
        l.setLogId(rs.getInt("log_id")); l.setAdminId(rs.getInt("admin_id"));
        l.setActionType(rs.getString("action_type")); l.setTargetTable(rs.getString("target_table"));
        l.setTargetId(rs.getInt("target_id")); l.setActionDescription(rs.getString("action_description"));
        l.setCreatedAt(rs.getTimestamp("created_at"));
        // admin_name is a joined column — silently ignore if absent
        try { l.setAdminName(rs.getString("admin_name")); } catch (SQLException ignored) {}
        return l;
    }
}
