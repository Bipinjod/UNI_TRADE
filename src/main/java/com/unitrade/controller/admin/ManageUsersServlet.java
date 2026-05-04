package com.unitrade.controller.admin;

import com.unitrade.model.User;
import com.unitrade.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * ManageUsersServlet - User Management
 * Handles listing users and admin actions (approve/reject)
 */
@WebServlet("/admin/users")
public class ManageUsersServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get filter parameter (all, pending, approved, etc.)
            String filter = request.getParameter("filter");

            List<User> users;

            if ("pending".equals(filter)) {
                // Show only pending users
                users = userService.getPendingUsers();
                request.setAttribute("filterType", "Pending Users");
            } else {
                // Show all users by default
                users = userService.getAllUsers();
                request.setAttribute("filterType", "All Users");
            }

            // Get pending count for sidebar/badge
            int pendingCount = userService.getPendingUsers().size();

            // Set attributes
            request.setAttribute("users", users);
            request.setAttribute("pendingCount", pendingCount);

            // Forward to JSP
            request.getRequestDispatcher("/admin/manage-users.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load users");
            request.getRequestDispatcher("/admin/manage-users.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");

        HttpSession session = request.getSession();

        try {
            // Validate parameters
            if (action == null || userIdStr == null) {
                session.setAttribute("error", "Invalid request parameters");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            int userId = Integer.parseInt(userIdStr);

            // Handle different actions
            switch (action) {
                case "approve":
                    boolean approveSuccess = userService.approveUser(userId);
                    if (approveSuccess) {
                        session.setAttribute("success", "User approved successfully");
                    } else {
                        session.setAttribute("error", "Failed to approve user");
                    }
                    break;

                case "reject":
                    boolean rejectSuccess = userService.rejectUser(userId);
                    if (rejectSuccess) {
                        session.setAttribute("success", "User rejected successfully");
                    } else {
                        session.setAttribute("error", "Failed to reject user");
                    }
                    break;

                case "block":
                    boolean blockSuccess = userService.blockUser(userId);
                    if (blockSuccess) {
                        session.setAttribute("success", "User blocked successfully");
                    } else {
                        session.setAttribute("error", "Failed to block user");
                    }
                    break;

                case "activate":
                    boolean activateSuccess = userService.activateUser(userId);
                    if (activateSuccess) {
                        session.setAttribute("success", "User activated successfully");
                    } else {
                        session.setAttribute("error", "Failed to activate user");
                    }
                    break;

                default:
                    session.setAttribute("error", "Invalid action");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid user ID");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred: " + e.getMessage());
        }

        // Redirect back to users page
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}


