package com.unitrade.controller.user;

import com.unitrade.dao.CategoryDAO;
import com.unitrade.model.Category;
import com.unitrade.model.Service;
import com.unitrade.model.ServiceOrder;
import com.unitrade.model.User;
import com.unitrade.service.ServiceListingService;
import com.unitrade.service.ServiceOrderService;
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
 * ServiceServlet – browse, post, edit, delete peer services.
 */
@WebServlet("/user/services")
public class ServiceServlet extends HttpServlet {

    private ServiceListingService svc;
    private ServiceOrderService soSvc;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        svc = new ServiceListingService();
        soSvc = new ServiceOrderService();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "browse";

        switch (action) {
            case "browse":   handleBrowse(req, res); break;
            case "detail":   handleDetail(req, res); break;
            case "add":      handleAddForm(req, res); break;
            case "edit":     handleEditForm(req, res); break;
            case "my":       handleMy(req, res); break;
            default:         handleBrowse(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        String action = req.getParameter("action");
        if (action == null) { res.sendRedirect(req.getContextPath() + "/user/services"); return; }

        switch (action) {
            case "add":    handleAdd(req, res, session, user); break;
            case "edit":   handleEdit(req, res, session, user); break;
            case "delete": handleDelete(req, res, session, user); break;
            case "order":  handleOrder(req, res, session, user); break;
            default:       res.sendRedirect(req.getContextPath() + "/user/services");
        }
    }

    private void handleBrowse(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String catIdStr = req.getParameter("categoryId");
        Integer catId = null;
        if (catIdStr != null && !catIdStr.isEmpty()) { try { catId = Integer.parseInt(catIdStr); } catch (NumberFormatException ignored) {} }

        List<Service> services = (keyword != null || catId != null) ? svc.searchServices(keyword, catId) : svc.getApprovedServices();
        List<Category> categories = categoryDAO.getActiveCategoriesByType("SERVICE");

        req.setAttribute("services", services);
        req.setAttribute("categories", categories);
        req.setAttribute("keyword", keyword);
        req.setAttribute("selectedCategoryId", catId);
        req.getRequestDispatcher("/user/browse-services.jsp").forward(req, res);
    }

    private void handleDetail(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String idStr = req.getParameter("serviceId");
        if (idStr == null) { handleBrowse(req, res); return; }
        try {
            Service s = svc.getServiceById(Integer.parseInt(idStr));
            if (s == null) { req.setAttribute("error", "Service not found"); handleBrowse(req, res); return; }
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("loggedInUser");
            boolean isOwner = user != null && s.getUserId() == user.getUserId();
            req.setAttribute("service", s);
            req.setAttribute("isOwner", isOwner);
            req.getRequestDispatcher("/user/service-detail.jsp").forward(req, res);
        } catch (NumberFormatException e) { handleBrowse(req, res); }
    }

    private void handleAddForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("categories", categoryDAO.getActiveCategoriesByType("SERVICE"));
        req.setAttribute("formAction", "add");
        req.getRequestDispatcher("/user/post-service.jsp").forward(req, res);
    }

    private void handleEditForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String idStr = req.getParameter("serviceId");
        if (idStr == null) { res.sendRedirect(req.getContextPath() + "/user/services?action=my"); return; }
        Service s = svc.getServiceById(Integer.parseInt(idStr));
        req.setAttribute("service", s);
        req.setAttribute("categories", categoryDAO.getActiveCategoriesByType("SERVICE"));
        req.setAttribute("formAction", "edit");
        req.getRequestDispatcher("/user/post-service.jsp").forward(req, res);
    }

    private void handleMy(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }
        req.setAttribute("services", svc.getUserServices(user.getUserId()));
        req.getRequestDispatcher("/user/my-services.jsp").forward(req, res);
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        Service s = new Service();
        s.setUserId(user.getUserId());
        try { s.setCategoryId(Integer.parseInt(req.getParameter("categoryId"))); } catch (Exception e) { session.setAttribute("error", "Invalid category"); res.sendRedirect(req.getContextPath() + "/user/services?action=add"); return; }
        s.setTitle(req.getParameter("title"));
        s.setDescription(req.getParameter("description"));
        try { s.setPrice(new BigDecimal(req.getParameter("price"))); } catch (Exception e) { session.setAttribute("error", "Invalid price"); res.sendRedirect(req.getContextPath() + "/user/services?action=add"); return; }

        String result = svc.addService(s);
        session.setAttribute(result.contains("successfully") ? "success" : "error", result);
        res.sendRedirect(req.getContextPath() + "/user/services?action=my");
    }

    private void handleEdit(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String idStr = req.getParameter("serviceId");
        if (idStr == null) { session.setAttribute("error", "Service ID required"); res.sendRedirect(req.getContextPath() + "/user/services?action=my"); return; }
        Service s = svc.getServiceById(Integer.parseInt(idStr));
        if (s == null || s.getUserId() != user.getUserId()) { session.setAttribute("error", "Not authorized"); res.sendRedirect(req.getContextPath() + "/user/services?action=my"); return; }
        try { s.setCategoryId(Integer.parseInt(req.getParameter("categoryId"))); } catch (Exception ignored) {}
        s.setTitle(req.getParameter("title"));
        s.setDescription(req.getParameter("description"));
        try { s.setPrice(new BigDecimal(req.getParameter("price"))); } catch (Exception ignored) {}
        String avail = req.getParameter("availabilityStatus");
        if (avail != null) s.setAvailabilityStatus(avail);

        String result = svc.updateService(s);
        session.setAttribute(result.contains("successfully") ? "success" : "error", result);
        res.sendRedirect(req.getContextPath() + "/user/services?action=my");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String idStr = req.getParameter("serviceId");
        if (idStr != null && svc.deleteService(Integer.parseInt(idStr), user.getUserId())) {
            session.setAttribute("success", "Service deleted");
        } else {
            session.setAttribute("error", "Failed to delete");
        }
        res.sendRedirect(req.getContextPath() + "/user/services?action=my");
    }

    private void handleOrder(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String idStr = req.getParameter("serviceId");
        if (idStr == null) { session.setAttribute("error", "Service ID required"); res.sendRedirect(req.getContextPath() + "/user/services"); return; }
        Service s = svc.getServiceById(Integer.parseInt(idStr));
        if (s == null) { session.setAttribute("error", "Service not found"); res.sendRedirect(req.getContextPath() + "/user/services"); return; }

        ServiceOrder order = new ServiceOrder();
        order.setServiceId(s.getServiceId());
        order.setBuyerId(user.getUserId());
        order.setProviderId(s.getUserId());
        order.setRequestMessage(req.getParameter("message"));

        String result = soSvc.createOrder(order);
        session.setAttribute(result.contains("successfully") ? "success" : "error", result);
        res.sendRedirect(req.getContextPath() + "/user/services?action=detail&serviceId=" + s.getServiceId());
    }
}

