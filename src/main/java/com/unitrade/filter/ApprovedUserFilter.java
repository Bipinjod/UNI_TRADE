package com.unitrade.filter;

import com.unitrade.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * ApprovedUserFilter – ensures only APPROVED + ACTIVE users can post content.
 * Applied to write-actions under /user/*.
 */
@WebFilter(urlPatterns = {"/user/*"})
public class ApprovedUserFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session != null) {
            User user = (User) session.getAttribute("loggedInUser");
            if (user != null) {
                if (!"APPROVED".equals(user.getApprovalStatus())) {
                    res.sendRedirect(req.getContextPath() + "/error.jsp?message=Your+account+is+pending+approval");
                    return;
                }
                if (!"ACTIVE".equals(user.getAccountStatus())) {
                    res.sendRedirect(req.getContextPath() + "/error.jsp?message=Your+account+is+not+active");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
}

