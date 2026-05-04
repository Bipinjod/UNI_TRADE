package com.unitrade.filter;

import com.unitrade.controller.auth.LoginServlet;
import com.unitrade.dao.RememberTokenDAO;
import com.unitrade.dao.UserDAO;
import com.unitrade.model.RememberToken;
import com.unitrade.model.User;
import com.unitrade.util.CookieUtil;
import com.unitrade.util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AuthFilter - Authentication Filter
 * Protects routes that require user login (/user/*).
 *
 * If no session exists, it checks for a valid Remember Me cookie and
 * restores the session automatically (persistent login).
 */
@WebFilter(urlPatterns = {"/user/*"})
public class AuthFilter implements Filter {

    private RememberTokenDAO tokenDAO;
    private UserDAO userDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        tokenDAO = new RememberTokenDAO();
        userDAO  = new UserDAO();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // ── 1. Check active session ──────────────────────────────────────────
        HttpSession session    = httpRequest.getSession(false);
        User        loggedInUser = (session != null)
                ? (User) session.getAttribute("loggedInUser")
                : null;

        // ── 2. No session — try Remember Me cookie ───────────────────────────
        if (loggedInUser == null) {
            String cookieValue = CookieUtil.getCookieValue(httpRequest, LoginServlet.REMEMBER_COOKIE);

            if (cookieValue != null) {
                loggedInUser = resolveRememberCookie(cookieValue, httpRequest, httpResponse);

                if (loggedInUser != null) {
                    // Restore session from cookie
                    HttpSession newSession = httpRequest.getSession(true);
                    newSession.setAttribute("loggedInUser", loggedInUser);
                    newSession.setMaxInactiveInterval(30 * 60);
                }
            }
        }

        // ── 3. Still not authenticated → redirect to login ───────────────────
        if (loggedInUser == null) {
            // Save original URL so we can redirect back after login
            String requestedUrl = httpRequest.getRequestURI();
            String queryString  = httpRequest.getQueryString();
            if (queryString != null) requestedUrl += "?" + queryString;

            HttpSession newSession = httpRequest.getSession(true);
            newSession.setAttribute("redirectAfterLogin", requestedUrl);

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/login");
            return;
        }

        // ── 4. Check account state ────────────────────────────────────────────
        if (!"APPROVED".equals(loggedInUser.getApprovalStatus())) {
            httpResponse.sendRedirect(httpRequest.getContextPath()
                    + "/error.jsp?message=Your+account+is+pending+approval");
            return;
        }
        if (!"ACTIVE".equals(loggedInUser.getAccountStatus())) {
            httpResponse.sendRedirect(httpRequest.getContextPath()
                    + "/error.jsp?message=Your+account+is+not+active");
            return;
        }

        // ── 5. All checks passed ──────────────────────────────────────────────
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }

    // ─── Private helper ──────────────────────────────────────────────────────

    /**
     * Validate the Remember Me cookie value and return the matching User, or null.
     * Cookie format: "selector:validatorRaw"
     */
    @SuppressWarnings("unused")
    private User resolveRememberCookie(String cookieValue,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        try {
            String[] parts = cookieValue.split(":", 2);
            if (parts.length != 2) {
                CookieUtil.deleteCookie(response, LoginServlet.REMEMBER_COOKIE);
                return null;
            }

            String selector     = parts[0];
            String validatorRaw = parts[1];

            RememberToken token = tokenDAO.findBySelector(selector);

            if (token == null || token.isExpired()) {
                if (token != null) tokenDAO.deleteBySelector(selector);
                CookieUtil.deleteCookie(response, LoginServlet.REMEMBER_COOKIE);
                return null;
            }

            if (!PasswordUtil.verifyPassword(validatorRaw, token.getValidatorHash())) {
                tokenDAO.deleteAllForUser(token.getUserId());
                CookieUtil.deleteCookie(response, LoginServlet.REMEMBER_COOKIE);
                return null;
            }

            User user = userDAO.getUserById(token.getUserId());
            if (user == null
                    || !"APPROVED".equals(user.getApprovalStatus())
                    || !"ACTIVE".equals(user.getAccountStatus())) {
                tokenDAO.deleteBySelector(selector);
                CookieUtil.deleteCookie(response, LoginServlet.REMEMBER_COOKIE);
                return null;
            }

            return user;

        } catch (Exception e) {
            System.err.println("[AuthFilter] resolveRememberCookie error: " + e.getMessage());
            return null;
        }
    }
}
