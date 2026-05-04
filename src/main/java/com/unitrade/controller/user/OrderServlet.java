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
 * OrderServlet – create item orders, view buyer/seller orders, update status.
 */
@WebServlet("/user/orders")
public class OrderServlet extends HttpServlet {

    private OrderService orderService;
    private ItemService itemService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
        itemService = new ItemService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        // Buyer orders + seller orders combined
        List<Order> buyerOrders = orderService.getBuyerOrders(user.getUserId());
        List<Order> sellerOrders = orderService.getSellerOrders(user.getUserId());

        req.setAttribute("buyerOrders", buyerOrders);
        req.setAttribute("sellerOrders", sellerOrders);
        req.getRequestDispatcher("/user/my-orders.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        String action = req.getParameter("action");
        if (action == null) action = "create";

        switch (action) {
            case "create":
                handleCreate(req, res, session, user);
                break;
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

    private void handleCreate(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String itemIdStr = req.getParameter("itemId");
        String message = req.getParameter("message");

        if (itemIdStr == null) {
            session.setAttribute("error", "Item ID is required");
            res.sendRedirect(req.getContextPath() + "/user/items");
            return;
        }

        try {
            int itemId = Integer.parseInt(itemIdStr);
            Item item = itemService.getItemById(itemId);
            if (item == null) {
                session.setAttribute("error", "Item not found");
                res.sendRedirect(req.getContextPath() + "/user/items");
                return;
            }

            Order order = new Order();
            order.setItemId(itemId);
            order.setBuyerId(user.getUserId());
            order.setSellerId(item.getUserId());
            order.setQuantity(1);
            order.setMessage(message);
            order.setOrderStatus("PENDING");

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

    private void handleStatusUpdate(HttpServletRequest req, HttpServletResponse res, HttpSession session, String action) throws IOException {
        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null) {
            session.setAttribute("error", "Order ID required");
            res.sendRedirect(req.getContextPath() + "/user/orders");
            return;
        }
        try {
            int orderId = Integer.parseInt(orderIdStr);
            String status = switch (action) {
                case "accept" -> "ACCEPTED";
                case "reject" -> "REJECTED";
                case "complete" -> "COMPLETED";
                case "cancel" -> "CANCELLED";
                default -> null;
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

