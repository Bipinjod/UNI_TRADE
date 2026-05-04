package com.unitrade.model;

import java.sql.Timestamp;

/**
 * ServiceOrder model – represents the service_orders table.
 */
public class ServiceOrder {

    private int serviceOrderId;
    private int serviceId;
    private int buyerId;
    private int providerId;
    private String requestMessage;
    private String orderStatus; // PENDING, ACCEPTED, REJECTED, COMPLETED, CANCELLED
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // joined helpers
    private String serviceTitle;
    private String buyerName;
    private String providerName;

    public ServiceOrder() {}

    public int getServiceOrderId() { return serviceOrderId; }
    public void setServiceOrderId(int serviceOrderId) { this.serviceOrderId = serviceOrderId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getBuyerId() { return buyerId; }
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }

    public int getProviderId() { return providerId; }
    public void setProviderId(int providerId) { this.providerId = providerId; }

    public String getRequestMessage() { return requestMessage; }
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getServiceTitle() { return serviceTitle; }
    public void setServiceTitle(String serviceTitle) { this.serviceTitle = serviceTitle; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
}

