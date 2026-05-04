package com.unitrade.controller.user;

import com.unitrade.dao.ItemDAO;
import com.unitrade.dao.WishlistDAO;
import com.unitrade.dao.ServiceDAO;
import com.unitrade.dao.HelpRequestDAO;
import com.unitrade.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * UserDashboardServlet - User Dashboard with personal statistics.
 */
@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    private ItemDAO itemDAO;
    private WishlistDAO wishlistDAO;
    private ServiceDAO serviceDAO;
    private HelpRequestDAO helpRequestDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.itemDAO = new ItemDAO();
        this.wishlistDAO = new WishlistDAO();
        this.serviceDAO = new ServiceDAO();
        this.helpRequestDAO = new HelpRequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get current user from session
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // Should not happen due to AuthFilter, but safety check
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        try {
            int uid = loggedInUser.getUserId();

            // Get user's item count
            int userItemsCount = itemDAO.getItemsByUserId(uid).size();

            // Get user's wishlist count
            int wishlistCount = wishlistDAO.getWishlistByUserId(uid).size();

            // Get pending items count (items awaiting approval)
            int pendingItemsCount = 0;
            // Get approved items count
            int approvedItemsCount = 0;
            for (var item : itemDAO.getItemsByUserId(uid)) {
                if ("PENDING".equals(item.getListingStatus())) pendingItemsCount++;
                if ("APPROVED".equals(item.getListingStatus())) approvedItemsCount++;
            }

            // Get user's service count
            int userServicesCount = serviceDAO.getServicesByUserId(uid).size();

            // Get user's request count
            int userRequestsCount = helpRequestDAO.getRequestsByUserId(uid).size();

            // Set attributes for JSP
            request.setAttribute("userItemsCount", userItemsCount);
            request.setAttribute("wishlistCount", wishlistCount);
            request.setAttribute("pendingItemsCount", pendingItemsCount);
            request.setAttribute("approvedItemsCount", approvedItemsCount);
            request.setAttribute("userServicesCount", userServicesCount);
            request.setAttribute("userRequestsCount", userRequestsCount);

            // Forward to dashboard JSP
            request.getRequestDispatcher("/user/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load dashboard data");
            request.getRequestDispatcher("/user/dashboard.jsp").forward(request, response);
        }
    }
}
