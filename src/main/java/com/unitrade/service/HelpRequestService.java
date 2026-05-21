package com.unitrade.service;

import com.unitrade.dao.HelpRequestDAO;
import com.unitrade.dao.RequestResponseDAO;
import com.unitrade.model.HelpRequest;
import com.unitrade.model.RequestResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * HelpRequestService – business logic layer for peer help requests and their responses.
 * Validates input, delegates persistence to the DAOs, and manages approval workflow.
 */
public class HelpRequestService {

    private final HelpRequestDAO requestDAO = new HelpRequestDAO();
    private final RequestResponseDAO responseDAO = new RequestResponseDAO();

    /**
     * Post a new help request after validating all required fields.
     *
     * @param r HelpRequest populated from the submission form
     * @return Success or descriptive error message
     */
    public String addRequest(HelpRequest r) {
        if (r.getUserId() <= 0) return "Invalid user";
        if (r.getCategoryId() <= 0) return "Please select a category";
        if (r.getTitle() == null || r.getTitle().trim().isEmpty()) return "Title is required";
        if (r.getDescription() == null || r.getDescription().trim().length() < 20) return "Description must be at least 20 characters";
        if (r.getBudget() == null || r.getBudget().compareTo(BigDecimal.ZERO) < 0) return "Invalid budget";
        // Default statuses assigned at creation
        r.setRequestStatus("OPEN");
        r.setApprovalStatus("PENDING");
        return requestDAO.addRequest(r) ? "Request posted! Waiting for admin approval." : "Failed to post request.";
    }

    /**
     * Update an existing help request.
     *
     * @param r HelpRequest with updated fields and a valid requestId
     * @return Success or error message
     */
    public String updateRequest(HelpRequest r) {
        if (r.getRequestId() <= 0) return "Invalid request ID";
        if (r.getTitle() == null || r.getTitle().trim().isEmpty()) return "Title is required";
        return requestDAO.updateRequest(r) ? "Request updated successfully" : "Failed to update request.";
    }

    /**
     * Delete a help request belonging to a given user.
     *
     * @param requestId ID of the request to delete
     * @param userId    User ID of the owner (ownership enforced in DAO)
     * @return true if deletion succeeded
     */
    public boolean deleteRequest(int requestId, int userId) { return requestDAO.deleteRequest(requestId, userId); }

    /**
     * Retrieve a single help request by its ID.
     *
     * @param id Request ID
     * @return HelpRequest object, or null if not found
     */
    public HelpRequest getRequestById(int id) { return requestDAO.getRequestById(id); }

    /**
     * Get all admin-approved and open help requests (for the browse page).
     *
     * @return List of approved, open requests ordered newest first
     */
    public List<HelpRequest> getApprovedRequests() { return requestDAO.getApprovedRequests(); }

    /**
     * Get all requests pending admin approval (for the admin panel).
     *
     * @return List of pending requests
     */
    public List<HelpRequest> getPendingRequests() { return requestDAO.getPendingRequests(); }

    /**
     * Get all requests submitted by a specific user.
     *
     * @param userId The requester's user ID
     * @return List of the user's help requests
     */
    public List<HelpRequest> getUserRequests(int userId) { return requestDAO.getRequestsByUserId(userId); }

    /**
     * Approve a help request (admin action).
     *
     * @param id Request ID to approve
     * @return true if the approval status was updated
     */
    public boolean approveRequest(int id) { return requestDAO.updateApprovalStatus(id, "APPROVED"); }

    /**
     * Reject a help request (admin action).
     *
     * @param id Request ID to reject
     * @return true if the rejection status was updated
     */
    public boolean rejectRequest(int id) { return requestDAO.updateApprovalStatus(id, "REJECTED"); }

    // ── Response management ──────────────────────────────────────────────────

    /**
     * Submit a response to a help request.
     *
     * @param r RequestResponse populated with requestId, responderId, and message
     * @return Success or error message
     */
    public String addResponse(RequestResponse r) {
        if (r.getRequestId() <= 0 || r.getResponderId() <= 0) return "Invalid data";
        if (r.getResponseMessage() == null || r.getResponseMessage().trim().isEmpty()) return "Message is required";
        r.setResponseStatus("PENDING"); // starts as pending until accepted/rejected
        return responseDAO.addResponse(r) ? "Response submitted!" : "Failed to submit response.";
    }

    /**
     * Get all responses for a specific help request.
     *
     * @param requestId ID of the help request
     * @return List of responses with responder details
     */
    public List<RequestResponse> getResponsesForRequest(int requestId) { return responseDAO.getResponsesByRequestId(requestId); }

    /**
     * Accept a response (marks as ACCEPTED so the requester can act on it).
     *
     * @param responseId Response ID to accept
     * @return true if status was updated
     */
    public boolean acceptResponse(int responseId) { return responseDAO.updateStatus(responseId, "ACCEPTED"); }

    /**
     * Reject a response.
     *
     * @param responseId Response ID to reject
     * @return true if status was updated
     */
    public boolean rejectResponse(int responseId) { return responseDAO.updateStatus(responseId, "REJECTED"); }
}
