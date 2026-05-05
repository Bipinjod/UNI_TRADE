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

    <%-- ===== SIDEBAR ===== --%>
    <aside class="admin-sidebar" id="adminSidebar">
        <div class="sidebar-header">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-logo">
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo-light.svg"
                     alt="UniTrade" class="admin-logo-img">
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

    <%-- ===== MAIN AREA ===== --%>
    <div class="admin-main">

        <%-- TOP HEADER --%>
        <header class="admin-header">
            <div class="header-left">
                <div>
                    <h1 class="page-title">Admin Dashboard</h1>
                    <p class="page-subtitle">Platform overview and moderation control</p>
                </div>
            </div>
            <div class="header-right">
                <span class="header-status-badge">
                    <span class="hstatus-dot"></span>Operational
                </span>
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

        <%-- MAIN CONTENT --%>
        <div class="admin-content">

            <%-- Flash alerts --%>
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

            <%-- ===== HERO PANEL ===== --%>
            <div class="dash-hero">
                <div class="dash-hero-content">
                    <div class="dash-hero-status">PLATFORM OPERATIONAL</div>
                    <h2 class="dash-hero-title">Welcome back,&#32;<c:choose><c:when test="${not empty sessionScope.loggedInUser.fullName}">${sessionScope.loggedInUser.fullName}</c:when><c:otherwise>Admin</c:otherwise></c:choose></h2>
                    <p class="dash-hero-desc">Review pending content, approve new users, and keep the UniTrade platform running smoothly. All moderation tasks are one click away.</p>
                    <div class="dash-hero-counters">
                        <div class="dash-hero-counter">
                            <div class="dash-hero-counter-value">${pendingUsers}</div>
                            <div class="dash-hero-counter-label">Users awaiting</div>
                        </div>
                        <div class="dash-hero-counter">
                            <div class="dash-hero-counter-value">${pendingItems}</div>
                            <div class="dash-hero-counter-label">Items awaiting</div>
                        </div>
                        <div class="dash-hero-counter">
                            <div class="dash-hero-counter-value">${pendingServices}</div>
                            <div class="dash-hero-counter-label">Services awaiting</div>
                        </div>
                        <div class="dash-hero-counter">
                            <div class="dash-hero-counter-value">${pendingRequests}</div>
                            <div class="dash-hero-counter-label">Requests awaiting</div>
                        </div>
                    </div>
                </div>
                <div class="dash-hero-aside">
                    <a href="${pageContext.request.contextPath}/admin/users?filter=pending" class="dash-btn dash-btn-primary">
                        <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>
                        Review Users
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/items?filter=pending" class="dash-btn dash-btn-outline">
                        <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/></svg>
                        Review Listings
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/categories" class="dash-btn dash-btn-outline">
                        <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/></svg>
                        Manage Categories
                    </a>
                </div>
            </div>

            <%-- ===== STATS ROW ===== --%>
            <div class="dash-stats-row">
                <div class="dash-stat-item">
                    <div class="dash-stat-label"><svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>Total Users</div>
                    <div class="dash-stat-value">${totalUsers}</div>
                </div>
                <div class="dash-stat-item">
                    <div class="dash-stat-label"><svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/></svg>Total Items</div>
                    <div class="dash-stat-value">${totalItems}</div>
                </div>
                <div class="dash-stat-item">
                    <div class="dash-stat-label"><svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg>Active Services</div>
                    <div class="dash-stat-value">${approvedServices}</div>
                </div>
                <div class="dash-stat-item">
                    <div class="dash-stat-label"><svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/></svg>Active Categories</div>
                    <div class="dash-stat-value">${activeCategories}</div>
                </div>
                <div class="dash-stat-item dash-stat-pending">
                    <div class="dash-stat-label"><svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>Pending Approval</div>
                    <div class="dash-stat-value">${pendingUsers + pendingItems + pendingServices + pendingRequests}</div>
                </div>
            </div>

            <%-- ===== TWO-COLUMN SECTION ===== --%>
            <div class="dash-columns">

                <%-- LEFT: Moderation Queue --%>
                <div class="dash-card">
                    <div class="dash-card-header">
                        <h3 class="dash-card-title">Moderation Queue</h3>
                        <span class="dash-card-badge">${pendingUsers + pendingItems + pendingServices + pendingRequests} pending</span>
                    </div>
                    <div class="dash-card-body">
                        <a href="${pageContext.request.contextPath}/admin/users?filter=pending" class="queue-item">
                            <div class="queue-item-icon queue-icon-users"><svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg></div>
                            <div class="queue-item-info">
                                <div class="queue-item-title">Approve Users</div>
                                <div class="queue-item-subtitle">New user registrations</div>
                            </div>
                            <span class="queue-item-count${pendingUsers > 0 ? ' has-pending' : ''}">${pendingUsers}</span>
                            <span class="queue-item-arrow"><svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg></span>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/items?filter=pending" class="queue-item">
                            <div class="queue-item-icon queue-icon-items"><svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></div>
                            <div class="queue-item-info">
                                <div class="queue-item-title">Review Items</div>
                                <div class="queue-item-subtitle">Listed item submissions</div>
                            </div>
                            <span class="queue-item-count${pendingItems > 0 ? ' has-pending' : ''}">${pendingItems}</span>
                            <span class="queue-item-arrow"><svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg></span>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/services" class="queue-item">
                            <div class="queue-item-icon queue-icon-services"><svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg></div>
                            <div class="queue-item-info">
                                <div class="queue-item-title">Review Services</div>
                                <div class="queue-item-subtitle">Service offer submissions</div>
                            </div>
                            <span class="queue-item-count${pendingServices > 0 ? ' has-pending' : ''}">${pendingServices}</span>
                            <span class="queue-item-arrow"><svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg></span>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/requests" class="queue-item">
                            <div class="queue-item-icon queue-icon-requests"><svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></div>
                            <div class="queue-item-info">
                                <div class="queue-item-title">Review Help Requests</div>
                                <div class="queue-item-subtitle">Student help requests</div>
                            </div>
                            <span class="queue-item-count${pendingRequests > 0 ? ' has-pending' : ''}">${pendingRequests}</span>
                            <span class="queue-item-arrow"><svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg></span>
                        </a>
                    </div>
                </div>

                <%-- RIGHT: Platform Snapshot --%>
                <div class="dash-card">
                    <div class="dash-card-header">
                        <h3 class="dash-card-title">Platform Snapshot</h3>
                    </div>
                    <div class="dash-card-body">
                        <div class="snapshot-row">
                            <span class="snapshot-row-label"><span class="snapshot-dot dot-green"></span>System Status</span>
                            <span class="snapshot-status-text">Operational</span>
                        </div>
                        <div class="snapshot-row">
                            <span class="snapshot-row-label"><span class="snapshot-dot dot-primary"></span>Registered Users</span>
                            <span class="snapshot-row-value">${totalUsers}</span>
                        </div>
                        <div class="snapshot-row">
                            <span class="snapshot-row-label"><span class="snapshot-dot dot-info"></span>Items Listed</span>
                            <span class="snapshot-row-value">${totalItems}</span>
                        </div>
                        <div class="snapshot-row">
                            <span class="snapshot-row-label"><span class="snapshot-dot dot-primary"></span>Active Services</span>
                            <span class="snapshot-row-value">${approvedServices}</span>
                        </div>
                        <div class="snapshot-row">
                            <span class="snapshot-row-label"><span class="snapshot-dot dot-warning"></span>Categories</span>
                            <span class="snapshot-row-value">${activeCategories}</span>
                        </div>
                    </div>
                    <div class="dash-note">
                        <c:choose>
                            <c:when test="${pendingUsers + pendingItems + pendingServices + pendingRequests == 0}">
                                <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 11-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
                                <span class="dash-note-ok">All moderation queues are clear</span>
                            </c:when>
                            <c:otherwise>
                                <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
                                <span class="dash-note-warn">${pendingUsers + pendingItems + pendingServices + pendingRequests} items need attention</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </div>

            <%-- ===== QUICK ACCESS ===== --%>
            <div class="dash-section">
                <h2 class="dash-section-title">Quick Access</h2>
                <div class="quick-access">
                    <a href="${pageContext.request.contextPath}/admin/users" class="quick-chip">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/></svg>
                        Manage Users
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/items" class="quick-chip">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/></svg>
                        Manage Items
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/services" class="quick-chip">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg>
                        Manage Services
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/requests" class="quick-chip">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>
                        Help Requests
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/categories" class="quick-chip">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/></svg>
                        Categories
                    </a>
                </div>
            </div>

        </div><%-- /.admin-content --%>
    </div><%-- /.admin-main --%>
</div><%-- /.admin-layout --%>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

