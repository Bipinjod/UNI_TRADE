package com.unitrade.controller.user;

import com.unitrade.dao.WishlistDAO;
import com.unitrade.model.Item;
import com.unitrade.model.User;
import com.unitrade.model.Wishlist;
import com.unitrade.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * WishlistServlet - Wishlist Management for Users
 * Handles viewing wishlist, adding items, and removing items
 * Uses session-based wishlist cache for better performance
 */
@WebServlet("/user/wishlist")
public class WishlistServlet extends HttpServlet {

    private WishlistDAO wishlistDAO;
    private ItemService itemService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.wishlistDAO = new WishlistDAO();
        this.itemService = new ItemService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Default action is view
        if (action == null || action.isEmpty()) {
            action = "view";
        }

        try {
            switch (action) {
                case "view":
                    handleViewWishlist(request, response);
                    break;

                default:
                    handleViewWishlist(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/user/wishlist.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (action == null || action.isEmpty()) {
            session.setAttribute("error", "Invalid action");
            response.sendRedirect(request.getContextPath() + "/user/wishlist");
            return;
        }

        try {
            switch (action) {
                case "add":
                    handleAddToWishlist(request, response, session);
                    break;

                case "remove":
                    handleRemoveFromWishlist(request, response, session);
                    break;

                default:
                    session.setAttribute("error", "Invalid action");
                    response.sendRedirect(request.getContextPath() + "/user/wishlist");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/wishlist");
        }
    }

    /**
     * Handle view wishlist - Show user's wishlist items
     */
    private void handleViewWishlist(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get wishlist from database
        List<Wishlist> wishlistItems = wishlistDAO.getWishlistByUserId(loggedInUser.getUserId());

        // Update session-based wishlist cache (store item IDs for quick checking)
        Set<Integer> wishlistItemIds = new HashSet<>();
        for (Wishlist wishlist : wishlistItems) {
            wishlistItemIds.add(wishlist.getItemId());
        }
        session.setAttribute("wishlistItemIds", wishlistItemIds);

        // Get full item details for wishlist items (if not already populated by DAO JOIN)
        List<Item> items = new ArrayList<>();
        for (Wishlist wishlist : wishlistItems) {
            // Check if item details are already populated from JOIN
            if (wishlist.getItemTitle() != null) {
                // Create Item object from wishlist data
                Item item = new Item();
                item.setItemId(wishlist.getItemId());
                item.setTitle(wishlist.getItemTitle());
                item.setPrice(wishlist.getPrice());
                item.setImagePath(wishlist.getImagePath());
                items.add(item);
            } else {
                // Fallback: fetch item details
                Item item = itemService.getItemById(wishlist.getItemId());
                if (item != null) {
                    items.add(item);
                }
            }
        }

        // Set attributes
        request.setAttribute("wishlistItems", items);
        request.setAttribute("wishlistCount", items.size());

        // Forward to JSP
        request.getRequestDispatcher("/user/wishlist.jsp").forward(request, response);
    }

    /**
     * Handle add to wishlist - Add item to user's wishlist
     */
    private void handleAddToWishlist(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String itemIdStr = request.getParameter("itemId");
        String returnUrl = request.getParameter("returnUrl");

        if (itemIdStr == null) {
            session.setAttribute("error", "Item ID is required");
            redirectToReturnUrl(response, returnUrl, request.getContextPath());
            return;
        }

        try {
            int itemId = Integer.parseInt(itemIdStr);
            int userId = loggedInUser.getUserId();

            // Check if already in wishlist
            if (wishlistDAO.isWishlisted(userId, itemId)) {
                session.setAttribute("info", "Item is already in your wishlist");
                redirectToReturnUrl(response, returnUrl, request.getContextPath());
                return;
            }

            // Check if user is trying to add their own item
            Item item = itemService.getItemById(itemId);
            if (item != null && item.getUserId() == userId) {
                session.setAttribute("error", "You cannot add your own item to wishlist");
                redirectToReturnUrl(response, returnUrl, request.getContextPath());
                return;
            }

            // Add to wishlist
            boolean success = wishlistDAO.addToWishlist(userId, itemId);

            if (success) {
                // Update session cache
                @SuppressWarnings("unchecked")
                Set<Integer> wishlistItemIds = (Set<Integer>) session.getAttribute("wishlistItemIds");
                if (wishlistItemIds == null) {
                    wishlistItemIds = new HashSet<>();
                }
                wishlistItemIds.add(itemId);
                session.setAttribute("wishlistItemIds", wishlistItemIds);

                session.setAttribute("success", "Item added to wishlist");
            } else {
                session.setAttribute("error", "Failed to add item to wishlist");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid item ID");
        }

        redirectToReturnUrl(response, returnUrl, request.getContextPath());
    }

    /**
     * Handle remove from wishlist - Remove item from user's wishlist
     */
    private void handleRemoveFromWishlist(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String itemIdStr = request.getParameter("itemId");
        String returnUrl = request.getParameter("returnUrl");

        if (itemIdStr == null) {
            session.setAttribute("error", "Item ID is required");
            redirectToReturnUrl(response, returnUrl, request.getContextPath());
            return;
        }

        try {
            int itemId = Integer.parseInt(itemIdStr);
            int userId = loggedInUser.getUserId();

            // Remove from wishlist
            boolean success = wishlistDAO.removeFromWishlist(userId, itemId);

            if (success) {
                // Update session cache
                @SuppressWarnings("unchecked")
                Set<Integer> wishlistItemIds = (Set<Integer>) session.getAttribute("wishlistItemIds");
                if (wishlistItemIds != null) {
                    wishlistItemIds.remove(itemId);
                    session.setAttribute("wishlistItemIds", wishlistItemIds);
                }

                session.setAttribute("success", "Item removed from wishlist");
            } else {
                session.setAttribute("error", "Failed to remove item from wishlist");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid item ID");
        }

        redirectToReturnUrl(response, returnUrl, request.getContextPath());
    }

    /**
     * Helper method to redirect to return URL or default wishlist page
     */
    private void redirectToReturnUrl(HttpServletResponse response, String returnUrl, String contextPath)
            throws IOException {

        if (returnUrl != null && !returnUrl.isEmpty()) {
            // Make sure returnUrl is relative and safe
            if (returnUrl.startsWith("/")) {
                response.sendRedirect(contextPath + returnUrl);
            } else {
                response.sendRedirect(returnUrl);
            }
        } else {
            // Default to wishlist page
            response.sendRedirect(contextPath + "/user/wishlist");
        }
    }
}


