package com.unitrade.controller.user;

import com.unitrade.dao.CategoryDAO;
import com.unitrade.model.*;
import com.unitrade.service.HelpRequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * HelpRequestServlet - User-facing controller for peer help requests.
 * <p>
 * Handles browsing all approved requests, viewing request details,
 * posting new requests, managing a user's own requests, and submitting
 * or accepting responses to requests.
 * </p>
 *
 * GET  /user/requests                     - Browse approved requests (default)
 * GET  /user/requests?action=detail       - View a single request and its responses
 * GET  /user/requests?action=add          - Show the "Post a Request" form
 * GET  /user/requests?action=my           - Show the current user's own requests
 * POST /user/requests?action=add          - Submit a new help request
 * POST /user/requests?action=delete       - Delete a user's own request
 * POST /user/requests?action=respond      - Submit a response to a request
 * POST /user/requests?action=acceptResponse - Accept a response (requester only)
 *
 * Access: Requires authenticated session (enforced by {@code AuthFilter}).
 */
@WebServlet("/user/requests")
public class HelpRequestServlet extends HttpServlet {

    /** Business logic for help requests and their responses. */
    private HelpRequestService hrSvc;
    /** DAO used to populate the category dropdown on the post-request form. */
    private CategoryDAO categoryDAO;

    /**
     * Initialise services on servlet load.
     *
     * @throws ServletException if parent init fails
     */
    @Override
    public void init() throws ServletException {
        hrSvc = new HelpRequestService();
        categoryDAO = new CategoryDAO();
    }

    /**
     * Route GET requests to the appropriate handler based on the {@code action} parameter.
     * Defaults to "browse" when no action is specified.
     *
     * @param req  the HTTP request
     * @param res  the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "browse";

        switch (action) {
            case "browse":  handleBrowse(req, res); break;
            case "detail":  handleDetail(req, res); break;
            case "add":     handleAddForm(req, res); break;
            case "my":      handleMy(req, res); break;
            default:        handleBrowse(req, res);
        }
    }

    /**
     * Route POST requests to the appropriate action handler.
     * Redirects unauthenticated users to the login page.
     *
     * @param req  the HTTP request
     * @param res  the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if a redirect or I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        String action = req.getParameter("action");
        if (action == null) { res.sendRedirect(req.getContextPath() + "/user/requests"); return; }

        switch (action) {
            case "add":      handleAdd(req, res, session, user); break;
            case "delete":   handleDelete(req, res, session, user); break;
            case "respond":  handleRespond(req, res, session, user); break;
            case "acceptResponse": handleAcceptResponse(req, res, session); break;
            default:         res.sendRedirect(req.getContextPath() + "/user/requests");
        }
    }

    // ── Private GET handlers ──────────────────────────────────────────────────

    /**
     * Load all admin-approved, open help requests and forward to the browse view.
     *
     * @param req the HTTP request
     * @param res the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if an I/O error occurs
     */
    private void handleBrowse(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("requests", hrSvc.getApprovedRequests());
        req.getRequestDispatcher("/user/help-requests.jsp").forward(req, res);
    }

    /**
     * Load a single help request by ID along with its responses and forward to the detail view.
     * Sets the {@code isOwner} flag so the JSP can conditionally show management controls.
     *
     * @param req the HTTP request (requires {@code requestId} parameter)
     * @param res the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if an I/O error occurs
     */
    private void handleDetail(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String idStr = req.getParameter("requestId");
        if (idStr == null) { handleBrowse(req, res); return; }
        HelpRequest hr = hrSvc.getRequestById(Integer.parseInt(idStr));
        if (hr == null) { req.setAttribute("error", "Request not found"); handleBrowse(req, res); return; }

        List<RequestResponse> responses = hrSvc.getResponsesForRequest(hr.getRequestId());
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        // Determine ownership so the JSP can show/hide accept-response buttons
        boolean isOwner = user != null && hr.getUserId() == user.getUserId();

        req.setAttribute("helpRequest", hr);
        req.setAttribute("responses", responses);
        req.setAttribute("isOwner", isOwner);
        req.getRequestDispatcher("/user/help-requests.jsp").forward(req, res);
    }

    /**
     * Load active REQUEST-type categories and forward to the post-request form.
     *
     * @param req the HTTP request
     * @param res the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if an I/O error occurs
     */
    private void handleAddForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("categories", categoryDAO.getActiveCategoriesByType("REQUEST"));
        req.setAttribute("formAction", "add");
        req.getRequestDispatcher("/user/post-request.jsp").forward(req, res);
    }

    /**
     * Load all requests submitted by the currently logged-in user and forward to the list view.
     *
     * @param req the HTTP request
     * @param res the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if an I/O error occurs
     */
    private void handleMy(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }
        req.setAttribute("requests", hrSvc.getUserRequests(user.getUserId()));
        req.setAttribute("viewMode", "my");
        req.getRequestDispatcher("/user/help-requests.jsp").forward(req, res);
    }

    // ── Private POST handlers ─────────────────────────────────────────────────

    /**
     * Parse form parameters and submit a new help request via the service layer.
     * Redirects to the user's own requests view on completion.
     *
     * @param req     the HTTP request
     * @param res     the HTTP response
     * @param session the current HTTP session (for flash messages)
     * @param user    the authenticated user submitting the request
     * @throws IOException if a redirect occurs
     */
    private void handleAdd(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        HelpRequest hr = new HelpRequest();
        hr.setUserId(user.getUserId());
        // Parse and validate categoryId — redirect back to form on failure
        try { hr.setCategoryId(Integer.parseInt(req.getParameter("categoryId"))); } catch (Exception e) { session.setAttribute("error", "Invalid category"); res.sendRedirect(req.getContextPath() + "/user/requests?action=add"); return; }
        hr.setTitle(req.getParameter("title"));
        hr.setDescription(req.getParameter("description"));
        // Budget defaults to 0 if the user left the field blank / entered invalid text
        try { hr.setBudget(new BigDecimal(req.getParameter("budget"))); } catch (Exception e) { hr.setBudget(BigDecimal.ZERO); }
        String urgency = req.getParameter("urgencyLevel");
        hr.setUrgencyLevel(urgency != null ? urgency : "MEDIUM");

        String result = hrSvc.addRequest(hr);
        // Determine flash message key based on success/failure
        session.setAttribute(result.contains("successfully") || result.contains("Waiting") ? "success" : "error", result);
        res.sendRedirect(req.getContextPath() + "/user/requests?action=my");
    }

    /**
     * Delete the user's own help request (ownership is enforced by the service/DAO).
     *
     * @param req     the HTTP request (requires {@code requestId} parameter)
     * @param res     the HTTP response
     * @param session the current HTTP session (for flash messages)
     * @param user    the authenticated user requesting deletion
     * @throws IOException if a redirect occurs
     */
    private void handleDelete(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String idStr = req.getParameter("requestId");
        if (idStr != null && hrSvc.deleteRequest(Integer.parseInt(idStr), user.getUserId())) {
            session.setAttribute("success", "Request deleted");
        } else {
            session.setAttribute("error", "Failed to delete");
        }
        res.sendRedirect(req.getContextPath() + "/user/requests?action=my");
    }

    /**
     * Submit a response (offer to help) for an existing help request.
     * Redirects back to the request detail page after submission.
     *
     * @param req     the HTTP request (requires {@code requestId} and {@code responseMessage})
     * @param res     the HTTP response
     * @param session the current HTTP session (for flash messages)
     * @param user    the authenticated user submitting the response
     * @throws IOException if a redirect occurs
     */
    private void handleRespond(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String idStr = req.getParameter("requestId");
        if (idStr == null) { session.setAttribute("error", "Request ID required"); res.sendRedirect(req.getContextPath() + "/user/requests"); return; }

        RequestResponse rr = new RequestResponse();
        rr.setRequestId(Integer.parseInt(idStr));
        rr.setResponderId(user.getUserId());
        rr.setResponseMessage(req.getParameter("responseMessage"));

        String result = hrSvc.addResponse(rr);
        session.setAttribute(result.contains("submitted") ? "success" : "error", result);
        res.sendRedirect(req.getContextPath() + "/user/requests?action=detail&requestId=" + idStr);
    }

    /**
     * Accept a specific response on a help request (requester-only action).
     * Redirects back to the request detail page.
     *
     * @param req     the HTTP request (requires {@code responseId} and {@code requestId})
     * @param res     the HTTP response
     * @param session the current HTTP session (for flash messages)
     * @throws IOException if a redirect occurs
     */
    private void handleAcceptResponse(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException {
        String respIdStr = req.getParameter("responseId");
        String reqIdStr  = req.getParameter("requestId");
        if (respIdStr != null) {
            // Mark the chosen response as ACCEPTED so the responder knows their offer was taken
            hrSvc.acceptResponse(Integer.parseInt(respIdStr));
            session.setAttribute("success", "Response accepted");
        }
        res.sendRedirect(req.getContextPath() + "/user/requests?action=detail&requestId=" + (reqIdStr != null ? reqIdStr : ""));
    }
}
