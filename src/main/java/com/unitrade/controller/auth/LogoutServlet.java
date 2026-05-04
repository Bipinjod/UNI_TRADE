package com.unitrade.controller.auth;

import com.unitrade.dao.RememberTokenDAO;
import com.unitrade.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * LogoutServlet - Handles user logout
 * GET /auth/logout → invalidates session, clears Remember Me cookie + DB token.
 */
@WebServlet("/auth/logout")
public class LogoutServlet extends HttpServlet {

    private final RememberTokenDAO tokenDAO = new RememberTokenDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ── 1. Revoke Remember Me cookie & DB token ──────────────────────────
        String cookieValue = CookieUtil.getCookieValue(request, LoginServlet.REMEMBER_COOKIE);
        if (cookieValue != null) {
            String[] parts = cookieValue.split(":", 2);
            if (parts.length == 2) {
                String selector = parts[0];
                tokenDAO.deleteBySelector(selector); // Remove token from DB
            }
            CookieUtil.deleteCookie(response, LoginServlet.REMEMBER_COOKIE); // Expire cookie
        }

        // ── 2. Invalidate HTTP session ────────────────────────────────────────
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // ── 3. Redirect to login ──────────────────────────────────────────────
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }
}
