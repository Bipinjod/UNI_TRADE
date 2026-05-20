package com.unitrade.controller.publicweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AboutServlet - Serves the public About page.
 * <p>
 * Handles GET requests to {@code /about} and forwards them to the
 * {@code about.jsp} view. No authentication or database access is required.
 * </p>
 *
 * URL mapping: GET /about
 */
@WebServlet("/about")
public class AboutServlet extends HttpServlet {

    /**
     * Forwards the incoming GET request to the About page JSP.
     *
     * @param req  the HTTP servlet request
     * @param res  the HTTP servlet response
     * @throws ServletException if the request dispatcher fails
     * @throws IOException      if an I/O error occurs during forwarding
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("/about.jsp").forward(req, res);
    }
}