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
 * ServiceServlet – central controller for the peer-services module.
 * <p>
 * Mapped to {@code /user/services}, this servlet uses an
 * <b>action-based routing pattern</b>: the {@code action} request parameter
 * selects which private helper method handles the request. This keeps the
 * controller small, readable, and easy to extend.
 * </p>
 *
 * <p><b>Supported GET actions</b> (read-only — no login required for browse/detail):</p>
 * <ul>
 *   <li>{@code browse}  – list approved services, optionally filtered by keyword/category</li>
 *   <li>{@code detail}  – show a single service-detail page</li>
 *   <li>{@code add}     – render the "post a service" form</li>
 *   <li>{@code edit}    – render the edit-service form (owner only)</li>
 *   <li>{@code my}      – list the current user's own services and received orders</li>
 * </ul>
 *
 * <p><b>Supported POST actions</b> (all require a logged-in user):</p>
 * <ul>
 *   <li>{@code add}            – persist a new service listing (status PENDING)</li>
 *   <li>{@code edit}           – update an existing service (owner only)</li>
 *   <li>{@code delete}         – delete a service (owner only)</li>
 *   <li>{@code order}          – buyer requests a service from a provider</li>
 *   <li>{@code acceptOrder} / {@code rejectOrder} / {@code completeOrder} / {@code cancelOrder}
 *       – update a service order's status</li>
 * </ul>
 *
 * <p>Delegates persistence to {@link ServiceListingService} and
 * {@link ServiceOrderService}, and reads categories via {@link CategoryDAO}.</p>
 */
@WebServlet("/user/services")
public class ServiceServlet extends HttpServlet {

    /** Business-logic facade for service CRUD and search operations. */
    private ServiceListingService svc;
    /** Business-logic facade for service-order CRUD and status updates. */
    private ServiceOrderService soSvc;
    /** DAO used to load the dropdown list of SERVICE-typed categories. */
    private CategoryDAO categoryDAO;

    /**
     * Initialise dependencies once when the servlet is loaded by the container.
     * Instantiating service objects here avoids re-creating them on every request.
     *
     * @throws ServletException if the parent initialisation fails
     */
    @Override
    public void init() throws ServletException {
        svc = new ServiceListingService();
        soSvc = new ServiceOrderService();
        categoryDAO = new CategoryDAO();
    }

    /**
     * GET dispatcher – routes the request to the appropriate handler
     * based on the {@code action} query parameter. Defaults to "browse"
     * when no action is supplied.
     *
     * @param req incoming HTTP GET request
     * @param res outgoing HTTP response
     * @throws ServletException if a forwarded JSP throws
     * @throws IOException      on I/O failure during forward/redirect
     */
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

    /**
     * POST dispatcher – enforces a session guard (must be logged in)
     * and then routes write/mutation actions to the correct handler.
     * Unknown actions fall back to the browse page.
     *
     * @param req incoming HTTP POST request
     * @param res outgoing HTTP response
     * @throws ServletException if a forwarded JSP throws
     * @throws IOException      on I/O failure during forward/redirect
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        // Session guard — only authenticated users can POST to this endpoint
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        String action = req.getParameter("action");
        if (action == null) { res.sendRedirect(req.getContextPath() + "/user/services"); return; }

        switch (action) {
            case "add":           handleAdd(req, res, session, user); break;
            case "edit":          handleEdit(req, res, session, user); break;
            case "delete":        handleDelete(req, res, session, user); break;
            case "order":         handleOrder(req, res, session, user); break;
            // All four status-change actions share one handler — distinguished by the action string
            case "acceptOrder":
            case "rejectOrder":
            case "completeOrder":
            case "cancelOrder":   handleOrderStatus(req, res, session, user, action); break;
            default:              res.sendRedirect(req.getContextPath() + "/user/services");
        }
    }

    /**
     * Handle the public "browse services" page.
     * Applies optional keyword and category filters; otherwise lists all
     * admin-approved services. Populates the category dropdown for the
     * search form and forwards to {@code browse-services.jsp}.
     *
     * @param req incoming request (may contain {@code keyword} and {@code categoryId})
     * @param res response used to forward to the JSP
     * @throws ServletException if the JSP throws
     * @throws IOException      on I/O failure
     */
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
        // Parse the serviceId — if missing or malformed, fall back to the browse page
        String idStr = req.getParameter("serviceId");
        if (idStr == null) { handleBrowse(req, res); return; }
        try {
            Service s = svc.getServiceById(Integer.parseInt(idStr));
            if (s == null) { req.setAttribute("error", "Service not found"); handleBrowse(req, res); return; }
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("loggedInUser");
            // Owner flag controls visibility of Edit/Delete buttons in the JSP
            boolean isOwner = user != null && s.getUserId() == user.getUserId();
            req.setAttribute("service", s);
            req.setAttribute("isOwner", isOwner);
            req.getRequestDispatcher("/user/service-detail.jsp").forward(req, res);
        } catch (NumberFormatException e) { handleBrowse(req, res); }
    }

    /**
     * Render the "post a new service" form.
     * Loads the list of active SERVICE-typed categories for the dropdown
     * and signals to the JSP that this is an "add" (not "edit") submission.
     */
    private void handleAddForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("categories", categoryDAO.getActiveCategoriesByType("SERVICE"));
        req.setAttribute("formAction", "add");
        req.getRequestDispatcher("/user/post-service.jsp").forward(req, res);
    }

    /**
     * Render the "edit service" form pre-filled with the existing service data.
     * If the {@code serviceId} parameter is missing the user is redirected
     * back to their "My Services" page.
     */
    private void handleEditForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String idStr = req.getParameter("serviceId");
        if (idStr == null) { res.sendRedirect(req.getContextPath() + "/user/services?action=my"); return; }
        Service s = svc.getServiceById(Integer.parseInt(idStr));
        req.setAttribute("service", s);
        req.setAttribute("categories", categoryDAO.getActiveCategoriesByType("SERVICE"));
        req.setAttribute("formAction", "edit");
        req.getRequestDispatcher("/user/post-service.jsp").forward(req, res);
    }

    /**
     * Show the current user's "My Services" dashboard.
     * Lists both the services this user has posted AND the service orders
     * other users have placed against them (as a provider).
     * Requires a logged-in session.
     */
    private void handleMy(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        // Session guard — redirect anonymous users to login
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }
        req.setAttribute("services", svc.getUserServices(user.getUserId()));
        req.setAttribute("receivedOrders", soSvc.getProviderOrders(user.getUserId()));
        req.getRequestDispatcher("/user/my-services.jsp").forward(req, res);
    }

    /**
     * POST handler: persist a new service listing for the current user.
     * Parses and validates the form fields (categoryId, title, description, price),
     * delegates persistence to {@link ServiceListingService#addService(Service)},
     * stores a flash message in the session, and redirects to "My Services".
     */
    private void handleAdd(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        Service s = new Service();
        s.setUserId(user.getUserId());
        // Defensive parsing — invalid numeric input becomes a user-facing error message
        try { s.setCategoryId(Integer.parseInt(req.getParameter("categoryId"))); } catch (Exception e) { session.setAttribute("error", "Invalid category"); res.sendRedirect(req.getContextPath() + "/user/services?action=add"); return; }
        s.setTitle(req.getParameter("title"));
        s.setDescription(req.getParameter("description"));
        try { s.setPrice(new BigDecimal(req.getParameter("price"))); } catch (Exception e) { session.setAttribute("error", "Invalid price"); res.sendRedirect(req.getContextPath() + "/user/services?action=add"); return; }

        String result = svc.addService(s);
        // Convention: success messages contain the word "successfully"
        session.setAttribute(result.contains("successfully") ? "success" : "error", result);
        res.sendRedirect(req.getContextPath() + "/user/services?action=my");
    }

    /**
     * POST handler: update an existing service.
     * Enforces an <b>ownership check</b> — only the user who created the
     * service may edit it. Updates editable fields and the availability
     * status, then delegates to {@link ServiceListingService#updateService(Service)}.
     */
    private void handleEdit(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String idStr = req.getParameter("serviceId");
        if (idStr == null) { session.setAttribute("error", "Service ID required"); res.sendRedirect(req.getContextPath() + "/user/services?action=my"); return; }
        Service s = svc.getServiceById(Integer.parseInt(idStr));
        // Ownership guard — prevent users from editing services they don't own
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

    /**
     * POST handler: delete a service.
     * Ownership is enforced at the DAO layer — the DELETE SQL includes
     * {@code WHERE user_id = ?} so a service belonging to another user
     * will never be removed.
     */
    private void handleDelete(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String idStr = req.getParameter("serviceId");
        if (idStr != null && svc.deleteService(Integer.parseInt(idStr), user.getUserId())) {
            session.setAttribute("success", "Service deleted");
        } else {
            session.setAttribute("error", "Failed to delete");
        }
        res.sendRedirect(req.getContextPath() + "/user/services?action=my");
    }

    /**
     * POST handler: a buyer places an order against a service.
     * Builds a new {@link ServiceOrder} that links three entities — the
     * service being ordered, the buyer (current user), and the provider
     * (service owner). Delegates persistence to {@link ServiceOrderService}.
     */
    private void handleOrder(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String idStr = req.getParameter("serviceId");
        if (idStr == null) { session.setAttribute("error", "Service ID required"); res.sendRedirect(req.getContextPath() + "/user/services"); return; }
        Service s = svc.getServiceById(Integer.parseInt(idStr));
        if (s == null) { session.setAttribute("error", "Service not found"); res.sendRedirect(req.getContextPath() + "/user/services"); return; }

        // Build the order — links buyer (current user) with provider (service owner)
        ServiceOrder order = new ServiceOrder();
        order.setServiceId(s.getServiceId());
        order.setBuyerId(user.getUserId());
        order.setProviderId(s.getUserId());
        order.setRequestMessage(req.getParameter("message"));

        String result = soSvc.createOrder(order);
        session.setAttribute(result.contains("successfully") ? "success" : "error", result);
        res.sendRedirect(req.getContextPath() + "/user/services?action=detail&serviceId=" + s.getServiceId());
    }

    /**
     * POST handler shared by the four status-change actions
     * (accept / reject / complete / cancel an order).
     * Maps the incoming action string to the corresponding status value
     * and delegates to {@link ServiceOrderService#updateStatus(int, String)}.
     *
     * @param action one of: acceptOrder, rejectOrder, completeOrder, cancelOrder
     */
    private void handleOrderStatus(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user, String action) throws IOException {
        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null) {
            session.setAttribute("error", "Order ID required");
            res.sendRedirect(req.getContextPath() + "/user/services?action=my");
            return;
        }
        try {
            int orderId = Integer.parseInt(orderIdStr);
            String status = switch (action) {
                case "acceptOrder"   -> "ACCEPTED";
                case "rejectOrder"   -> "REJECTED";
                case "completeOrder" -> "COMPLETED";
                case "cancelOrder"   -> "CANCELLED";
                default              -> null;
            };
            if (status != null && soSvc.updateStatus(orderId, status)) {
                session.setAttribute("success", "Order " + status.toLowerCase() + " successfully");
            } else {
                session.setAttribute("error", "Failed to update order status");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid order ID");
        }
        res.sendRedirect(req.getContextPath() + "/user/services?action=my");
    }
}

