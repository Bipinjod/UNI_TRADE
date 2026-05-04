package com.unitrade.controller.admin;

import com.unitrade.service.AdminDashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * AdminDashboardServlet - Admin Dashboard with full stats.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private AdminDashboardService dashSvc;

    @Override
    public void init() throws ServletException {
        super.init();
        dashSvc = new AdminDashboardService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("totalUsers",       dashSvc.getTotalUsers());
            request.setAttribute("pendingUsers",     dashSvc.getPendingUsers());
            request.setAttribute("totalItems",       dashSvc.getTotalItems());
            request.setAttribute("pendingItems",     dashSvc.getPendingItems());
            request.setAttribute("totalCategories",  dashSvc.getTotalCategories());
            request.setAttribute("activeCategories", dashSvc.getActiveCategories());
            request.setAttribute("pendingServices",  dashSvc.getPendingServices());
            request.setAttribute("approvedServices", dashSvc.getApprovedServices());
            request.setAttribute("pendingRequests",  dashSvc.getPendingRequests());
            request.setAttribute("approvedRequests", dashSvc.getApprovedRequests());

            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load dashboard data");
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }
}
