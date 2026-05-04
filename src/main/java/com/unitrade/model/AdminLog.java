package com.unitrade.model;

import java.sql.Timestamp;

/**
 * AdminLog model – represents the admin_logs table.
 */
public class AdminLog {

    private int logId;
    private int adminId;
    private String actionType;
    private String targetTable;
    private int targetId;
    private String actionDescription;
    private Timestamp createdAt;

    // joined
    private String adminName;

    public AdminLog() {}

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getTargetTable() { return targetTable; }
    public void setTargetTable(String targetTable) { this.targetTable = targetTable; }

    public int getTargetId() { return targetId; }
    public void setTargetId(int targetId) { this.targetId = targetId; }

    public String getActionDescription() { return actionDescription; }
    public void setActionDescription(String actionDescription) { this.actionDescription = actionDescription; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }
}

