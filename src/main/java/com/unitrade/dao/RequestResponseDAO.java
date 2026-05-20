package com.unitrade.dao;

import com.unitrade.model.RequestResponse;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RequestResponseDAO - Data Access Object for the request_responses table.
 * <p>
 * Provides persistence methods for student responses to help requests,
 * including insertion, status updates, and retrieval by request or responder.
 * </p>
 */
public class RequestResponseDAO {

    /**
     * Insert a new response record into the database.
     * Defaults the status to PENDING if not set by the caller.
     *
     * @param r RequestResponse populated with requestId, responderId, responseMessage, and responseStatus
     * @return {@code true} if at least one row was inserted
     */
    public boolean addResponse(RequestResponse r) {
        String sql = "INSERT INTO request_responses (request_id,responder_id,response_message,response_status) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getRequestId()); ps.setInt(2, r.getResponderId());
            ps.setString(3, r.getResponseMessage());
            // Use PENDING as a safe default if status was not explicitly set
            ps.setString(4, r.getResponseStatus() != null ? r.getResponseStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Update the status of an existing response (ACCEPTED or REJECTED).
     *
     * @param responseId Primary key of the response to update
     * @param status     New status string (ACCEPTED, REJECTED, PENDING)
     * @return {@code true} if the row was found and updated
     */
    public boolean updateStatus(int responseId, String status) {
        String sql = "UPDATE request_responses SET response_status=? WHERE response_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, responseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Retrieve all responses for a specific help request, joined with the responder's name.
     *
     * @param requestId ID of the help request
     * @return List of {@link RequestResponse} objects ordered newest first
     */
    public List<RequestResponse> getResponsesByRequestId(int requestId) {
        List<RequestResponse> list = new ArrayList<>();
        String sql = "SELECT rr.*, u.full_name AS responder_name FROM request_responses rr " +
                "LEFT JOIN users u ON rr.responder_id=u.user_id WHERE rr.request_id=? ORDER BY rr.created_at DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Retrieve all responses submitted by a specific user, joined with the request title.
     *
     * @param responderId The responder's user ID
     * @return List of {@link RequestResponse} objects ordered newest first, with request titles
     */
    public List<RequestResponse> getResponsesByResponderId(int responderId) {
        List<RequestResponse> list = new ArrayList<>();
        String sql = "SELECT rr.*, u.full_name AS responder_name, hr.title AS request_title FROM request_responses rr " +
                "LEFT JOIN users u ON rr.responder_id=u.user_id LEFT JOIN help_requests hr ON rr.request_id=hr.request_id " +
                "WHERE rr.responder_id=? ORDER BY rr.created_at DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, responderId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Map a single ResultSet row to a {@link RequestResponse} object.
     * Gracefully handles optional joined columns (responder_name, request_title).
     *
     * @param rs ResultSet positioned at the current row
     * @return Populated RequestResponse object
     * @throws SQLException if a required column cannot be read
     */
    private RequestResponse map(ResultSet rs) throws SQLException {
        RequestResponse r = new RequestResponse();
        r.setResponseId(rs.getInt("response_id")); r.setRequestId(rs.getInt("request_id"));
        r.setResponderId(rs.getInt("responder_id")); r.setResponseMessage(rs.getString("response_message"));
        r.setResponseStatus(rs.getString("response_status")); r.setCreatedAt(rs.getTimestamp("created_at"));
        // Joined display fields — silently ignore if the column is absent from the result set
        try { r.setResponderName(rs.getString("responder_name")); } catch (SQLException ignored) {}
        try { r.setRequestTitle(rs.getString("request_title")); } catch (SQLException ignored) {}
        return r;
    }
}
