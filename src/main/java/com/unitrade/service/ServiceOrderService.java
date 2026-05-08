package com.unitrade.service;

import com.unitrade.dao.ServiceOrderDAO;
import com.unitrade.model.ServiceOrder;

import java.util.List;

/**
 * ServiceOrderService – business logic layer for service order lifecycle.
 * Handles order placement, status transitions, and retrieval queries.
 */
public class ServiceOrderService {

    private final ServiceOrderDAO dao = new ServiceOrderDAO();

    /**
     * Place a new service order after basic validation.
     *
     * @param o ServiceOrder populated with serviceId, buyerId, providerId, and optional message
     * @return Success or descriptive error message
     */
    public String createOrder(ServiceOrder o) {
        if (o.getServiceId() <= 0) return "Invalid service";
        if (o.getBuyerId() <= 0 || o.getProviderId() <= 0) return "Invalid user";
        // Prevent a provider from ordering their own service
        if (o.getBuyerId() == o.getProviderId()) return "You cannot order your own service";
        o.setOrderStatus("PENDING"); // default status at creation
        return dao.createOrder(o) ? "Service order placed successfully!" : "Failed to place order.";
    }

    /**
     * Update the status of an existing service order.
     *
     * @param orderId ID of the order to update
     * @param status  New status string (ACCEPTED, REJECTED, COMPLETED, CANCELLED)
     * @return true if the database row was updated
     */
    public boolean updateStatus(int orderId, String status) { return dao.updateStatus(orderId, status); }

    /**
     * Retrieve all orders placed by a specific buyer (client-side view).
     *
     * @param buyerId The buyer's user ID
     * @return List of ServiceOrder objects placed by the buyer
     */
    public List<ServiceOrder> getBuyerOrders(int buyerId) { return dao.getOrdersByBuyerId(buyerId); }

    /**
     * Retrieve all orders received by a service provider.
     *
     * @param providerId The provider's user ID
     * @return List of ServiceOrder objects directed at the provider
     */
    public List<ServiceOrder> getProviderOrders(int providerId) { return dao.getOrdersByProviderId(providerId); }
}
