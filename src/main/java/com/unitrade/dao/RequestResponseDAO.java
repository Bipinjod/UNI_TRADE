package com.unitrade.dao;

import com.unitrade.model.RequestResponse;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RequestResponseDAO – CRUD for the request_responses table.
 */
public class RequestResponseDAO {

    public boolean addResponse(RequestResponse r) {
        String sql = "INSERT INTO request_responses (request_id,responder_id,response_message,response_status) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getRequestId()); ps.setInt(2, r.getResponderId());
            ps.setString(3, r.getResponseMessage());
            ps.setString(4, r.getResponseStatus() != null ? r.getResponseStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateStatus(int responseId, String status) {
        String sql = "UPDATE request_responses SET response_status=? WHERE response_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, responseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

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

    private RequestResponse map(ResultSet rs) throws SQLException {
        RequestResponse r = new RequestResponse();
        r.setResponseId(rs.getInt("response_id")); r.setRequestId(rs.getInt("request_id"));
        r.setResponderId(rs.getInt("responder_id")); r.setResponseMessage(rs.getString("response_message"));
        r.setResponseStatus(rs.getString("response_status")); r.setCreatedAt(rs.getTimestamp("created_at"));
        try { r.setResponderName(rs.getString("responder_name")); } catch (SQLException ignored) {}
        try { r.setRequestTitle(rs.getString("request_title")); } catch (SQLException ignored) {}
        return r;
    }
}

