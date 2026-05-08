package com.unitrade.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CookieUtil – helper methods for creating, reading, and deleting HTTP cookies.
 * All cookies set by this utility are HttpOnly and scoped to the root path "/".
 */
public class CookieUtil {

    /**
     * Add a new cookie to the HTTP response.
     *
     * @param response   The HTTP response to attach the cookie to
     * @param name       Cookie name
     * @param value      Cookie value
     * @param maxAgeSec  Cookie lifetime in seconds (use 0 to delete, -1 for session-only)
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAgeSec) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAgeSec);
        cookie.setHttpOnly(true); // Prevents JavaScript access (XSS protection)
        cookie.setPath("/");      // Accessible for the entire application
        response.addCookie(cookie);
    }

    /**
     * Read the value of a named cookie from the HTTP request.
     *
     * @param request The HTTP request containing cookies
     * @param name    Name of the cookie to find
     * @return Cookie value string, or null if the cookie does not exist
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (name.equals(c.getName())) return c.getValue();
            }
        }
        return null; // Cookie not found
    }

    /**
     * Immediately expire (delete) a named cookie by setting its max-age to 0.
     *
     * @param response The HTTP response to write the deletion cookie into
     * @param name     Name of the cookie to delete
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);   // Instruct the browser to remove the cookie
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
