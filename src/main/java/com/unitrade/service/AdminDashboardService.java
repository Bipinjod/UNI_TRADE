package com.unitrade.service;

import com.unitrade.dao.*;

/**
 * AdminDashboardService - Aggregates statistics for the admin dashboard.
 * <p>
 * Acts as a facade over the individual DAOs, providing a single convenient
 * service that the {@code AdminDashboardServlet} can query to populate
 * all dashboard counters in one place.
 * </p>
 */
public class AdminDashboardService {

    /** DAO for user-related statistics. */
    private final UserDAO userDAO = new UserDAO();
    /** DAO for item-related statistics. */
    private final ItemDAO itemDAO = new ItemDAO();
    /** DAO for category-related statistics. */
    private final CategoryDAO categoryDAO = new CategoryDAO();
    /** DAO for service-listing statistics. */
    private final ServiceDAO serviceDAO = new ServiceDAO();
    /** DAO for help-request statistics. */
    private final HelpRequestDAO helpRequestDAO = new HelpRequestDAO();

    /**
     * @return Total number of registered users (all statuses).
     */
    public int getTotalUsers()       { return userDAO.getAllUsers().size(); }

    /**
     * @return Number of users whose approval status is PENDING.
     */
    public int getPendingUsers()     { return userDAO.getPendingUsers().size(); }

    /**
     * @return Total number of items (approved + pending combined).
     */
    public int getTotalItems()       { return itemDAO.getAllApprovedItems().size() + itemDAO.getPendingItems().size(); }

    /**
     * @return Number of items awaiting admin approval (listing_status = PENDING).
     */
    public int getPendingItems()     { return itemDAO.getPendingItems().size(); }

    /**
     * @return Total number of categories (active and inactive).
     */
    public int getTotalCategories()  { return categoryDAO.getAllCategories().size(); }

    /**
     * @return Number of categories with status = ACTIVE.
     */
    public int getActiveCategories() { return categoryDAO.getActiveCategories().size(); }

    /**
     * @return Number of peer services awaiting admin approval.
     */
    public int getPendingServices()  { return serviceDAO.getPendingServices().size(); }

    /**
     * @return Number of peer services that have been approved by an admin.
     */
    public int getApprovedServices() { return serviceDAO.getApprovedServices().size(); }

    /**
     * @return Number of help requests awaiting admin approval.
     */
    public int getPendingRequests()  { return helpRequestDAO.getPendingRequests().size(); }

    /**
     * @return Number of help requests that have been approved by an admin.
     */
    public int getApprovedRequests() { return helpRequestDAO.getApprovedRequests().size(); }
}
