package com.unitrade.service;

import com.unitrade.dao.HelpRequestDAO;
import com.unitrade.dao.RequestResponseDAO;
import com.unitrade.model.HelpRequest;
import com.unitrade.model.RequestResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * HelpRequestService – business logic for help requests.
 */
public class HelpRequestService {

    private final HelpRequestDAO requestDAO = new HelpRequestDAO();
    private final RequestResponseDAO responseDAO = new RequestResponseDAO();

    public String addRequest(HelpRequest r) {
        if (r.getUserId() <= 0) return "Invalid user";
        if (r.getCategoryId() <= 0) return "Please select a category";
        if (r.getTitle() == null || r.getTitle().trim().isEmpty()) return "Title is required";
        if (r.getDescription() == null || r.getDescription().trim().length() < 20) return "Description must be at least 20 characters";
        if (r.getBudget() == null || r.getBudget().compareTo(BigDecimal.ZERO) < 0) return "Invalid budget";
        r.setRequestStatus("OPEN");
        r.setApprovalStatus("PENDING");
        return requestDAO.addRequest(r) ? "Request posted! Waiting for admin approval." : "Failed to post request.";
    }

    public String updateRequest(HelpRequest r) {
        if (r.getRequestId() <= 0) return "Invalid request ID";
        if (r.getTitle() == null || r.getTitle().trim().isEmpty()) return "Title is required";
        return requestDAO.updateRequest(r) ? "Request updated successfully" : "Failed to update request.";
    }

    public boolean deleteRequest(int requestId, int userId) { return requestDAO.deleteRequest(requestId, userId); }
    public HelpRequest getRequestById(int id) { return requestDAO.getRequestById(id); }
    public List<HelpRequest> getApprovedRequests() { return requestDAO.getApprovedRequests(); }
    public List<HelpRequest> getPendingRequests() { return requestDAO.getPendingRequests(); }
    public List<HelpRequest> getUserRequests(int userId) { return requestDAO.getRequestsByUserId(userId); }
    public boolean approveRequest(int id) { return requestDAO.updateApprovalStatus(id, "APPROVED"); }
    public boolean rejectRequest(int id) { return requestDAO.updateApprovalStatus(id, "REJECTED"); }

    // responses
    public String addResponse(RequestResponse r) {
        if (r.getRequestId() <= 0 || r.getResponderId() <= 0) return "Invalid data";
        if (r.getResponseMessage() == null || r.getResponseMessage().trim().isEmpty()) return "Message is required";
        r.setResponseStatus("PENDING");
        return responseDAO.addResponse(r) ? "Response submitted!" : "Failed to submit response.";
    }
    public List<RequestResponse> getResponsesForRequest(int requestId) { return responseDAO.getResponsesByRequestId(requestId); }
    public boolean acceptResponse(int responseId) { return responseDAO.updateStatus(responseId, "ACCEPTED"); }
    public boolean rejectResponse(int responseId) { return responseDAO.updateStatus(responseId, "REJECTED"); }
}

