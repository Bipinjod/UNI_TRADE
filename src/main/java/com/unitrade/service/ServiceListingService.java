package com.unitrade.service;

import com.unitrade.dao.ServiceDAO;
import com.unitrade.model.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * ServiceListingService – business logic for peer services.
 */
public class ServiceListingService {

    private final ServiceDAO serviceDAO = new ServiceDAO();

    public String addService(Service s) {
        if (s.getUserId() <= 0) return "Invalid user";
        if (s.getCategoryId() <= 0) return "Please select a category";
        if (s.getTitle() == null || s.getTitle().trim().isEmpty()) return "Title is required";
        if (s.getDescription() == null || s.getDescription().trim().length() < 20) return "Description must be at least 20 characters";
        if (s.getPrice() == null || s.getPrice().compareTo(BigDecimal.ZERO) < 0) return "Invalid price";
        s.setAvailabilityStatus("AVAILABLE");
        s.setApprovalStatus("PENDING");
        return serviceDAO.addService(s) ? "Service posted successfully! Waiting for admin approval." : "Failed to post service.";
    }

    public String updateService(Service s) {
        if (s.getServiceId() <= 0) return "Invalid service ID";
        if (s.getTitle() == null || s.getTitle().trim().isEmpty()) return "Title is required";
        if (s.getDescription() == null || s.getDescription().trim().length() < 20) return "Description must be at least 20 characters";
        return serviceDAO.updateService(s) ? "Service updated successfully" : "Failed to update service.";
    }

    public boolean deleteService(int serviceId, int userId) { return serviceDAO.deleteService(serviceId, userId); }
    public Service getServiceById(int id) { return serviceDAO.getServiceById(id); }
    public List<Service> getApprovedServices() { return serviceDAO.getApprovedServices(); }
    public List<Service> getPendingServices() { return serviceDAO.getPendingServices(); }
    public List<Service> getUserServices(int userId) { return serviceDAO.getServicesByUserId(userId); }
    public List<Service> searchServices(String kw, Integer catId) { return serviceDAO.searchServices(kw, catId); }
    public boolean approveService(int id) { return serviceDAO.updateApprovalStatus(id, "APPROVED"); }
    public boolean rejectService(int id) { return serviceDAO.updateApprovalStatus(id, "REJECTED"); }
}

