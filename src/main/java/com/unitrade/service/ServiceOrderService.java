package com.unitrade.service;

import com.unitrade.dao.ServiceOrderDAO;
import com.unitrade.model.ServiceOrder;

import java.util.List;

/**
 * ServiceOrderService – business logic for service orders.
 */
public class ServiceOrderService {

    private final ServiceOrderDAO dao = new ServiceOrderDAO();

    public String createOrder(ServiceOrder o) {
        if (o.getServiceId() <= 0) return "Invalid service";
        if (o.getBuyerId() <= 0 || o.getProviderId() <= 0) return "Invalid user";
        if (o.getBuyerId() == o.getProviderId()) return "You cannot order your own service";
        o.setOrderStatus("PENDING");
        return dao.createOrder(o) ? "Service order placed successfully!" : "Failed to place order.";
    }

    public boolean updateStatus(int orderId, String status) { return dao.updateStatus(orderId, status); }
    public List<ServiceOrder> getBuyerOrders(int buyerId) { return dao.getOrdersByBuyerId(buyerId); }
    public List<ServiceOrder> getProviderOrders(int providerId) { return dao.getOrdersByProviderId(providerId); }
}

