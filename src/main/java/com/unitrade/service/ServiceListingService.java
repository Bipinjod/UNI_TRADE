package com.unitrade.service;

import com.unitrade.dao.ServiceDAO;
import com.unitrade.model.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * ServiceListingService – business logic layer for peer service listings.
 * Handles validation, creation, update, deletion, and approval of services.
 */
public class ServiceListingService {

    private final ServiceDAO serviceDAO = new ServiceDAO();

    /**
     * Add a new service listing with validation.
     *
     * @param s Service object populated from the form
     * @return Success or descriptive error message
     */
    public String addService(Service s) {
        if (s.getUserId() <= 0) return "Invalid user";
        if (s.getCategoryId() <= 0) return "Please select a category";
        if (s.getTitle() == null || s.getTitle().trim().isEmpty()) return "Title is required";
        if (s.getDescription() == null || s.getDescription().trim().length() < 20) return "Description must be at least 20 characters";
        if (s.getPrice() == null || s.getPrice().compareTo(BigDecimal.ZERO) < 0) return "Invalid price";
        // Set default statuses before persisting
        s.setAvailabilityStatus("AVAILABLE");
        s.setApprovalStatus("PENDING");
        return serviceDAO.addService(s) ? "Service posted successfully! Waiting for admin approval." : "Failed to post service.";
    }

    /**
     * Update an existing service listing.
     *
     * @param s Service object with updated fields
     * @return Success or error message
     */
    public String updateService(Service s) {
        if (s.getServiceId() <= 0) return "Invalid service ID";
        if (s.getTitle() == null || s.getTitle().trim().isEmpty()) return "Title is required";
        if (s.getDescription() == null || s.getDescription().trim().length() < 20) return "Description must be at least 20 characters";
        return serviceDAO.updateService(s) ? "Service updated successfully" : "Failed to update service.";
    }

    /**
     * Delete a service belonging to a given user.
     *
     * @param serviceId ID of the service to delete
     * @param userId    ID of the user requesting deletion (must be owner)
     * @return true if deleted successfully
     */
    public boolean deleteService(int serviceId, int userId) { return serviceDAO.deleteService(serviceId, userId); }

    /**
     * Retrieve a single service by its ID (with joined category and provider name).
     *
     * @param id Service ID
     * @return Service object, or null if not found
     */
    public Service getServiceById(int id) { return serviceDAO.getServiceById(id); }

    /**
     * Get all admin-approved services (for the browse page).
     *
     * @return List of approved services ordered by creation date (newest first)
     */
    public List<Service> getApprovedServices() { return serviceDAO.getApprovedServices(); }

    /**
     * Get all services awaiting admin approval (for the admin panel).
     *
     * @return List of pending services
     */
    public List<Service> getPendingServices() { return serviceDAO.getPendingServices(); }

    /**
     * Get all services posted by a specific user.
     *
     * @param userId The provider's user ID
     * @return List of the user's services
     */
    public List<Service> getUserServices(int userId) { return serviceDAO.getServicesByUserId(userId); }

    /**
     * Search approved services by keyword and/or category.
     *
     * @param kw    Search keyword (searches title and description); may be null
     * @param catId Category ID filter; may be null to skip category filtering
     * @return List of matching approved services
     */
    public List<Service> searchServices(String kw, Integer catId) { return serviceDAO.searchServices(kw, catId); }

    /**
     * Approve a service (admin action).
     *
     * @param id Service ID to approve
     * @return true if the status was updated
     */
    public boolean approveService(int id) { return serviceDAO.updateApprovalStatus(id, "APPROVED"); }

    /**
     * Reject a service (admin action).
     *
     * @param id Service ID to reject
     * @return true if the status was updated
     */
    public boolean rejectService(int id) { return serviceDAO.updateApprovalStatus(id, "REJECTED"); }
}
