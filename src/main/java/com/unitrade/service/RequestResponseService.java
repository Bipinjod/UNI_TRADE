package com.unitrade.service;

import com.unitrade.dao.RequestResponseDAO;
import com.unitrade.model.RequestResponse;

import java.util.List;

/**
 * RequestResponseService – business logic for help-request responses.
 */
public class RequestResponseService {

    private final RequestResponseDAO dao = new RequestResponseDAO();

    public String addResponse(RequestResponse r) {
        if (r.getRequestId() <= 0 || r.getResponderId() <= 0) return "Invalid data";
        if (r.getResponseMessage() == null || r.getResponseMessage().trim().isEmpty()) return "Message is required";
        if (r.getResponseMessage().length() > 255) return "Message too long (max 255 chars)";
        r.setResponseStatus("PENDING");
        return dao.addResponse(r) ? "Response submitted successfully!" : "Failed to submit response.";
    }

    public List<RequestResponse> getResponsesForRequest(int requestId) {
        return dao.getResponsesByRequestId(requestId);
    }

    public List<RequestResponse> getResponsesByResponder(int responderId) {
        return dao.getResponsesByResponderId(responderId);
    }

    public boolean acceptResponse(int responseId) { return dao.updateStatus(responseId, "ACCEPTED"); }
    public boolean rejectResponse(int responseId) { return dao.updateStatus(responseId, "REJECTED"); }
}

