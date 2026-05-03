package com.unitrade.filter;

import com.unitrade.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AdminFilter - Authorization Filter
 * Protects routes that require admin role
 * Applies to all /admin/* routes
 * Checks both authentication and admin role
 */
@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization - can add logging here if needed
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Cast to HttpServletRequest and HttpServletResponse
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get the current session (do not create a new one)
        HttpSession session = httpRequest.getSession(false);

        // Get the logged-in user from session
        User loggedInUser = null;
        if (session != null) {
            loggedInUser = (User) session.getAttribute("loggedInUser");
        }

        // Check if user is logged in
        if (loggedInUser == null) {
            // User is not authenticated - redirect to login page
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/auth/login");
            return;
        }

        // Check if user has admin role
        if (!"ADMIN".equals(loggedInUser.getRole())) {
            // User is logged in but not an admin - access denied
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/error.jsp?message=Access denied. Admin privileges required.");
            return;
        }

        // Additional checks for admin account status
        if (!"APPROVED".equals(loggedInUser.getApprovalStatus())) {
            // Admin account is not approved
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/error.jsp?message=Admin account is pending approval");
            return;
        }

        if (!"ACTIVE".equals(loggedInUser.getAccountStatus())) {
            // Admin account is not active
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/error.jsp?message=Admin account is not active");
            return;
        }

        // User is authenticated as admin - allow the request to proceed
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
        Filter.super.destroy();
    }
}


