package com.unitrade.controller.admin;

import com.unitrade.model.Item;
import com.unitrade.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * ManageItemsServlet - Item Management and Moderation
 * Handles listing items and admin moderation actions (approve/reject/delete)
 */
@WebServlet("/admin/items")
public class ManageItemsServlet extends HttpServlet {

    private ItemService itemService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.itemService = new ItemService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get filter parameter
            String filter = request.getParameter("filter");

            List<Item> items;

            if ("pending".equals(filter)) {
                // Show only pending items (for moderation)
                items = itemService.getPendingItems();
                request.setAttribute("filterType", "Pending Items");
            } else if ("approved".equals(filter)) {
                // Show only approved items
                items = itemService.getApprovedItems();
                request.setAttribute("filterType", "Approved Items");
            } else {
                // Show pending items by default (most important for admin)
                items = itemService.getPendingItems();
                request.setAttribute("filterType", "Pending Items");
            }

            // Get pending count for badge
            int pendingCount = itemService.getPendingItems().size();

            // Get total approved items count
            int approvedCount = itemService.getApprovedItems().size();

            // Set attributes
            request.setAttribute("items", items);
            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("approvedCount", approvedCount);

            // Forward to JSP
            request.getRequestDispatcher("/admin/manage-items.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load items");
            request.getRequestDispatcher("/admin/manage-items.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String itemIdStr = request.getParameter("itemId");

        HttpSession session = request.getSession();

        try {
            // Validate parameters
            if (action == null || itemIdStr == null) {
                session.setAttribute("error", "Invalid request parameters");
                response.sendRedirect(request.getContextPath() + "/admin/items");
                return;
            }

            int itemId = Integer.parseInt(itemIdStr);

            // Handle different actions
            switch (action) {
                case "approve":
                    boolean approveSuccess = itemService.approveItem(itemId);
                    if (approveSuccess) {
                        session.setAttribute("success", "Item approved successfully");
                    } else {
                        session.setAttribute("error", "Failed to approve item");
                    }
                    break;

                case "reject":
                    boolean rejectSuccess = itemService.rejectItem(itemId);
                    if (rejectSuccess) {
                        session.setAttribute("success", "Item rejected successfully");
                    } else {
                        session.setAttribute("error", "Failed to reject item");
                    }
                    break;

                case "delete":
                    // For admin, we can delete directly using the item ID
                    // Note: Regular deleteItem requires userId verification
                    // For admin deletion, we might need a special method or use DAO directly
                    Item item = itemService.getItemById(itemId);
                    if (item != null) {
                        boolean deleteSuccess = itemService.deleteItem(itemId, item.getUserId());
                        if (deleteSuccess) {
                            session.setAttribute("success", "Item deleted successfully");
                        } else {
                            session.setAttribute("error", "Failed to delete item");
                        }
                    } else {
                        session.setAttribute("error", "Item not found");
                    }
                    break;

                case "markSold":
                    Item soldItem = itemService.getItemById(itemId);
                    if (soldItem != null) {
                        boolean soldSuccess = itemService.markAsSold(itemId, soldItem.getUserId());
                        if (soldSuccess) {
                            session.setAttribute("success", "Item marked as sold");
                        } else {
                            session.setAttribute("error", "Failed to mark item as sold");
                        }
                    } else {
                        session.setAttribute("error", "Item not found");
                    }
                    break;

                default:
                    session.setAttribute("error", "Invalid action");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid item ID");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred: " + e.getMessage());
        }

        // Redirect back to items page
        response.sendRedirect(request.getContextPath() + "/admin/items");
    }
}


