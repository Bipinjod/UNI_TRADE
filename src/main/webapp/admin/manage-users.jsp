<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - Admin - UniTrade</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">

    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body class="admin-page">

    <!-- Admin Layout -->
    <div class="admin-layout">

        <!-- Sidebar -->
        <aside class="admin-sidebar">
            <div class="sidebar-header">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-logo">
                    <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo-light.svg" alt="UniTrade" class="admin-logo-img">
                </a>
                <div class="admin-badge">Admin</div>
            </div>

            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg></span>
                    <span class="nav-text">Dashboard</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/users" class="nav-item active">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg></span>
                    <span class="nav-text">Users</span>
                    <c:if test="${pendingCount > 0}">
                        <span class="nav-badge">${pendingCount}</span>
                    </c:if>
                </a>
                <a href="${pageContext.request.contextPath}/admin/categories" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg></span>
                    <span class="nav-text">Categories</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/items" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></span>
                    <span class="nav-text">Items</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/services" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg></span>
                    <span class="nav-text">Services</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/requests" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></span>
                    <span class="nav-text">Help Requests</span>
                </a>
            </nav>

            <div class="sidebar-footer">
                <a href="${pageContext.request.contextPath}/auth/logout" class="logout-btn">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg></span>
                    <span class="nav-text">Logout</span>
                </a>
            </div>
        </aside>

        <!-- Main Content -->
        <div class="admin-main">

            <!-- Top Header -->
            <header class="admin-header">
                <div class="header-left">
                    <h1 class="page-title">Manage Users</h1>
                    <p class="page-subtitle">Review and manage user accounts</p>
                </div>
                <div class="header-right">
                    <div class="user-info">
                        <div class="user-avatar">
                            <span class="avatar-text">${sessionScope.loggedInUser.fullName.substring(0,1)}</span>
                        </div>
                        <div class="user-details">
                            <div class="user-name">${sessionScope.loggedInUser.fullName}</div>
                            <div class="user-role">Administrator</div>
                        </div>
                    </div>
                </div>
            </header>

            <!-- Content Area -->
            <div class="admin-content">

                <!-- Alert Messages -->
                <c:if test="${not empty sessionScope.success}">
                    <div class="alert alert-success">
                        <span class="alert-icon"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 11-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg></span>
                        <span class="alert-text">${sessionScope.success}</span>
                    </div>
                    <c:remove var="success" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-error">
                        <span class="alert-icon"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg></span>
                        <span class="alert-text">${sessionScope.error}</span>
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <!-- Filter Bar -->
                <div class="filter-bar">
                    <div class="filter-tabs">
                        <a href="${pageContext.request.contextPath}/admin/users"
                           class="filter-tab ${empty param.filter ? 'active' : ''}">
                            All Users
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/users?filter=pending"
                           class="filter-tab ${param.filter == 'pending' ? 'active' : ''}">
                            Pending
                            <c:if test="${pendingCount > 0}">
                                <span class="filter-badge">${pendingCount}</span>
                            </c:if>
                        </a>
                    </div>
                </div>

                <!-- Users Table -->
                <div class="table-container">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>User</th>
                                <th>Contact</th>
                                <th>College</th>
                                <th>Role</th>
                                <th>Approval</th>
                                <th>Status</th>
                                <th>Joined</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty users}">
                                    <c:forEach var="user" items="${users}">
                                        <tr>
                                            <td>
                                                <div class="user-cell">
                                                    <div class="user-avatar-small">
                                                        <span>${user.fullName.substring(0,1)}</span>
                                                    </div>
                                                    <div>
                                                        <div class="cell-title">${user.fullName}</div>
                                                        <div class="cell-subtitle">${user.courseName}</div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="cell-title">${user.email}</div>
                                                <div class="cell-subtitle">${user.phone}</div>
                                            </td>
                                            <td>
                                                <div class="cell-text">${user.collegeName}</div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${user.role == 'ADMIN'}">
                                                        <span class="badge badge-accent">Admin</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-secondary">User</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${user.approvalStatus == 'APPROVED'}">
                                                        <span class="badge badge-success">Approved</span>
                                                    </c:when>
                                                    <c:when test="${user.approvalStatus == 'PENDING'}">
                                                        <span class="badge badge-warning">Pending</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-danger">Rejected</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${user.accountStatus == 'ACTIVE'}">
                                                        <span class="badge badge-success-outline">Active</span>
                                                    </c:when>
                                                    <c:when test="${user.accountStatus == 'BLOCKED'}">
                                                        <span class="badge badge-danger-outline">Blocked</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-secondary-outline">Inactive</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${user.createdAt}" pattern="MMM dd, yyyy"/>
                                            </td>
                                            <td>
                                                <div class="action-buttons">
                                                    <c:if test="${user.approvalStatus == 'PENDING'}">
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                                                            <input type="hidden" name="action" value="approve">
                                                            <input type="hidden" name="userId" value="${user.userId}">
                                                            <button type="submit" class="btn-icon btn-success" title="Approve">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
                                                            </button>
                                                        </form>
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                                                            <input type="hidden" name="action" value="reject">
                                                            <input type="hidden" name="userId" value="${user.userId}">
                                                            <button type="submit" class="btn-icon btn-danger" title="Reject">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                                                            </button>
                                                        </form>
                                                    </c:if>

                                                    <c:if test="${user.accountStatus == 'ACTIVE'}">
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                                                            <input type="hidden" name="action" value="block">
                                                            <input type="hidden" name="userId" value="${user.userId}">
                                                            <button type="submit" class="btn-icon btn-secondary" title="Block">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="4.93" y1="4.93" x2="19.07" y2="19.07"/></svg>
                                                            </button>
                                                        </form>
                                                    </c:if>

                                                    <c:if test="${user.accountStatus == 'BLOCKED'}">
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                                                            <input type="hidden" name="action" value="activate">
                                                            <input type="hidden" name="userId" value="${user.userId}">
                                                            <button type="submit" class="btn-icon btn-primary" title="Activate">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
                                                            </button>
                                                        </form>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="8" class="empty-state">
                                            <div class="empty-icon"><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.3"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg></div>
                                            <div class="empty-text">No users found</div>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

            </div>

        </div>

    </div>

    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

