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
 * ProfileServlet – view and update user profile, change password.
 */
@WebServlet("/user/profile")
public class ProfileServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        // Refresh from DB to get latest data
        User fresh = userService.getUserById(user.getUserId());
        if (fresh != null) {
            req.setAttribute("profileUser", fresh);
        } else {
            req.setAttribute("profileUser", user);
        }
        req.getRequestDispatcher("/user/profile.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/auth/login"); return; }

        String action = req.getParameter("action");
        if ("changePassword".equals(action)) {
            handleChangePassword(req, res, session, user);
        } else {
            handleUpdateProfile(req, res, session, user);
        }
    }

    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        user.setFullName(req.getParameter("fullName"));
        user.setEmail(req.getParameter("email"));
        user.setPhone(req.getParameter("phone"));
        user.setCollegeName(req.getParameter("collegeName"));
        user.setCourseName(req.getParameter("courseName"));
        user.setAcademicYear(req.getParameter("academicYear"));

        String result = userService.updateProfile(user);
        if (result.contains("successfully")) {
            // Update session with new data
            User refreshed = userService.getUserById(user.getUserId());
            if (refreshed != null) session.setAttribute("loggedInUser", refreshed);
            session.setAttribute("success", result);
        } else {
            session.setAttribute("error", result);
        }
        res.sendRedirect(req.getContextPath() + "/user/profile");
    }

    private void handleChangePassword(HttpServletRequest req, HttpServletResponse res, HttpSession session, User user) throws IOException {
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (currentPassword == null || newPassword == null || confirmPassword == null) {
            session.setAttribute("error", "All password fields are required");
            res.sendRedirect(req.getContextPath() + "/user/profile");
            return;
        }

        if (!PasswordUtil.verifyPassword(currentPassword, user.getPasswordHash())) {
            session.setAttribute("error", "Current password is incorrect");
            res.sendRedirect(req.getContextPath() + "/user/profile");
            return;
        }

        if (newPassword.length() < 6) {
            session.setAttribute("error", "New password must be at least 6 characters");
            res.sendRedirect(req.getContextPath() + "/user/profile");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("error", "New passwords do not match");
            res.sendRedirect(req.getContextPath() + "/user/profile");
            return;
        }

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

