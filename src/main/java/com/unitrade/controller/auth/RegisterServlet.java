package com.unitrade.controller.auth;

import com.unitrade.model.User;
import com.unitrade.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * RegisterServlet - Handles new user registration
 * GET  /auth/register → shows registration page
 * POST /auth/register → processes registration form
 */
@WebServlet("/auth/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Collect form data
        User user = new User();
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setPasswordHash(request.getParameter("password")); // plain text — service will hash it
        user.setCollegeName(request.getParameter("collegeName"));
        user.setCourseName(request.getParameter("courseName"));
        user.setAcademicYear(request.getParameter("academicYear"));

        String confirmPassword = request.getParameter("confirmPassword");

        try {
            // Register via service (validates, hashes password, saves)
            String result = userService.registerUser(user, confirmPassword);

            if (result.contains("successful")) {
                // Registration successful — redirect to login with success message
                request.getSession().setAttribute("success", result);
                response.sendRedirect(request.getContextPath() + "/auth/login");
            } else {
                // Validation error — go back to register form
                request.setAttribute("error", result);
                request.setAttribute("user", user); // re-fill form fields
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Database or unexpected error — show friendly message
            System.err.println("[RegisterServlet] Registration error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error",
                "An unexpected error occurred. Please try again later.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
        }
    }
}

