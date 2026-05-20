package com.unitrade.service;

import com.unitrade.dao.RequestResponseDAO;
import com.unitrade.model.RequestResponse;

import java.util.List;

/**
 * RequestResponseService - Business logic layer for help-request responses.
 * <p>
 * Validates incoming response data and delegates persistence to
 * {@link RequestResponseDAO}. Also provides status management for the
 * PENDING → ACCEPTED / REJECTED workflow.
 * </p>
 */
public class RequestResponseService {

    /** DAO used for all database operations on the request_responses table. */
    private final RequestResponseDAO dao = new RequestResponseDAO();

    /**
     * Submit a new response to a help request after validation.
     * <p>
     * The response status is automatically set to PENDING on creation.
     * </p>
     *
     * @param r RequestResponse populated with requestId, responderId, and responseMessage
     * @return Success message if persisted, or a descriptive error message if validation fails
     */
    public String addResponse(RequestResponse r) {
        if (r.getRequestId() <= 0 || r.getResponderId() <= 0) return "Invalid data";
        if (r.getResponseMessage() == null || r.getResponseMessage().trim().isEmpty()) return "Message is required";
        if (r.getResponseMessage().length() > 255) return "Message too long (max 255 chars)";
        // All new responses start as PENDING until accepted/rejected by the requester
        r.setResponseStatus("PENDING");
        return dao.addResponse(r) ? "Response submitted successfully!" : "Failed to submit response.";
    }

    /**
     * Retrieve all responses for a specific help request.
     *
     * @param requestId ID of the help request
     * @return List of {@link RequestResponse} objects with responder name details, newest first
     */
    public List<RequestResponse> getResponsesForRequest(int requestId) {
        return dao.getResponsesByRequestId(requestId);
    }

    /**
     * Retrieve all responses submitted by a specific user across all requests.
     *
     * @param responderId The responder's user ID
     * @return List of {@link RequestResponse} objects with request title details, newest first
     */
    public List<RequestResponse> getResponsesByResponder(int responderId) {
        return dao.getResponsesByResponderId(responderId);
    }

    /**
     * Accept a response, marking it as ACCEPTED so the requester can act on it.
     *
     * @param responseId ID of the response to accept
     * @return {@code true} if the status was updated successfully
     */
    public boolean acceptResponse(int responseId) { return dao.updateStatus(responseId, "ACCEPTED"); }

    /**
     * Reject a response, marking it as REJECTED.
     *
     * @param responseId ID of the response to reject
     * @return {@code true} if the status was updated successfully
     */
    public boolean rejectResponse(int responseId) { return dao.updateStatus(responseId, "REJECTED"); }
}
