package com.unitrade.model;

import java.sql.Timestamp;

/**
 * RequestResponse model - represents a row in the request_responses table.
 * <p>
 * A RequestResponse is submitted by a student who wants to help with
 * another student's {@link HelpRequest}. The response goes through a
 * PENDING -> ACCEPTED / REJECTED workflow managed by the original requester.
 * </p>
 */
public class RequestResponse {

    // ── Primary Key ───────────────────────────────────────────────────────────
    /** Auto-generated primary key for this response. */
    private int responseId;

    // ── Foreign Keys ─────────────────────────────────────────────────────────
    /** ID of the help request this response belongs to. */
    private int requestId;
    /** ID of the user who submitted this response. */
    private int responderId;

    // ── Response Content ──────────────────────────────────────────────────────
    /** The text message the responder sent to the requester. */
    private String responseMessage;
    /**
     * Current moderation status of this response.
     * ENUM: PENDING (default), ACCEPTED, REJECTED.
     */
    private String responseStatus;

    // ── Timestamp ─────────────────────────────────────────────────────────────
    /** Timestamp when this response was created (set by the database). */
    private Timestamp createdAt;

    // ── Joined Display Fields (not stored in DB) ──────────────────────────────
    /** Responder's full name resolved from the users table via JOIN. */
    private String responderName;
    /** Title of the associated help request resolved from help_requests via JOIN. */
    private String requestTitle;

    /** Default no-arg constructor. */
    public RequestResponse() {}

    // ── Getters / Setters ─────────────────────────────────────────────────────

    /** @return Primary key of this response. */
    public int getResponseId() { return responseId; }
    /** @param responseId Primary key to set. */
    public void setResponseId(int responseId) { this.responseId = responseId; }

    /** @return ID of the associated help request. */
    public int getRequestId() { return requestId; }
    /** @param requestId Help request foreign key. */
    public void setRequestId(int requestId) { this.requestId = requestId; }

    /** @return ID of the user who submitted this response. */
    public int getResponderId() { return responderId; }
    /** @param responderId Responder's user ID. */
    public void setResponderId(int responderId) { this.responderId = responderId; }

    /** @return Text content of the response message. */
    public String getResponseMessage() { return responseMessage; }
    /** @param responseMessage The response message text. */
    public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }

    /** @return Current status of this response (PENDING, ACCEPTED, REJECTED). */
    public String getResponseStatus() { return responseStatus; }
    /** @param responseStatus New response status. */
    public void setResponseStatus(String responseStatus) { this.responseStatus = responseStatus; }

    /** @return Timestamp when this response was submitted. */
    public Timestamp getCreatedAt() { return createdAt; }
    /** @param createdAt Creation timestamp. */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    /** @return Responder's full name resolved via JOIN (may be null if not joined). */
    public String getResponderName() { return responderName; }
    /** @param responderName Joined responder full name. */
    public void setResponderName(String responderName) { this.responderName = responderName; }

    /** @return Title of the associated help request resolved via JOIN (may be null). */
    public String getRequestTitle() { return requestTitle; }
    /** @param requestTitle Joined help request title. */
    public void setRequestTitle(String requestTitle) { this.requestTitle = requestTitle; }
}
