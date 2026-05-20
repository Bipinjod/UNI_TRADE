package com.unitrade.controller.user;

import com.unitrade.model.User;
import com.unitrade.service.UserService;
import com.unitrade.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * ProfileServlet - User profile management controller.
 * <p>
 * Allows the authenticated user to view and update their profile information
 * and to change their password.
 * </p>
 *
 * GET  /user/profile                        - Display the profile page
 * POST /user/profile                        - Update profile information
 * POST /user/profile?action=changePassword  - Change the user's password
 *
 * Access: Requires authenticated session (enforced by {@code AuthFilter}).
 */
@WebServlet("/user/profile")
public class ProfileServlet extends HttpServlet {

    /** Business logic service for user account operations. */
    private UserService userService;

    /**
     * Initialise the UserService on servlet load.
     *
     * @throws ServletException if the parent init fails
     */
    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    /**
     * Display the profile page with the latest user data refreshed from the database.
     * <p>
     * The profile data is re-fetched each time to ensure the page shows any
     * changes made by admin that may not yet be reflected in the session object.
     * </p>
     *
     * @param req  the HTTP request
     * @param res  the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        // Refresh user data from DB so the form always shows the latest values
        User fresh = userService.getUserById(user.getUserId());
        req.setAttribute("profileUser", fresh != null ? fresh : user);
        req.getRequestDispatcher("/user/profile.jsp").forward(req, res);
    }

    /**
     * Route POST requests: delegate to password-change or profile-update handler
     * based on the {@code action} parameter.
     *
     * @param req  the HTTP request
     * @param res  the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException      if a redirect or I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        String action = req.getParameter("action");
        if ("changePassword".equals(action)) {
            handleChangePassword(req, res, session, user);
        } else {
            // Default: update profile fields
            handleUpdateProfile(req, res, session, user);
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Update the user's profile information (name, email, phone, academic details).
     * <p>
     * On success the session user object is refreshed so subsequent page renders
     * immediately reflect the new values without requiring a new login.
     * </p>
     *
     * @param req     the HTTP request containing the updated field values
     * @param res     the HTTP response
     * @param session the current HTTP session
     * @param user    the authenticated user whose profile is being updated
     * @throws IOException if a redirect occurs
     */
    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        // Copy submitted values onto the session user object before passing to the service
        user.setFullName(req.getParameter("fullName"));
        user.setEmail(req.getParameter("email"));
        user.setPhone(req.getParameter("phone"));
        user.setCollegeName(req.getParameter("collegeName"));
        user.setCourseName(req.getParameter("courseName"));
        user.setAcademicYear(req.getParameter("academicYear"));

        String result = userService.updateProfile(user);
        if (result.contains("successfully")) {
            // Refresh session user so in-memory data stays in sync with the DB
            User refreshed = userService.getUserById(user.getUserId());
            if (refreshed != null) session.setAttribute("loggedInUser", refreshed);
            session.setAttribute("success", result);
        } else {
            session.setAttribute("error", result);
        }
        res.sendRedirect(req.getContextPath() + "/user/profile");
    }

    /**
     * Change the user's password after verifying the current password.
     * <p>
     * Validates that:
     * <ul>
     *   <li>All three password fields are provided.</li>
     *   <li>The current password matches the stored BCrypt hash.</li>
     *   <li>The new password meets the minimum length requirement (6 chars).</li>
     *   <li>The new password and confirmation match.</li>
     * </ul>
     * On success the session user object is refreshed so the in-memory hash
     * stays consistent with the database.
     * </p>
     *
     * @param req     the HTTP request containing {@code currentPassword}, {@code newPassword},
     *                and {@code confirmPassword}
     * @param res     the HTTP response
     * @param session the current HTTP session
     * @param user    the authenticated user changing their password
     * @throws IOException if a redirect occurs
     */
    private void handleChangePassword(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String currentPassword  = req.getParameter("currentPassword");
        String newPassword      = req.getParameter("newPassword");
        String confirmPassword  = req.getParameter("confirmPassword");

        // All three fields are mandatory
        if (currentPassword == null || newPassword == null || confirmPassword == null) {
            session.setAttribute("error", "All password fields are required");
            res.sendRedirect(req.getContextPath() + "/user/profile");
            return;
        }

        // Verify the user knows their current password before allowing the change
        if (!PasswordUtil.verifyPassword(currentPassword, user.getPasswordHash())) {
            session.setAttribute("error", "Current password is incorrect");
            res.sendRedirect(req.getContextPath() + "/user/profile");
            return;
        }

        // Enforce minimum length policy
        if (newPassword.length() < 6) {
            session.setAttribute("error", "New password must be at least 6 characters");
            res.sendRedirect(req.getContextPath() + "/user/profile");
            return;
        }

        // Ensure the confirmation matches the new password
        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("error", "New passwords do not match");
            res.sendRedirect(req.getContextPath() + "/user/profile");
            return;
        }

        // Hash and persist the new password
        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        boolean saved = userService.updatePassword(user.getUserId(), user.getPasswordHash());

        if (saved) {
            // Refresh session user so in-memory hash stays in sync
            User refreshed = userService.getUserById(user.getUserId());
            if (refreshed != null) session.setAttribute("loggedInUser", refreshed);
            session.setAttribute("success", "Password changed successfully");
        } else {
            session.setAttribute("error", "Failed to update password. Please try again.");
        }
        res.sendRedirect(req.getContextPath() + "/user/profile");
    }
}
