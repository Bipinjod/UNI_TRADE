package com.unitrade.controller.admin;

import com.unitrade.service.AdminDashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * AdminDashboardServlet - Serves the admin statistics dashboard.
 * <p>
 * Loads all platform-wide counters (users, items, categories, services,
 * and help requests) via {@link AdminDashboardService} and forwards them
 * to the dashboard JSP for display.
 * </p>
 *
 * URL mapping: GET /admin/dashboard
 * Access:      Restricted to ADMIN role (enforced by {@code AdminFilter}).
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    /** Service that aggregates counts from all DAOs used by the dashboard. */
    private AdminDashboardService dashSvc;

    /**
     * Initialise the dashboard service once when the servlet is loaded.
     *
     * @throws ServletException if the parent init fails
     */
    @Override
    public void init() throws ServletException {
        super.init();
        dashSvc = new AdminDashboardService();
    }

    /**
     * Fetch all dashboard statistics and forward to the admin dashboard JSP.
     * <p>
     * If any stat fails to load an error message is set and the page is still
     * forwarded so the admin can see a partial view rather than a blank error page.
     * </p>
     *
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @throws ServletException if forwarding fails
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Populate all stat attributes used by the dashboard JSP
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
