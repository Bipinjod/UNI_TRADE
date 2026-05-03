package com.unitrade.dao;

import com.unitrade.model.ServiceOrder;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceOrderDAO – CRUD for the service_orders table.
 */
public class ServiceOrderDAO {

    public boolean createOrder(ServiceOrder o) {
        String sql = "INSERT INTO service_orders (service_id,buyer_id,provider_id,request_message,order_status) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, o.getServiceId()); ps.setInt(2, o.getBuyerId());
            ps.setInt(3, o.getProviderId()); ps.setString(4, o.getRequestMessage());
            ps.setString(5, o.getOrderStatus() != null ? o.getOrderStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE service_orders SET order_status=?, updated_at=CURRENT_TIMESTAMP WHERE service_order_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status); ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<ServiceOrder> getOrdersByBuyerId(int buyerId) {
        return listByField("buyer_id", buyerId);
    }

    public List<ServiceOrder> getOrdersByProviderId(int providerId) {
        return listByField("provider_id", providerId);
    }

    private List<ServiceOrder> listByField(String field, int val) {
        List<ServiceOrder> list = new ArrayList<>();
        String sql = "SELECT o.*, sv.title AS service_title, ub.full_name AS buyer_name, up.full_name AS provider_name " +
                "FROM service_orders o LEFT JOIN services sv ON o.service_id=sv.service_id " +
                "LEFT JOIN users ub ON o.buyer_id=ub.user_id LEFT JOIN users up ON o.provider_id=up.user_id " +
                "WHERE o." + field + "=? ORDER BY o.created_at DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, val);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private ServiceOrder map(ResultSet rs) throws SQLException {
        ServiceOrder o = new ServiceOrder();
        o.setServiceOrderId(rs.getInt("service_order_id")); o.setServiceId(rs.getInt("service_id"));
        o.setBuyerId(rs.getInt("buyer_id")); o.setProviderId(rs.getInt("provider_id"));
        o.setRequestMessage(rs.getString("request_message")); o.setOrderStatus(rs.getString("order_status"));
        o.setCreatedAt(rs.getTimestamp("created_at")); o.setUpdatedAt(rs.getTimestamp("updated_at"));
        try { o.setServiceTitle(rs.getString("service_title")); } catch (SQLException ignored) {}
        try { o.setBuyerName(rs.getString("buyer_name")); } catch (SQLException ignored) {}
        try { o.setProviderName(rs.getString("provider_name")); } catch (SQLException ignored) {}
        return o;
    }
}

