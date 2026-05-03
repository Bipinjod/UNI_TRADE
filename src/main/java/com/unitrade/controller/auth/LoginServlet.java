package com.unitrade.controller.auth;

import com.unitrade.dao.RememberTokenDAO;
import com.unitrade.dao.UserDAO;
import com.unitrade.model.RememberToken;
import com.unitrade.model.User;
import com.unitrade.service.UserService;
import com.unitrade.util.CookieUtil;
import com.unitrade.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

/**
 * LoginServlet - Handles user authentication.
 * GET  /auth/login  → shows login page (also handles auto-login via Remember Me cookie)
 * POST /auth/login  → processes login form + optional Remember Me cookie issuance
 *
 * Remember Me design (split-token pattern):
 *   Cookie value stored on client : "selector:validatorRaw"
 *   Database stores               : selector, BCrypt(validatorRaw), expires_at
 *   On auto-login                 : look up by selector, verify BCrypt, load user
 */
@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {

    /** Cookie name used for Remember Me. Public so filters and logout can reference it. */
    public static final String REMEMBER_COOKIE = "unitrade_remember";

    /** Cookie lifetime in seconds (30 days). */
    private static final int REMEMBER_MAX_AGE = 30 * 24 * 60 * 60;

    private final UserService userService = new UserService();
    private final RememberTokenDAO tokenDAO = new RememberTokenDAO();
    private final UserDAO userDAO = new UserDAO();

    // ─── GET ─────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. If there is an active session, redirect straight to dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            redirectToDashboard((User) session.getAttribute("loggedInUser"), request, response);
            return;
        }

        // 2. Check for a Remember Me cookie — attempt auto-login
        String cookieValue = CookieUtil.getCookieValue(request, REMEMBER_COOKIE);
        if (cookieValue != null) {
            User autoUser = resolveRememberCookie(cookieValue, request, response);
            if (autoUser != null) {
                // Auto-login succeeded — create session and redirect
                createSession(request, autoUser);
                redirectToDashboard(autoUser, request, response);
                return;
            }
        }

        // 3. No session, no valid cookie — show login page
        request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
    }

    // ─── POST ────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email       = request.getParameter("email");
        String password    = request.getParameter("password");
        boolean rememberMe = "on".equals(request.getParameter("rememberMe"));

        try {
            // Validate credentials and get detailed status message
            String status = userService.checkLoginStatus(email, password);

            if ("SUCCESS".equals(status)) {
                User user = userService.authenticateUser(email, password);

                if (user != null) {
                    // Create HTTP session
                    createSession(request, user);

                    // ── Remember Me ──────────────────────────────────────────
                    if (rememberMe) {
                        issueRememberCookie(user.getUserId(), response);
                    }
                    // ─────────────────────────────────────────────────────────

                    // ── Redirect-after-login ──────────────────────────────────
                    HttpSession session = request.getSession(false);
                    String redirectUrl = null;
                    if (session != null) {
                        redirectUrl = (String) session.getAttribute("redirectAfterLogin");
                        if (redirectUrl != null) {
                            session.removeAttribute("redirectAfterLogin");
                        }
                    }
                    if (redirectUrl != null && !redirectUrl.isEmpty()) {
                        response.sendRedirect(redirectUrl);
                    } else {
                        redirectToDashboard(user, request, response);
                    }
                    // ─────────────────────────────────────────────────────────
                    return;
                }
            }

            // Authentication failed — forward back to login with error
            request.setAttribute("error", status);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[LoginServlet] Login error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "A system error occurred. Please try again later.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
        }
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    /**
     * Create a 30-minute session and store the logged-in user.
     */
    private void createSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        session.setAttribute("loggedInUser", user);
        session.setMaxInactiveInterval(30 * 60); // 30 minutes
    }

    /**
     * Redirect the user to the appropriate dashboard based on role.
     */
    private void redirectToDashboard(User user, HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {
        if ("ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
        }
    }

    /**
     * Generate a split-token, persist it, and set the Remember Me cookie.
     *
     * Cookie value: "selector:validatorRaw"
     * DB stores   : selector, BCrypt(validatorRaw), expires_at = now + 30 days
     *
     * @param userId   the authenticated user's ID
     * @param response the HTTP response to attach the cookie to
     */
    private void issueRememberCookie(int userId, HttpServletResponse response) {
        try {
            SecureRandom random = new SecureRandom();

            // selector: 16 random bytes → 32 hex chars (matches VARCHAR(32) column)
            byte[] selectorBytes = new byte[16];
            random.nextBytes(selectorBytes);
            String selector = HexFormat.of().formatHex(selectorBytes);

            // validator: 32 random bytes → raw value sent in cookie; hash stored in DB
            byte[] validatorBytes = new byte[32];
            random.nextBytes(validatorBytes);
            String validatorRaw  = HexFormat.of().formatHex(validatorBytes);
            String validatorHash = PasswordUtil.hashPassword(validatorRaw);

            // Persist token (expires 30 days from now)
            RememberToken token = new RememberToken(
                    userId,
                    selector,
                    validatorHash,
                    LocalDateTime.now().plusDays(30)
            );

            boolean saved = tokenDAO.saveToken(token);

            if (saved) {
                // Cookie value: "selector:validatorRaw"
                String cookieValue = selector + ":" + validatorRaw;
                CookieUtil.addCookie(response, REMEMBER_COOKIE, cookieValue, REMEMBER_MAX_AGE);
                System.out.println("[LoginServlet] Remember Me token issued for userId=" + userId);
            } else {
                System.err.println("[LoginServlet] Failed to save Remember Me token for userId=" + userId);
            }

        } catch (Exception e) {
            // Non-fatal — login still succeeded, just no persistent cookie
            System.err.println("[LoginServlet] issueRememberCookie error: " + e.getMessage());
        }
    }

    /**
     * Validate the Remember Me cookie and return the corresponding User, or null.
     * Deletes the cookie + DB record if invalid or expired.
     *
     * @param cookieValue raw cookie value ("selector:validatorRaw")
     * @param request     HTTP request
     * @param response    HTTP response (used to delete bad cookies)
     * @return User if auto-login succeeded, null otherwise
     */
    @SuppressWarnings("unused")
    private User resolveRememberCookie(String cookieValue,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        try {
            String[] parts = cookieValue.split(":", 2);
            if (parts.length != 2) {
                CookieUtil.deleteCookie(response, REMEMBER_COOKIE);
                return null;
            }

            String selector     = parts[0];
            String validatorRaw = parts[1];

            // Look up token record by selector
            RememberToken token = tokenDAO.findBySelector(selector);

            if (token == null) {
                // Token not in DB — delete stale cookie
                CookieUtil.deleteCookie(response, REMEMBER_COOKIE);
                return null;
            }

            // Check expiry
            if (token.isExpired()) {
                tokenDAO.deleteBySelector(selector);
                CookieUtil.deleteCookie(response, REMEMBER_COOKIE);
                return null;
            }

            // Verify the raw validator matches the stored BCrypt hash
            if (!PasswordUtil.verifyPassword(validatorRaw, token.getValidatorHash())) {
                // Mismatch — possible token theft; delete all tokens for this user
                tokenDAO.deleteAllForUser(token.getUserId());
                CookieUtil.deleteCookie(response, REMEMBER_COOKIE);
                System.err.println("[LoginServlet] Remember Me validator mismatch for userId="
                        + token.getUserId() + ". All tokens revoked.");
                return null;
            }

            // Token is valid — load user
            User user = userDAO.getUserById(token.getUserId());

            if (user == null
                    || !"APPROVED".equals(user.getApprovalStatus())
                    || !"ACTIVE".equals(user.getAccountStatus())) {
                // User no longer valid — clean up
                tokenDAO.deleteBySelector(selector);
                CookieUtil.deleteCookie(response, REMEMBER_COOKIE);
                return null;
            }

            System.out.println("[LoginServlet] Auto-login via Remember Me for userId=" + user.getUserId());
            return user;

        } catch (Exception e) {
            System.err.println("[LoginServlet] resolveRememberCookie error: " + e.getMessage());
            return null;
        }
    }
}
