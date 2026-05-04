package com.unitrade.controller.user;

import com.unitrade.dao.CategoryDAO;
import com.unitrade.model.Category;
import com.unitrade.model.Item;
import com.unitrade.model.User;
import com.unitrade.service.ItemService;
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
 * ItemServlet - Item Management for Users
 * Handles browsing, viewing, adding, editing, and deleting items
 * Uses action parameter to determine operation
 */
@WebServlet("/user/items")
public class ItemServlet extends HttpServlet {

    private ItemService itemService;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.itemService = new ItemService();
        this.categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Default action is browse
        if (action == null || action.isEmpty()) {
            action = "browse";
        }

        try {
            switch (action) {
                case "browse":
                    handleBrowse(request, response);
                    break;

                case "detail":
                    handleDetail(request, response);
                    break;

                case "my-listings":
                    handleMyListings(request, response);
                    break;

                case "add":
                    handleAddForm(request, response);
                    break;

                case "edit":
                    handleEditForm(request, response);
                    break;

                default:
                    // Default to browse
                    handleBrowse(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/user/browse-items.jsp").forward(request, response);  // FIX: was /user/items.jsp
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (action == null || action.isEmpty()) {
            session.setAttribute("error", "Invalid action");
            response.sendRedirect(request.getContextPath() + "/user/items");
            return;
        }

        try {
            switch (action) {
                case "add":
                    handleAddItem(request, response, session);
                    break;

                case "edit":
                    handleEditItem(request, response, session);
                    break;

                case "delete":
                    handleDeleteItem(request, response, session);
                    break;

                default:
                    session.setAttribute("error", "Invalid action");
                    response.sendRedirect(request.getContextPath() + "/user/items");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/items");
        }
    }

    /**
     * Handle browse - Show all approved items with search/filter
     */
    private void handleBrowse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get search and filter parameters
        String keyword = request.getParameter("keyword");
        String categoryIdStr = request.getParameter("categoryId");

        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryIdStr);
            } catch (NumberFormatException e) {
                // Invalid category ID, ignore
            }
        }

        // Get items based on search/filter
        List<Item> items;
        if (keyword != null || categoryId != null) {
            items = itemService.searchItems(keyword, categoryId);
        } else {
            items = itemService.getApprovedItems();
        }

        // Get categories for filter dropdown
        List<Category> categories = categoryDAO.getActiveCategories();

        // Set attributes
        request.setAttribute("items", items);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("action", "browse");

        // FIX: was /user/items.jsp
        request.getRequestDispatcher("/user/browse-items.jsp").forward(request, response);
    }

    /**
     * Handle detail - Show single item detail
     */
    private void handleDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String itemIdStr = request.getParameter("itemId");

        if (itemIdStr == null) {
            request.setAttribute("error", "Item ID is required");
            handleBrowse(request, response);
            return;
        }

        try {
            int itemId = Integer.parseInt(itemIdStr);
            Item item = itemService.getItemById(itemId);

            if (item == null) {
                request.setAttribute("error", "Item not found");
                handleBrowse(request, response);
                return;
            }

            // Check if current user is the owner
            HttpSession session = request.getSession();
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            boolean isOwner = (loggedInUser != null && item.getUserId() == loggedInUser.getUserId());

            // Set attributes
            request.setAttribute("item", item);
            request.setAttribute("isOwner", isOwner);
            request.setAttribute("action", "detail");

            // FIX: was /user/item-detail.jsp
            request.getRequestDispatcher("/user/item-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid item ID");
            handleBrowse(request, response);
        }
    }

    /**
     * Handle my-listings - Show user's own items
     */
    private void handleMyListings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // FIX: was /login.jsp
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get user's items
        List<Item> items = itemService.getUserItems(loggedInUser.getUserId());

        // Set attributes
        request.setAttribute("items", items);
        request.setAttribute("action", "my-listings");

        // FIX: was /user/my-items.jsp
        request.getRequestDispatcher("/user/my-listings.jsp").forward(request, response);
    }

    /**
     * Handle add form - Show add item form
     */
    private void handleAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get categories for dropdown
        List<Category> categories = categoryDAO.getActiveCategories();

        // Set attributes
        request.setAttribute("categories", categories);
        request.setAttribute("action", "add");

        // FIX: was /user/item-form.jsp
        request.getRequestDispatcher("/user/post-item.jsp").forward(request, response);
    }

    /**
     * Handle edit form - Show edit item form
     */
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String itemIdStr = request.getParameter("itemId");

        if (itemIdStr == null) {
            request.setAttribute("error", "Item ID is required");
            handleMyListings(request, response);
            return;
        }

        try {
            int itemId = Integer.parseInt(itemIdStr);
            Item item = itemService.getItemById(itemId);

            if (item == null) {
                request.setAttribute("error", "Item not found");
                handleMyListings(request, response);
                return;
            }

            // Verify ownership
            HttpSession session = request.getSession();
            User loggedInUser = (User) session.getAttribute("loggedInUser");

            if (loggedInUser == null || !itemService.isItemOwner(itemId, loggedInUser.getUserId())) {
                request.setAttribute("error", "You don't have permission to edit this item");
                handleMyListings(request, response);
                return;
            }

            // Get categories for dropdown
            List<Category> categories = categoryDAO.getActiveCategories();

            // Set attributes
            request.setAttribute("item", item);
            request.setAttribute("categories", categories);
            request.setAttribute("action", "edit");

            // FIX: was /user/item-form.jsp
            request.getRequestDispatcher("/user/post-item.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid item ID");
            handleMyListings(request, response);
        }
    }

    /**
     * Handle add item - Process add item form
     */
    private void handleAddItem(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // FIX: was /login.jsp
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get form parameters
        String categoryIdStr = request.getParameter("categoryId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String itemCondition = request.getParameter("itemCondition");
        String imagePath = request.getParameter("imagePath"); // In real app, handle file upload

        // Create item object
        Item item = new Item();
        item.setUserId(loggedInUser.getUserId());

        // Set category ID
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            try {
                item.setCategoryId(Integer.parseInt(categoryIdStr));
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid category");
                response.sendRedirect(request.getContextPath() + "/user/items?action=add");
                return;
            }
        }

        item.setTitle(title);
        item.setDescription(description);

        // Set price
        BigDecimal price = itemService.validatePrice(priceStr);
        if (price == null) {
            session.setAttribute("error", "Invalid price");
            response.sendRedirect(request.getContextPath() + "/user/items?action=add");
            return;
        }
        item.setPrice(price);

        item.setItemCondition(itemCondition);
        item.setImagePath(imagePath != null && !imagePath.isEmpty() ? imagePath : null);

        // Add item using service
        String result = itemService.addItem(item);

        if (result.contains("successfully")) {
            session.setAttribute("success", result);
            response.sendRedirect(request.getContextPath() + "/user/items?action=my-listings");
        } else {
            session.setAttribute("error", result);
            response.sendRedirect(request.getContextPath() + "/user/items?action=add");
        }
    }

    /**
     * Handle edit item - Process edit item form
     */
    private void handleEditItem(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // FIX: was /login.jsp
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get form parameters
        String itemIdStr = request.getParameter("itemId");
        String categoryIdStr = request.getParameter("categoryId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String itemCondition = request.getParameter("itemCondition");
        String imagePath = request.getParameter("imagePath");

        // Validate item ID
        if (itemIdStr == null) {
            session.setAttribute("error", "Item ID is required");
            response.sendRedirect(request.getContextPath() + "/user/items?action=my-listings");
            return;
        }

        try {
            int itemId = Integer.parseInt(itemIdStr);

            // Verify ownership
            if (!itemService.isItemOwner(itemId, loggedInUser.getUserId())) {
                session.setAttribute("error", "You don't have permission to edit this item");
                response.sendRedirect(request.getContextPath() + "/user/items?action=my-listings");
                return;
            }

            // Create item object
            Item item = new Item();
            item.setItemId(itemId);
            item.setCategoryId(Integer.parseInt(categoryIdStr));
            item.setTitle(title);
            item.setDescription(description);

            // Set price
            BigDecimal price = itemService.validatePrice(priceStr);
            if (price == null) {
                session.setAttribute("error", "Invalid price");
                response.sendRedirect(request.getContextPath() + "/user/items?action=edit&itemId=" + itemId);
                return;
            }
            item.setPrice(price);

            item.setItemCondition(itemCondition);
            item.setImagePath(imagePath != null && !imagePath.isEmpty() ? imagePath : null);

            // Update item using service
            String result = itemService.updateItem(item);

            if (result.contains("successfully")) {
                session.setAttribute("success", result);
            } else {
                session.setAttribute("error", result);
            }

            response.sendRedirect(request.getContextPath() + "/user/items?action=my-listings");

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid item ID or category ID");
            response.sendRedirect(request.getContextPath() + "/user/items?action=my-listings");
        }
    }

    /**
     * Handle delete item - Delete user's item
     */
    private void handleDeleteItem(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // FIX: was /login.jsp
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String itemIdStr = request.getParameter("itemId");

        if (itemIdStr == null) {
            session.setAttribute("error", "Item ID is required");
            response.sendRedirect(request.getContextPath() + "/user/items?action=my-listings");
            return;
        }

        try {
            int itemId = Integer.parseInt(itemIdStr);

            // Delete item (service checks ownership)
            boolean success = itemService.deleteItem(itemId, loggedInUser.getUserId());

            if (success) {
                session.setAttribute("success", "Item deleted successfully");
            } else {
                session.setAttribute("error", "Failed to delete item. You may not have permission.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid item ID");
        }

        response.sendRedirect(request.getContextPath() + "/user/items?action=my-listings");
    }
}

