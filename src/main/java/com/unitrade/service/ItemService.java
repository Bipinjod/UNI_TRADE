package com.unitrade.service;

import com.unitrade.dao.ItemDAO;
import com.unitrade.model.Item;
import com.unitrade.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * ItemService - Business logic layer for Item operations
 * Handles item CRUD, validation, approval workflow, and search
 */
public class ItemService {

    private final ItemDAO itemDAO;

    /**
     * Constructor
     */
    public ItemService() {
        this.itemDAO = new ItemDAO();
    }

    /**
     * Add a new item with validation
     *
     * @param item Item object to add
     * @return Success or error message
     */
    public String addItem(Item item) {
        // Validate required fields
        if (item.getUserId() <= 0) {
            return "Invalid user ID";
        }

        if (item.getCategoryId() <= 0) {
            return "Please select a category";
        }

        if (item.getTitle() == null || item.getTitle().trim().isEmpty()) {
            return "Item title is required";
        }

        if (item.getTitle().length() > 150) {
            return "Item title must be less than 150 characters";
        }

        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            return "Item description is required";
        }

        if (item.getDescription().length() < 20) {
            return "Description must be at least 20 characters";
        }

        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return "Please enter a valid price";
        }

        if (item.getPrice().compareTo(new BigDecimal("999999.99")) > 0) {
            return "Price is too high";
        }

        if (item.getItemCondition() == null || item.getItemCondition().trim().isEmpty()) {
            return "Please select item condition";
        }

        // Validate item condition
        String condition = item.getItemCondition().toUpperCase();
        if (!condition.equals("NEW") && !condition.equals("LIKE_NEW") &&
            !condition.equals("GOOD") && !condition.equals("USED")) {
            return "Invalid item condition";
        }

        // Set default listing status if not set
        if (item.getListingStatus() == null || item.getListingStatus().isEmpty()) {
            item.setListingStatus("PENDING"); // Items need admin approval
        }

        // Add item
        boolean success = itemDAO.addItem(item);

        if (success) {
            return "Item added successfully! Waiting for admin approval.";
        } else {
            return "Failed to add item. Please try again.";
        }
    }

    /**
     * Update an existing item with validation
     *
     * @param item Item object with updated information
     * @return Success or error message
     */
    public String updateItem(Item item) {
        // Validate required fields
        if (item.getItemId() <= 0) {
            return "Invalid item ID";
        }

        if (item.getCategoryId() <= 0) {
            return "Please select a category";
        }

        if (item.getTitle() == null || item.getTitle().trim().isEmpty()) {
            return "Item title is required";
        }

        if (item.getTitle().length() > 150) {
            return "Item title must be less than 150 characters";
        }

        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            return "Item description is required";
        }

        if (item.getDescription().length() < 20) {
            return "Description must be at least 20 characters";
        }

        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return "Please enter a valid price";
        }

        if (item.getPrice().compareTo(new BigDecimal("999999.99")) > 0) {
            return "Price is too high";
        }

        if (item.getItemCondition() == null || item.getItemCondition().trim().isEmpty()) {
            return "Please select item condition";
        }

        // Validate item condition
        String condition = item.getItemCondition().toUpperCase();
        if (!condition.equals("NEW") && !condition.equals("LIKE_NEW") &&
            !condition.equals("GOOD") && !condition.equals("USED")) {
            return "Invalid item condition";
        }

        // Update item
        boolean success = itemDAO.updateItem(item);

        if (success) {
            return "Item updated successfully";
        } else {
            return "Failed to update item. Please try again.";
        }
    }

    /**
     * Delete an item (only by owner)
     *
     * @param itemId Item ID to delete
     * @param userId User ID of the owner
     * @return true if successful, false otherwise
     */
    public boolean deleteItem(int itemId, int userId) {
        if (itemId <= 0 || userId <= 0) {
            return false;
        }
        return itemDAO.deleteItem(itemId, userId);
    }

    /**
     * Get item by ID with full details
     *
     * @param itemId Item ID
     * @return Item object or null
     */
    public Item getItemById(int itemId) {
        if (itemId <= 0) {
            return null;
        }
        return itemDAO.getItemById(itemId);
    }

    /**
     * Get all approved items for public viewing
     *
     * @return List of approved items
     */
    public List<Item> getApprovedItems() {
        return itemDAO.getAllApprovedItems();
    }

    /**
     * Get all items posted by a specific user
     *
     * @param userId User ID
     * @return List of user's items
     */
    public List<Item> getUserItems(int userId) {
        if (userId <= 0) {
            return List.of(); // Return empty list
        }
        return itemDAO.getItemsByUserId(userId);
    }

    /**
     * Search items with optional filters
     *
     * @param keyword Search keyword (can be null)
     * @param categoryId Category filter (can be null)
     * @return List of matching items
     */
    public List<Item> searchItems(String keyword, Integer categoryId) {
        // Clean up keyword
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }

        return itemDAO.searchItems(keyword, categoryId);
    }

    /**
     * Get all items with pending approval status (admin view)
     *
     * @return List of pending items
     */
    public List<Item> getPendingItems() {
        return itemDAO.getPendingItems();
    }

    /**
     * Approve an item (admin action)
     *
     * @param itemId Item ID to approve
     * @return true if successful, false otherwise
     */
    public boolean approveItem(int itemId) {
        if (itemId <= 0) {
            return false;
        }
        return itemDAO.updateItemStatus(itemId, "APPROVED");
    }

    /**
     * Reject an item (admin action)
     *
     * @param itemId Item ID to reject
     * @return true if successful, false otherwise
     */
    public boolean rejectItem(int itemId) {
        if (itemId <= 0) {
            return false;
        }
        return itemDAO.updateItemStatus(itemId, "REJECTED");
    }

    /**
     * Mark an item as sold
     *
     * @param itemId Item ID to mark as sold
     * @param userId User ID (must be the owner)
     * @return true if successful, false otherwise
     */
    public boolean markAsSold(int itemId, int userId) {
        if (itemId <= 0 || userId <= 0) {
            return false;
        }

        // Verify ownership
        Item item = itemDAO.getItemById(itemId);
        if (item == null || item.getUserId() != userId) {
            return false; // Not the owner
        }

        return itemDAO.updateItemStatus(itemId, "SOLD");
    }

    /**
     * Reactivate an item (change from SOLD or INACTIVE to PENDING)
     *
     * @param itemId Item ID to reactivate
     * @param userId User ID (must be the owner)
     * @return true if successful, false otherwise
     */
    public boolean reactivateItem(int itemId, int userId) {
        if (itemId <= 0 || userId <= 0) {
            return false;
        }

        // Verify ownership
        Item item = itemDAO.getItemById(itemId);
        if (item == null || item.getUserId() != userId) {
            return false; // Not the owner
        }

        // Reactivated items need admin approval again
        return itemDAO.updateItemStatus(itemId, "PENDING");
    }

    /**
     * Check if user owns an item
     *
     * @param itemId Item ID
     * @param userId User ID
     * @return true if user owns the item, false otherwise
     */
    public boolean isItemOwner(int itemId, int userId) {
        if (itemId <= 0 || userId <= 0) {
            return false;
        }

        Item item = itemDAO.getItemById(itemId);
        return item != null && item.getUserId() == userId;
    }

    /**
     * Validate price input
     *
     * @param priceString Price as string
     * @return Validated BigDecimal or null if invalid
     */
    public BigDecimal validatePrice(String priceString) {
        if (priceString == null || priceString.trim().isEmpty()) {
            return null;
        }

        try {
            BigDecimal price = new BigDecimal(priceString);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                return null; // Negative price
            }
            if (price.compareTo(new BigDecimal("999999.99")) > 0) {
                return null; // Too high
            }
            return price;
        } catch (NumberFormatException e) {
            return null; // Invalid format
        }
    }
}


