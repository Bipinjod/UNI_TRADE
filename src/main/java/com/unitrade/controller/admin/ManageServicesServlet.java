package com.unitrade.controller.admin;

import com.unitrade.model.Service;
import com.unitrade.service.ServiceListingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * ManageServicesServlet - Admin moderation controller for peer service listings.
 * <p>
 * Allows administrators to view all pending and approved service listings and
 * to approve or reject each one before they appear on the student-facing browse page.
 * </p>
 *
 * GET  /admin/services                    - List services (default: pending)
 * GET  /admin/services?filter=approved    - List approved services
 * POST /admin/services?action=approve     - Approve a service
 * POST /admin/services?action=reject      - Reject a service
 *
 * Access: Restricted to ADMIN role (enforced by {@code AdminFilter}).
 */
@WebServlet("/admin/services")
public class ManageServicesServlet extends HttpServlet {

    /** Business logic service for peer service listings. */
    private ServiceListingService svc;

    /**
     * Initialise the service listing service on servlet load.
     *
     * @throws ServletException if the parent init fails
     */
    @Override
    public void init() throws ServletException { svc = new ServiceListingService(); }

    /**
     * Display the list of service listings filtered by approval status.
     * <p>
     * Defaults to PENDING services so the admin sees items needing action first.
     * Pass {@code filter=approved} to see already-approved services.
     * </p>
     *
     * @param req  the HTTP request (optional {@code filter} parameter)
     * @param res  the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String filter = req.getParameter("filter");
        List<Service> services;
        if ("approved".equals(filter)) {
            // Switch to viewing already-approved services
            services = svc.getApprovedServices();
            req.setAttribute("filterType", "Approved Services");
        } else {
            // Default: pending services awaiting moderation
            services = svc.getPendingServices();
            req.setAttribute("filterType", "Pending Services");
        }
        req.setAttribute("services", services);
        // Badge counter shown in the admin sidebar
        req.setAttribute("pendingCount", svc.getPendingServices().size());
        req.getRequestDispatcher("/admin/manage-services.jsp").forward(req, res);
    }

    /**
     * Process approve or reject actions submitted from the moderation form.
     * <p>
     * Expects {@code action} (approve / reject) and {@code serviceId} as POST
     * parameters. Stores the result as a flash message and redirects back.
     * </p>
     *
     * @param req  the HTTP request
     * @param res  the HTTP response
     * @throws ServletException not thrown directly
     * @throws IOException      if the redirect fails
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        String idStr  = req.getParameter("serviceId");

        // Guard: both parameters are required
        if (action == null || idStr == null) {
            session.setAttribute("error", "Invalid request");
            res.sendRedirect(req.getContextPath() + "/admin/services");
            return;
        }

        int id = Integer.parseInt(idStr);
        // Map action string to the appropriate service method
        boolean ok = switch (action) {
            case "approve" -> svc.approveService(id);
            case "reject"  -> svc.rejectService(id);
            default        -> false;
        };
        // Persist flash message for display after redirect
        session.setAttribute(ok ? "success" : "error",
                ok ? "Service " + action + "d successfully" : "Failed to " + action + " service");
        res.sendRedirect(req.getContextPath() + "/admin/services");
    }
}
