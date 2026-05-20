package com.unitrade.model;

import java.sql.Timestamp;

/**
 * AdminLog model - represents a row in the admin_logs table.
 * <p>
 * Every meaningful action taken by an administrator (approving a user,
 * rejecting an item, etc.) is recorded in this audit table so that there is a
 * traceable history of administrative decisions.
 * </p>
 */
public class AdminLog {

    // ── Primary Key ───────────────────────────────────────────────────────────
    /** Auto-generated primary key for this log entry. */
    private int logId;

    // ── Foreign Key ───────────────────────────────────────────────────────────
    /** ID of the admin user who performed the action. */
    private int adminId;

    // ── Action Details ────────────────────────────────────────────────────────
    /** Short type code for the action performed (e.g. APPROVE, REJECT, DELETE). */
    private String actionType;

    /** Database table that was affected (e.g. users, items, services). */
    private String targetTable;

    /** Primary key of the row that was affected in the target table. */
    private int targetId;

    /** Human-readable description of what was done, for display in the audit log. */
    private String actionDescription;

    // ── Timestamp ─────────────────────────────────────────────────────────────
    /** Timestamp when this log entry was created (set by the database). */
    private Timestamp createdAt;

    // ── Joined Display Field (not stored in DB) ────────────────────────────────
    /** Full name of the admin resolved from the users table via JOIN. */
    private String adminName;

    /** Default no-arg constructor required by the DAO mapping. */
    public AdminLog() {}

    // ── Getters / Setters ─────────────────────────────────────────────────────

    /** @return Primary key of this log entry. */
    public int getLogId() { return logId; }
    /** @param logId Primary key to set. */
    public void setLogId(int logId) { this.logId = logId; }

    /** @return ID of the admin who performed the action. */
    public int getAdminId() { return adminId; }
    /** @param adminId Admin user ID. */
    public void setAdminId(int adminId) { this.adminId = adminId; }

    /** @return Short action type code (e.g. APPROVE, DELETE). */
    public String getActionType() { return actionType; }
    /** @param actionType Action type code. */
    public void setActionType(String actionType) { this.actionType = actionType; }

    /** @return Name of the database table that was affected. */
    public String getTargetTable() { return targetTable; }
    /** @param targetTable Target table name. */
    public void setTargetTable(String targetTable) { this.targetTable = targetTable; }

    /** @return Primary key of the affected row in the target table. */
    public int getTargetId() { return targetId; }
    /** @param targetId Affected row ID. */
    public void setTargetId(int targetId) { this.targetId = targetId; }

    /** @return Human-readable description of the action. */
    public String getActionDescription() { return actionDescription; }
    /** @param actionDescription Descriptive text for the audit log. */
    public void setActionDescription(String actionDescription) { this.actionDescription = actionDescription; }

    /** @return Timestamp when this record was created. */
    public Timestamp getCreatedAt() { return createdAt; }
    /** @param createdAt Creation timestamp. */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    /** @return Admin's full name resolved via JOIN (may be null if not joined). */
    public String getAdminName() { return adminName; }
    /** @param adminName Joined admin full name. */
    public void setAdminName(String adminName) { this.adminName = adminName; }
}
