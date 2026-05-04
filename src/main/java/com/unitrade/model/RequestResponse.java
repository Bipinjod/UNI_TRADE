package com.unitrade.model;

import java.sql.Timestamp;

/**
 * RequestResponse model – represents the request_responses table.
 */
public class RequestResponse {

    private int responseId;
    private int requestId;
    private int responderId;
    private String responseMessage;
    private String responseStatus; // PENDING, ACCEPTED, REJECTED
    private Timestamp createdAt;

    // joined helpers
    private String responderName;
    private String requestTitle;

    public RequestResponse() {}

    public int getResponseId() { return responseId; }
    public void setResponseId(int responseId) { this.responseId = responseId; }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getResponderId() { return responderId; }
    public void setResponderId(int responderId) { this.responderId = responderId; }

    public String getResponseMessage() { return responseMessage; }
    public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }

    public String getResponseStatus() { return responseStatus; }
    public void setResponseStatus(String responseStatus) { this.responseStatus = responseStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getResponderName() { return responderName; }
    public void setResponderName(String responderName) { this.responderName = responderName; }

    public String getRequestTitle() { return requestTitle; }
    public void setRequestTitle(String requestTitle) { this.requestTitle = requestTitle; }
}

