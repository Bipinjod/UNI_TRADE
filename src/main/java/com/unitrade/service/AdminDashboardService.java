package com.unitrade.service;

import com.unitrade.dao.*;

/**
 * AdminDashboardService – aggregates stats for the admin dashboard.
 */
public class AdminDashboardService {

    private final UserDAO userDAO = new UserDAO();
    private final ItemDAO itemDAO = new ItemDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final HelpRequestDAO helpRequestDAO = new HelpRequestDAO();

    public int getTotalUsers()       { return userDAO.getAllUsers().size(); }
    public int getPendingUsers()     { return userDAO.getPendingUsers().size(); }
    public int getTotalItems()       { return itemDAO.getAllApprovedItems().size() + itemDAO.getPendingItems().size(); }
    public int getPendingItems()     { return itemDAO.getPendingItems().size(); }
    public int getTotalCategories()  { return categoryDAO.getAllCategories().size(); }
    public int getActiveCategories() { return categoryDAO.getActiveCategories().size(); }
    public int getPendingServices()  { return serviceDAO.getPendingServices().size(); }
    public int getApprovedServices() { return serviceDAO.getApprovedServices().size(); }
    public int getPendingRequests()  { return helpRequestDAO.getPendingRequests().size(); }
    public int getApprovedRequests() { return helpRequestDAO.getApprovedRequests().size(); }
}

