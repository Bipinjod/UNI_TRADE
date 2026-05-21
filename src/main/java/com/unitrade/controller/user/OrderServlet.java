package com.unitrade.controller.user;

import com.unitrade.model.Item;
import com.unitrade.model.Order;
import com.unitrade.model.User;
import com.unitrade.service.ItemService;
import com.unitrade.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderServlet – manages item purchase orders at /user/orders.
 *
 * GET  /user/orders          → My Orders page (orders placed as buyer + orders received as seller)
 * POST /user/orders?action=create   → Place a new purchase request for an item
 * POST /user/orders?action=accept   → Seller accepts a pending order
 * POST /user/orders?action=reject   → Seller rejects a pending order
 * POST /user/orders?action=complete → Mark an accepted order as completed (triggers SOLD status on item)
 * POST /user/orders?action=cancel   → Buyer or seller cancels an order
 *
 * Requires an active logged-in session for all operations.
 */
@WebServlet("/user/orders")
public class OrderServlet extends HttpServlet {

    /** Business logic for order lifecycle (create, status updates, queries). */
    private OrderService orderService;
    /** Used to look up item details when creating a new order. */
    private ItemService itemService;

    /** Initialise service instances on servlet load. */
    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        itemService  = new ItemService();
    }

    /**
     * Display the My Orders page.
     * Loads both sides: orders the user placed as a buyer and orders received as a seller.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        // Guard: redirect unauthenticated visitors to login
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        // Load buyer-side and seller-side orders separately so the JSP can render two tables
        List<Order> buyerOrders  = orderService.getBuyerOrders(user.getUserId());
        List<Order> sellerOrders = orderService.getSellerOrders(user.getUserId());

        req.setAttribute("buyerOrders",  buyerOrders);
        req.setAttribute("sellerOrders", sellerOrders);
        req.getRequestDispatcher("/user/my-orders.jsp").forward(req, res);
    }

    /**
     * Route POST requests to the appropriate handler based on the "action" parameter.
     * Default action when none is supplied is "create".
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        // Guard: redirect unauthenticated users to login
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        String action = req.getParameter("action");
        if (action == null) action = "create"; // default to creating a new order

        switch (action) {
            case "create":
                handleCreate(req, res, session, user);
                break;
            // Status-update actions share a single handler; action string carries the target status
            case "accept":
            case "reject":
            case "complete":
            case "cancel":
                handleStatusUpdate(req, res, session, action);
                break;
            default:
                session.setAttribute("error", "Invalid action");
                res.sendRedirect(req.getContextPath() + "/user/orders");
        }
    }

    /**
     * Handle new order creation.
     * Validates the item exists, builds the Order object, and delegates to OrderService.
     *
     * @param user The currently authenticated buyer
     */
    private void handleCreate(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String itemIdStr = req.getParameter("itemId");
        String message   = req.getParameter("message"); // optional message to seller

        if (itemIdStr == null) {
            session.setAttribute("error", "Item ID is required");
            res.sendRedirect(req.getContextPath() + "/user/items");
            return;
        }

        try {
            int itemId = Integer.parseInt(itemIdStr);
            // Look up the item to retrieve the seller's user ID
            Item item = itemService.getItemById(itemId);
            if (item == null) {
                session.setAttribute("error", "Item not found");
                res.sendRedirect(req.getContextPath() + "/user/items");
                return;
            }

            // Build the Order object
            Order order = new Order();
            order.setItemId(itemId);
            order.setBuyerId(user.getUserId());
            order.setSellerId(item.getUserId()); // seller is the item owner
            order.setQuantity(1);                // single-unit purchase
            order.setMessage(message);
            order.setOrderStatus("PENDING");     // starts as pending until seller acts

            String result = orderService.createOrder(order);
            if (result.contains("successfully")) {
                session.setAttribute("success", result);
            } else {
                session.setAttribute("error", result);
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid item ID");
        }
        res.sendRedirect(req.getContextPath() + "/user/orders");
    }

    /**
     * Handle status updates (accept, reject, complete, cancel) for an existing order.
     * Maps the action string to the corresponding DB status value.
     *
     * @param action One of: accept, reject, complete, cancel
     */
    private void handleStatusUpdate(HttpServletRequest req, HttpServletResponse res, HttpSession session, String action) throws IOException {
        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null) {
            session.setAttribute("error", "Order ID required");
            res.sendRedirect(req.getContextPath() + "/user/orders");
            return;
        }
        try {
            int orderId = Integer.parseInt(orderIdStr);
            // Map action name → DB status string
            String status = switch (action) {
                case "accept"   -> "ACCEPTED";
                case "reject"   -> "REJECTED";
                case "complete" -> "COMPLETED";  // also marks item as SOLD via OrderService
                case "cancel"   -> "CANCELLED";
                default         -> null;
            };
            if (status != null && orderService.updateOrderStatus(orderId, status)) {
                session.setAttribute("success", "Order " + action + "ed successfully");
            } else {
                session.setAttribute("error", "Failed to update order");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid order ID");
        }
        res.sendRedirect(req.getContextPath() + "/user/orders");
    }
}
