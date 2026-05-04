package com.unitrade.util;

import com.unitrade.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * SessionUtil – convenience methods for session management.
 */
public class SessionUtil {

    /** Get the currently logged-in user from the session, or null. */
    public static User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute("loggedInUser");
    }

    /** Check if the current user is an admin. */
    public static boolean isAdmin(HttpServletRequest request) {
        User u = getLoggedInUser(request);
        return u != null && "ADMIN".equals(u.getRole());
    }

    /** Store a flash message in the session. */
    public static void setFlash(HttpServletRequest request, String type, String message) {
        request.getSession().setAttribute(type, message);
    }
}

