package com.unitrade.dao;

import com.unitrade.model.HelpRequest;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HelpRequestDAO – Data Access Object for the help_requests table.
 * Provides full CRUD, approval workflow, and lookup methods for peer help requests.
 */
public class HelpRequestDAO {

    /**
     * Insert a new help request record into the database.
     *
     * @param r HelpRequest populated with userId, categoryId, title, description, budget, urgency,
     *          requestStatus, and approvalStatus
     * @return true if at least one row was inserted
     */
    public boolean addRequest(HelpRequest r) {
        String sql = "INSERT INTO help_requests (user_id,category_id,title,description,budget,urgency_level,request_status,approval_status) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getUserId()); ps.setInt(2, r.getCategoryId());
            ps.setString(3, r.getTitle()); ps.setString(4, r.getDescription());
            ps.setBigDecimal(5, r.getBudget());
            // Fall back to safe defaults if not set by the caller
            ps.setString(6, r.getUrgencyLevel() != null ? r.getUrgencyLevel() : "MEDIUM");
            ps.setString(7, r.getRequestStatus() != null ? r.getRequestStatus() : "OPEN");
            ps.setString(8, r.getApprovalStatus() != null ? r.getApprovalStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Update the editable fields of an existing help request.
     * Approval/request status changes are handled by dedicated methods.
     *
     * @param r HelpRequest with updated fields and a valid requestId
     * @return true if the row was found and updated
     */
    public boolean updateRequest(HelpRequest r) {
        String sql = "UPDATE help_requests SET category_id=?,title=?,description=?,budget=?,urgency_level=?,updated_at=CURRENT_TIMESTAMP WHERE request_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getCategoryId()); ps.setString(2, r.getTitle());
            ps.setString(3, r.getDescription()); ps.setBigDecimal(4, r.getBudget());
            ps.setString(5, r.getUrgencyLevel()); ps.setInt(6, r.getRequestId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Delete a help request, enforcing that the requesting user is the owner.
     *
     * @param requestId ID of the request to delete
     * @param userId    User ID that must match the request owner
     * @return true if the record was deleted
     */
    public boolean deleteRequest(int requestId, int userId) {
        String sql = "DELETE FROM help_requests WHERE request_id=? AND user_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, requestId); ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Update only the approval_status column (admin action).
     *
     * @param requestId ID of the help request
     * @param status    New approval status (APPROVED or REJECTED)
     * @return true if the row was updated
     */
    public boolean updateApprovalStatus(int requestId, String status) {
        String sql = "UPDATE help_requests SET approval_status=?, updated_at=CURRENT_TIMESTAMP WHERE request_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Update only the request_status column (e.g. OPEN → CLOSED or FULFILLED).
     *
     * @param requestId ID of the help request
     * @param status    New request status (OPEN, CLOSED, FULFILLED)
     * @return true if the row was updated
     */
    public boolean updateRequestStatus(int requestId, String status) {
        String sql = "UPDATE help_requests SET request_status=?, updated_at=CURRENT_TIMESTAMP WHERE request_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Retrieve a single help request by its primary key, joined with category and poster name.
     *
     * @param requestId Primary key of the request
     * @return HelpRequest object, or null if not found
     */
    public HelpRequest getRequestById(int requestId) {
        String sql = BASE_SELECT + " WHERE r.request_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Get all admin-approved, currently open help requests (for the public browse page).
     *
     * @return List of approved + open HelpRequest objects, newest first
     */
    public List<HelpRequest> getApprovedRequests() {
        return listQuery(BASE_SELECT + " WHERE r.approval_status='APPROVED' AND r.request_status='OPEN' ORDER BY r.created_at DESC");
    }

    /**
     * Get all requests awaiting admin approval (for the admin panel).
     *
     * @return List of pending HelpRequest objects, newest first
     */
    public List<HelpRequest> getPendingRequests() {
        return listQuery(BASE_SELECT + " WHERE r.approval_status='PENDING' ORDER BY r.created_at DESC");
    }

    /**
     * Get all help requests submitted by a specific user.
     *
     * @param userId The requester's user ID
     * @return List of the user's help requests, newest first
     */
    public List<HelpRequest> getRequestsByUserId(int userId) {
        List<HelpRequest> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE r.user_id=? ORDER BY r.created_at DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Reusable base SELECT that joins categories and users for display purposes.
     * Append WHERE / ORDER BY clauses as needed before executing.
     */
    private static final String BASE_SELECT =
            "SELECT r.*, c.category_name, u.full_name AS poster_name FROM help_requests r " +
            "LEFT JOIN categories c ON r.category_id=c.category_id LEFT JOIN users u ON r.user_id=u.user_id";

    /**
     * Execute a no-parameter SQL query and map all rows to HelpRequest objects.
     *
     * @param sql Complete SQL SELECT string (no unbound parameters)
     * @return List of HelpRequest objects
     */
    private List<HelpRequest> listQuery(String sql) {
        List<HelpRequest> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Map the current ResultSet row to a HelpRequest object.
     * Silently skips optional joined columns that may not be present.
     *
     * @param rs ResultSet positioned at the current row
     * @return Populated HelpRequest object
     * @throws SQLException if a required column cannot be read
     */
    private HelpRequest map(ResultSet rs) throws SQLException {
        HelpRequest r = new HelpRequest();
        r.setRequestId(rs.getInt("request_id")); r.setUserId(rs.getInt("user_id"));
        r.setCategoryId(rs.getInt("category_id")); r.setTitle(rs.getString("title"));
        r.setDescription(rs.getString("description")); r.setBudget(rs.getBigDecimal("budget"));
        r.setUrgencyLevel(rs.getString("urgency_level")); r.setRequestStatus(rs.getString("request_status"));
        r.setApprovalStatus(rs.getString("approval_status"));
        r.setCreatedAt(rs.getTimestamp("created_at")); r.setUpdatedAt(rs.getTimestamp("updated_at"));
        // Joined display fields — silently skip if column is not in the result set
        try { r.setCategoryName(rs.getString("category_name")); } catch (SQLException ignored) {}
        try { r.setPosterName(rs.getString("poster_name")); } catch (SQLException ignored) {}
        return r;
    }
}
