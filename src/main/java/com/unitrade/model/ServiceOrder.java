package com.unitrade.model;

import java.sql.Timestamp;

/**
 * ServiceOrder model – represents a row in the service_orders table.
 * A ServiceOrder is created when a buyer requests a service from a provider.
 * The provider can then accept, reject, or complete the order.
 */
public class ServiceOrder {

    // ── Primary Key ───────────────────────────────────────────────────────────
    private int serviceOrderId;

    // ── Foreign Keys ─────────────────────────────────────────────────────────
    /** ID of the service being ordered. */
    private int serviceId;
    /** ID of the user who placed the order (the buyer/client). */
    private int buyerId;
    /** ID of the user who provides the service (the seller). */
    private int providerId;

    // ── Order Details ─────────────────────────────────────────────────────────
    /** Optional message from the buyer describing their specific requirements. */
    private String requestMessage;
    /** Current lifecycle status. ENUM: PENDING, ACCEPTED, REJECTED, COMPLETED, CANCELLED */
    private String orderStatus;

    // ── Timestamps ────────────────────────────────────────────────────────────
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ── Joined Display Fields (not stored in DB) ─────────��────────────────────
    /** Service title resolved from the services table via JOIN. */
    private String serviceTitle;
    /** Buyer's full name resolved from the users table via JOIN. */
    private String buyerName;
    /** Provider's full name resolved from the users table via JOIN. */
    private String providerName;

    /** Default no-arg constructor. */
    public ServiceOrder() {}

    // ── Getters / Setters ─────────────────────────────────────────────────────

    /** @return Primary key of this service order. */
    public int getServiceOrderId() { return serviceOrderId; }
    /** @param serviceOrderId Primary key to set. */
    public void setServiceOrderId(int serviceOrderId) { this.serviceOrderId = serviceOrderId; }

    /** @return ID of the associated service. */
    public int getServiceId() { return serviceId; }
    /** @param serviceId Service foreign key. */
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    /** @return ID of the buyer who placed this order. */
    public int getBuyerId() { return buyerId; }
    /** @param buyerId Buyer's user ID. */
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }

    /** @return ID of the service provider. */
    public int getProviderId() { return providerId; }
    /** @param providerId Provider's user ID. */
    public void setProviderId(int providerId) { this.providerId = providerId; }

    /** @return Optional request message from the buyer. */
    public String getRequestMessage() { return requestMessage; }
    /** @param requestMessage Buyer's message. */
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }

    /** @return Current order status (PENDING, ACCEPTED, REJECTED, COMPLETED, CANCELLED). */
    public String getOrderStatus() { return orderStatus; }
    /** @param orderStatus New order status. */
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    /** @return Timestamp when this order was created. */
    public Timestamp getCreatedAt() { return createdAt; }
    /** @param createdAt Creation timestamp. */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    /** @return Timestamp of the last update to this order. */
    public Timestamp getUpdatedAt() { return updatedAt; }
    /** @param updatedAt Last-updated timestamp. */
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    /** @return Service title resolved via JOIN (may be null if not joined). */
    public String getServiceTitle() { return serviceTitle; }
    /** @param serviceTitle Joined service title. */
    public void setServiceTitle(String serviceTitle) { this.serviceTitle = serviceTitle; }

    /** @return Buyer's full name resolved via JOIN (may be null if not joined). */
    public String getBuyerName() { return buyerName; }
    /** @param buyerName Joined buyer name. */
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    /** @return Provider's full name resolved via JOIN (may be null if not joined). */
    public String getProviderName() { return providerName; }
    /** @param providerName Joined provider name. */
    public void setProviderName(String providerName) { this.providerName = providerName; }
}
