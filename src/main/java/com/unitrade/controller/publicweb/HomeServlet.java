package com.unitrade.controller.publicweb;

import com.unitrade.dao.ItemDAO;
import com.unitrade.model.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("")
public class HomeServlet extends HttpServlet {

    private ItemDAO itemDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.itemDAO = new ItemDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Item> allApproved = itemDAO.getAllApprovedItems();

            // Separate books/textbooks so they appear first
            List<Item> books = allApproved.stream()
                    .filter(i -> i.getCategoryName() != null &&
                                 i.getCategoryName().toLowerCase().contains("book"))
                    .collect(Collectors.toList());

            List<Item> others = allApproved.stream()
                    .filter(i -> i.getCategoryName() == null ||
                                 !i.getCategoryName().toLowerCase().contains("book"))
                    .collect(Collectors.toList());

            List<Item> featured = new ArrayList<>();
            featured.addAll(books);
            featured.addAll(others);

            // Cap at 8 items for the home page grid
            if (featured.size() > 8) {
                featured = featured.subList(0, 8);
            }

            req.setAttribute("featuredItems", featured);

            // First item drives the hero card (book preferred)
            if (!featured.isEmpty()) {
                req.setAttribute("heroItem", featured.get(0));
            }

        } catch (Exception e) {
            // DB unavailable — page still renders without live listings
            e.printStackTrace();
        }

        req.getRequestDispatcher("/index.jsp").forward(req, res);
    }
}

