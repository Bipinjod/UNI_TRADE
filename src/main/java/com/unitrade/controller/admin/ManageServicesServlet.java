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
 * ManageServicesServlet – admin moderation of peer services.
 */
@WebServlet("/admin/services")
public class ManageServicesServlet extends HttpServlet {

    private ServiceListingService svc;

    @Override public void init() throws ServletException { svc = new ServiceListingService(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String filter = req.getParameter("filter");
        List<Service> services;
        if ("approved".equals(filter)) {
            services = svc.getApprovedServices();
            req.setAttribute("filterType", "Approved Services");
        } else {
            services = svc.getPendingServices();
            req.setAttribute("filterType", "Pending Services");
        }
        req.setAttribute("services", services);
        req.setAttribute("pendingCount", svc.getPendingServices().size());
        req.getRequestDispatcher("/admin/manage-services.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        String idStr = req.getParameter("serviceId");

        if (action == null || idStr == null) { session.setAttribute("error", "Invalid request"); res.sendRedirect(req.getContextPath() + "/admin/services"); return; }

        int id = Integer.parseInt(idStr);
        boolean ok = switch (action) {
            case "approve" -> svc.approveService(id);
            case "reject"  -> svc.rejectService(id);
            default -> false;
        };
        session.setAttribute(ok ? "success" : "error", ok ? "Service " + action + "d successfully" : "Failed to " + action + " service");
        res.sendRedirect(req.getContextPath() + "/admin/services");
    }
}

