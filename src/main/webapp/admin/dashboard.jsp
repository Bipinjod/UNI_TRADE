<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body class="admin-page">
<div class="admin-layout">
    <aside class="admin-sidebar">
        <div class="sidebar-header">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-logo">
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo-light.svg" alt="UniTrade" class="admin-logo-img">
            </a>
            <div class="admin-badge">Admin</div>
        </div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-item active">
                <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg></span>
                <span class="nav-text">Dashboard</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/users" class="nav-item">
                <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg></span>
                <span class="nav-text">Users</span>
                <c:if test="${pendingUsers > 0}"><span class="nav-badge">${pendingUsers}</span></c:if>
            </a>
            <a href="${pageContext.request.contextPath}/admin/categories" class="nav-item">
                <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg></span>
                <span class="nav-text">Categories</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/items" class="nav-item">
                <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></span>
                <span class="nav-text">Items</span>
                <c:if test="${pendingItems > 0}"><span class="nav-badge">${pendingItems}</span></c:if>
            </a>
            <a href="${pageContext.request.contextPath}/admin/services" class="nav-item">
                <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg></span>
                <span class="nav-text">Services</span>
                <c:if test="${pendingServices > 0}"><span class="nav-badge">${pendingServices}</span></c:if>
            </a>
            <a href="${pageContext.request.contextPath}/admin/requests" class="nav-item">
                <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></span>
                <span class="nav-text">Help Requests</span>
                <c:if test="${pendingRequests > 0}"><span class="nav-badge">${pendingRequests}</span></c:if>
            </a>
        </nav>
        <div class="sidebar-footer">
            <a href="${pageContext.request.contextPath}/auth/logout" class="logout-btn">
                <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg></span>
                <span class="nav-text">Logout</span>
            </a>
        </div>
    </aside>
    <div class="admin-main">
        <header class="admin-header">
            <div class="header-left">
                <h1 class="page-title">Admin Dashboard</h1>
                <p class="page-subtitle">Overview of the UniTrade platform</p>
            </div>
            <div class="header-right">
                <div class="user-info">
                    <div class="user-avatar">
                        <c:choose>
                            <c:when test="${not empty sessionScope.loggedInUser.fullName}">
                                <span class="avatar-text">${sessionScope.loggedInUser.fullName.substring(0,1)}</span>
                            </c:when>
                            <c:otherwise><span class="avatar-text">A</span></c:otherwise>
                        </c:choose>
                    </div>
                    <div class="user-details">
                        <div class="user-name">${sessionScope.loggedInUser.fullName}</div>
                        <div class="user-role">Administrator</div>
                    </div>
                </div>
            </div>
        </header>
        <div class="admin-content">
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
            <div class="stats-grid">
                <div class="stat-card stat-card-primary">
                    <div class="stat-icon"><svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg></div>
                    <div class="stat-info"><div class="stat-value">${totalUsers}</div><div class="stat-label">Total Users</div></div>
                </div>
                <div class="stat-card stat-card-warning">
                    <div class="stat-icon"><svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg></div>
                    <div class="stat-info"><div class="stat-value">${pendingUsers}</div><div class="stat-label">Pending Users</div></div>
                </div>
                <div class="stat-card stat-card-success">
                    <div class="stat-icon"><svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></div>
                    <div class="stat-info"><div class="stat-value">${totalItems}</div><div class="stat-label">Total Items</div></div>
                </div>
                <div class="stat-card stat-card-info">
                    <div class="stat-icon"><svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg></div>
                    <div class="stat-info"><div class="stat-value">${pendingItems}</div><div class="stat-label">Pending Items</div></div>
                </div>
                <div class="stat-card stat-card-primary">
                    <div class="stat-icon"><svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg></div>
                    <div class="stat-info"><div class="stat-value">${approvedServices}</div><div class="stat-label">Active Services</div></div>
                </div>
                <div class="stat-card stat-card-warning">
                    <div class="stat-icon"><svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/></svg></div>
                    <div class="stat-info"><div class="stat-value">${pendingRequests}</div><div class="stat-label">Pending Requests</div></div>
                </div>
            </div>
            <div class="section">
                <div class="section-header"><h2 class="section-title">Quick Actions</h2></div>
                <div class="quick-actions">
                    <a href="${pageContext.request.contextPath}/admin/users?filter=pending" class="action-card">
                        <div class="action-icon"><svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg></div>
                        <div class="action-text"><h3>Approve Users</h3><p>${pendingUsers} users waiting</p></div>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/items?filter=pending" class="action-card">
                        <div class="action-icon"><svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg></div>
                        <div class="action-text"><h3>Review Items</h3><p>${pendingItems} items pending</p></div>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/services" class="action-card">
                        <div class="action-icon"><svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg></div>
                        <div class="action-text"><h3>Review Services</h3><p>${pendingServices} services pending</p></div>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/requests" class="action-card">
                        <div class="action-icon"><svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></div>
                        <div class="action-text"><h3>Review Requests</h3><p>${pendingRequests} requests pending</p></div>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/categories" class="action-card">
                        <div class="action-icon"><svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg></div>
                        <div class="action-text"><h3>Manage Categories</h3><p>${totalCategories} categories</p></div>
                    </a>
                </div>
            </div>
            <div class="info-grid">
                <div class="info-card">
                    <h3 class="info-title">Active Categories</h3>
                    <div class="info-value">${activeCategories}</div>
                    <p class="info-desc">Currently active</p>
                </div>
                <div class="info-card">
                    <h3 class="info-title">Platform Status</h3>
                    <div class="status-badge badge-success">Operational</div>
                    <p class="info-desc">All systems running</p>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>