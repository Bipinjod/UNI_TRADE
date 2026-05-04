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
 * HelpRequestServlet – browse, post, respond to help requests.
 */
@WebServlet("/user/requests")
public class HelpRequestServlet extends HttpServlet {

    private HelpRequestService hrSvc;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        hrSvc = new HelpRequestService();
        categoryDAO = new CategoryDAO();
    }

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

    private void handleBrowse(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("requests", hrSvc.getApprovedRequests());
        req.getRequestDispatcher("/user/help-requests.jsp").forward(req, res);
    }

    private void handleDetail(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String idStr = req.getParameter("requestId");
        if (idStr == null) { handleBrowse(req, res); return; }
        HelpRequest hr = hrSvc.getRequestById(Integer.parseInt(idStr));
        if (hr == null) { req.setAttribute("error", "Request not found"); handleBrowse(req, res); return; }

        List<RequestResponse> responses = hrSvc.getResponsesForRequest(hr.getRequestId());
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        boolean isOwner = user != null && hr.getUserId() == user.getUserId();

        req.setAttribute("helpRequest", hr);
        req.setAttribute("responses", responses);
        req.setAttribute("isOwner", isOwner);
        req.getRequestDispatcher("/user/help-requests.jsp").forward(req, res);
    }

    private void handleAddForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("categories", categoryDAO.getActiveCategoriesByType("REQUEST"));
        req.setAttribute("formAction", "add");
        req.getRequestDispatcher("/user/post-request.jsp").forward(req, res);
    }

    private void handleMy(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }
        req.setAttribute("requests", hrSvc.getUserRequests(user.getUserId()));
        req.setAttribute("viewMode", "my");
        req.getRequestDispatcher("/user/help-requests.jsp").forward(req, res);
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        HelpRequest hr = new HelpRequest();
        hr.setUserId(user.getUserId());
        try { hr.setCategoryId(Integer.parseInt(req.getParameter("categoryId"))); } catch (Exception e) { session.setAttribute("error", "Invalid category"); res.sendRedirect(req.getContextPath() + "/user/requests?action=add"); return; }
        hr.setTitle(req.getParameter("title"));
        hr.setDescription(req.getParameter("description"));
        try { hr.setBudget(new BigDecimal(req.getParameter("budget"))); } catch (Exception e) { hr.setBudget(BigDecimal.ZERO); }
        String urgency = req.getParameter("urgencyLevel");
        hr.setUrgencyLevel(urgency != null ? urgency : "MEDIUM");

        String result = hrSvc.addRequest(hr);
        session.setAttribute(result.contains("successfully") || result.contains("Waiting") ? "success" : "error", result);
        res.sendRedirect(req.getContextPath() + "/user/requests?action=my");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String idStr = req.getParameter("requestId");
        if (idStr != null && hrSvc.deleteRequest(Integer.parseInt(idStr), user.getUserId())) {
            session.setAttribute("success", "Request deleted");
        } else {
            session.setAttribute("error", "Failed to delete");
        }
        res.sendRedirect(req.getContextPath() + "/user/requests?action=my");
    }

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

    private void handleAcceptResponse(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException {
        String respIdStr = req.getParameter("responseId");
        String reqIdStr = req.getParameter("requestId");
        if (respIdStr != null) {
            hrSvc.acceptResponse(Integer.parseInt(respIdStr));
            session.setAttribute("success", "Response accepted");
        }
        res.sendRedirect(req.getContextPath() + "/user/requests?action=detail&requestId=" + (reqIdStr != null ? reqIdStr : ""));
    }
}

