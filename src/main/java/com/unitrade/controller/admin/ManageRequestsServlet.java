package com.unitrade.controller.admin;

import com.unitrade.model.HelpRequest;
import com.unitrade.service.HelpRequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * ManageRequestsServlet - Admin moderation controller for peer help requests.
 * <p>
 * Allows administrators to view all pending and approved help requests and
 * to approve or reject each one to control what appears on the student-facing
 * browse page.
 * </p>
 *
 * GET  /admin/requests             - List requests (default: pending)
 * GET  /admin/requests?filter=approved - List approved requests
 * POST /admin/requests?action=approve  - Approve a help request
 * POST /admin/requests?action=reject   - Reject a help request
 *
 * Access: Restricted to ADMIN role (enforced by {@code AdminFilter}).
 */
@WebServlet("/admin/requests")
public class ManageRequestsServlet extends HttpServlet {

    /** Business logic service for help requests and responses. */
    private HelpRequestService hrSvc;

    /**
     * Initialise the service on servlet load.
     *
     * @throws ServletException if the parent init fails
     */
    @Override
    public void init() throws ServletException { hrSvc = new HelpRequestService(); }

    /**
     * Display the list of help requests filtered by status.
     * <p>
     * Defaults to showing PENDING requests so the admin immediately sees
     * content that needs action. Pass {@code filter=approved} to switch view.
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
        List<HelpRequest> requests;
        if ("approved".equals(filter)) {
            // Show already-approved requests for review
            requests = hrSvc.getApprovedRequests();
            req.setAttribute("filterType", "Approved Requests");
        } else {
            // Default: show pending requests awaiting action
            requests = hrSvc.getPendingRequests();
            req.setAttribute("filterType", "Pending Requests");
        }
        req.setAttribute("requests", requests);
        // Badge counter for pending items shown in the sidebar
        req.setAttribute("pendingCount", hrSvc.getPendingRequests().size());
        req.getRequestDispatcher("/admin/manage-requests.jsp").forward(req, res);
    }

    /**
     * Process approve or reject actions submitted from the form.
     * <p>
     * Reads {@code action} (approve / reject) and {@code requestId} from the
     * POST body, delegates to the service, stores the outcome in the flash
     * session, then redirects back to the requests list.
     * </p>
     *
     * @param req  the HTTP request containing {@code action} and {@code requestId}
     * @param res  the HTTP response
     * @throws ServletException not thrown directly
     * @throws IOException      if the redirect fails
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        String idStr  = req.getParameter("requestId");

        // Validate required parameters before acting
        if (action == null || idStr == null) {
            session.setAttribute("error", "Invalid request");
            res.sendRedirect(req.getContextPath() + "/admin/requests");
            return;
        }

        int id = Integer.parseInt(idStr);
        // Delegate to service and map action string to the correct operation
        boolean ok = switch (action) {
            case "approve" -> hrSvc.approveRequest(id);
            case "reject"  -> hrSvc.rejectRequest(id);
            default        -> false;
        };
        // Store success or error flash message for the redirected page
        session.setAttribute(ok ? "success" : "error",
                ok ? "Request " + action + "d successfully" : "Failed to " + action + " request");
        res.sendRedirect(req.getContextPath() + "/admin/requests");
    }
}
