package com.unitrade.controller.publicweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ContactServlet - Serves the public Contact page.
 * <p>
 * Handles GET requests to {@code /contact} and forwards them to the
 * {@code contact.jsp} view. No authentication or database access is required.
 * </p>
 *
 * URL mapping: GET /contact
 */
@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    /**
     * Forwards the incoming GET request to the Contact page JSP.
     *
     * @param req  the HTTP servlet request
     * @param res  the HTTP servlet response
     * @throws ServletException if the request dispatcher fails
     * @throws IOException      if an I/O error occurs during forwarding
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("/contact.jsp").forward(req, res);
    }
}
