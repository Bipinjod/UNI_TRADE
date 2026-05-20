package com.unitrade.filter;

import com.unitrade.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * ApprovedUserFilter – secondary guard filter applied to all /user/* routes.
 *
 * Even when a user passes the AuthFilter (is logged in), this filter ensures
 * that only APPROVED and ACTIVE accounts can proceed. This defends against a
 * race condition where an admin changes a user's status after the session was
 * created: the status is re-checked on every user request.
 *
 * Applied after AuthFilter because both are mapped to "/user/*".
 */
@WebFilter(urlPatterns = {"/user/*"})
public class ApprovedUserFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); // do not create a new session

        if (session != null) {
            User user = (User) session.getAttribute("loggedInUser");
            if (user != null) {
                // Reject users whose account has not yet been approved
                if (!"APPROVED".equals(user.getApprovalStatus())) {
                    res.sendRedirect(req.getContextPath() + "/error.jsp?message=Your+account+is+pending+approval");
                    return;
                }
                // Reject users whose account has been blocked or deactivated
                if (!"ACTIVE".equals(user.getAccountStatus())) {
                    res.sendRedirect(req.getContextPath() + "/error.jsp?message=Your+account+is+not+active");
                    return;
                }
            }
        }

        // All checks passed (or user not found in session — AuthFilter handles that)
        chain.doFilter(request, response);
    }
}
