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
 * ManageRequestsServlet – admin moderation of help requests.
 */
@WebServlet("/admin/requests")
public class ManageRequestsServlet extends HttpServlet {

    private HelpRequestService hrSvc;

    @Override public void init() throws ServletException { hrSvc = new HelpRequestService(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String filter = req.getParameter("filter");
        List<HelpRequest> requests;
        if ("approved".equals(filter)) {
            requests = hrSvc.getApprovedRequests();
            req.setAttribute("filterType", "Approved Requests");
        } else {
            requests = hrSvc.getPendingRequests();
            req.setAttribute("filterType", "Pending Requests");
        }
        req.setAttribute("requests", requests);
        req.setAttribute("pendingCount", hrSvc.getPendingRequests().size());
        req.getRequestDispatcher("/admin/manage-requests.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        String idStr = req.getParameter("requestId");

        if (action == null || idStr == null) { session.setAttribute("error", "Invalid request"); res.sendRedirect(req.getContextPath() + "/admin/requests"); return; }

        int id = Integer.parseInt(idStr);
        boolean ok = switch (action) {
            case "approve" -> hrSvc.approveRequest(id);
            case "reject"  -> hrSvc.rejectRequest(id);
            default -> false;
        };
        session.setAttribute(ok ? "success" : "error", ok ? "Request " + action + "d successfully" : "Failed to " + action + " request");
        res.sendRedirect(req.getContextPath() + "/admin/requests");
    }
}

