package com.unitrade.controller.admin;

import com.unitrade.dao.CategoryDAO;
import com.unitrade.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * ManageCategoriesServlet - Category Management
 * Handles listing, adding, updating, and deleting categories
 */
@WebServlet("/admin/categories")
public class ManageCategoriesServlet extends HttpServlet {

    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get filter parameter
            String filter = request.getParameter("filter");

            List<Category> categories;

            if ("active".equals(filter)) {
                // Show only active categories
                categories = categoryDAO.getActiveCategories();
                request.setAttribute("filterType", "Active Categories");
            } else {
                // Show all categories by default
                categories = categoryDAO.getAllCategories();
                request.setAttribute("filterType", "All Categories");
            }

            // Check if editing
            String editIdStr = request.getParameter("edit");
            if (editIdStr != null) {
                try {
                    int categoryId = Integer.parseInt(editIdStr);
                    Category editCategory = categoryDAO.getCategoryById(categoryId);
                    request.setAttribute("editCategory", editCategory);
                } catch (NumberFormatException e) {
                    // Invalid ID, ignore
                }
            }

            // Set attributes
            request.setAttribute("categories", categories);

            // Forward to JSP
            request.getRequestDispatcher("/admin/manage-categories.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load categories");
            request.getRequestDispatcher("/admin/manage-categories.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        try {
            // Validate action
            if (action == null) {
                session.setAttribute("error", "Invalid action");
                response.sendRedirect(request.getContextPath() + "/admin/categories");
                return;
            }

            // Handle different actions
            switch (action) {
                case "add":
                    handleAddCategory(request, session);
                    break;

                case "update":
                    handleUpdateCategory(request, session);
                    break;

                case "delete":
                    handleDeleteCategory(request, session);
                    break;

                default:
                    session.setAttribute("error", "Invalid action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred: " + e.getMessage());
        }

        // Redirect back to categories page
        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }

    /**
     * Handle adding a new category
     */
    private void handleAddCategory(HttpServletRequest request, HttpSession session) {
        String categoryName = request.getParameter("categoryName");
        String categoryType = request.getParameter("categoryType");
        String description = request.getParameter("description");
        String status = request.getParameter("status");

        // Validate inputs
        if (categoryName == null || categoryName.trim().isEmpty()) {
            session.setAttribute("error", "Category name is required");
            return;
        }

        if (status == null || status.trim().isEmpty()) {
            status = "ACTIVE"; // Default to active
        }

        // Create category object
        Category category = new Category();
        category.setCategoryName(categoryName.trim());
        category.setCategoryType(categoryType != null ? categoryType : "ITEM");
        category.setDescription(description != null ? description.trim() : "");
        category.setStatus(status);

        // Add to database
        boolean success = categoryDAO.addCategory(category);

        if (success) {
            session.setAttribute("success", "Category added successfully");
        } else {
            session.setAttribute("error", "Failed to add category. Name might already exist.");
        }
    }

    /**
     * Handle updating an existing category
     */
    private void handleUpdateCategory(HttpServletRequest request, HttpSession session) {
        String categoryIdStr = request.getParameter("categoryId");
        String categoryName = request.getParameter("categoryName");
        String categoryType = request.getParameter("categoryType");
        String description = request.getParameter("description");
        String status = request.getParameter("status");

        // Validate inputs
        if (categoryIdStr == null || categoryName == null || categoryName.trim().isEmpty()) {
            session.setAttribute("error", "Category ID and name are required");
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);

            // Create category object
            Category category = new Category();
            category.setCategoryId(categoryId);
            category.setCategoryName(categoryName.trim());
            category.setCategoryType(categoryType != null ? categoryType : "ITEM");
            category.setDescription(description != null ? description.trim() : "");
            category.setStatus(status != null ? status : "ACTIVE");

            // Update in database
            boolean success = categoryDAO.updateCategory(category);

            if (success) {
                session.setAttribute("success", "Category updated successfully");
            } else {
                session.setAttribute("error", "Failed to update category");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid category ID");
        }
    }

    /**
     * Handle deleting a category
     */
    private void handleDeleteCategory(HttpServletRequest request, HttpSession session) {
        String categoryIdStr = request.getParameter("categoryId");

        if (categoryIdStr == null) {
            session.setAttribute("error", "Category ID is required");
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);

            // Delete from database
            boolean success = categoryDAO.deleteCategory(categoryId);

            if (success) {
                session.setAttribute("success", "Category deleted successfully");
            } else {
                session.setAttribute("error", "Failed to delete category. It might be in use by items.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid category ID");
        }
    }
}

