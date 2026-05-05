<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Help Requests - Admin - UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body class="admin-page">

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
                <a href="${pageContext.request.contextPath}/admin/users" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg></span>
                    <span class="nav-text">Users</span>
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
                <a href="${pageContext.request.contextPath}/admin/requests" class="nav-item active">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></span>
                    <span class="nav-text">Help Requests</span>
                    <c:if test="${pendingCount > 0}"><span class="nav-badge">${pendingCount}</span></c:if>
                </a>
            </nav>

            <div class="sidebar-footer">
                <a href="${pageContext.request.contextPath}/auth/logout" class="logout-btn">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg></span>
                    <span class="nav-text">Logout</span>
                </a>
            </div>
        </aside>

        <div class="admin-main">
            <header class="admin-header">
                <div class="header-left">
                    <h1 class="page-title">Manage Help Requests</h1>
                    <p class="page-subtitle">Review and moderate community help requests</p>
                </div>
                <div class="header-right">
                    <div class="user-info">
                        <div class="user-avatar"><span class="avatar-text">${sessionScope.loggedInUser.fullName.substring(0,1)}</span></div>
                        <div class="user-details">
                            <div class="user-name">${sessionScope.loggedInUser.fullName}</div>
                            <div class="user-role">Administrator</div>
                        </div>
                    </div>
                </div>
            </header>

            <div class="admin-content">

                <c:if test="${not empty sessionScope.success}">
                    <div class="alert alert-success"><span class="alert-text">${sessionScope.success}</span></div>
                    <c:remove var="success" scope="session"/>
                </c:if>
                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-error"><span class="alert-text">${sessionScope.error}</span></div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <div class="filter-bar">
                    <div class="filter-tabs">
                        <a href="${pageContext.request.contextPath}/admin/requests" class="filter-tab ${empty param.filter ? 'active' : ''}">Pending Review</a>
                        <a href="${pageContext.request.contextPath}/admin/requests?filter=approved" class="filter-tab ${param.filter == 'approved' ? 'active' : ''}">Approved</a>
                    </div>
                </div>

                <div class="table-container">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Request</th>
                                <th>Posted By</th>
                                <th>Category</th>
                                <th>Urgency</th>
                                <th>Budget</th>
                                <th>Status</th>
                                <th>Posted</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty requests}">
                                    <c:forEach var="hr" items="${requests}">
                                        <tr>
                                            <td>
                                                <div class="cell-title">${hr.title}</div>
                                                <div class="cell-subtitle">
                                                    <c:choose>
                                                        <c:when test="${not empty hr.description and fn:length(hr.description) > 50}">${fn:substring(hr.description, 0, 50)}...</c:when>
                                                        <c:otherwise>${hr.description}</c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                            <td><div class="cell-text">${hr.posterName}</div></td>
                                            <td><div class="cell-text">${hr.categoryName}</div></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${hr.urgencyLevel == 'HIGH'}"><span class="badge badge-danger">High</span></c:when>
                                                    <c:when test="${hr.urgencyLevel == 'MEDIUM'}"><span class="badge badge-warning">Medium</span></c:when>
                                                    <c:otherwise><span class="badge badge-info">Low</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${hr.budget != null && hr.budget.doubleValue() > 0}">
                                                    <div class="price-cell">Rs. <fmt:formatNumber value="${hr.budget}" pattern="#,##0"/></div>
                                                </c:if>
                                                <c:if test="${hr.budget == null || hr.budget.doubleValue() == 0}">
                                                    <span style="color:var(--gray);font-size:0.875rem;">Free</span>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${hr.approvalStatus == 'APPROVED'}"><span class="badge badge-success">Approved</span></c:when>
                                                    <c:when test="${hr.approvalStatus == 'PENDING'}"><span class="badge badge-warning">Pending</span></c:when>
                                                    <c:otherwise><span class="badge badge-danger">Rejected</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatDate value="${hr.createdAt}" pattern="MMM dd"/></td>
                                            <td>
                                                <div class="action-buttons">
                                                    <c:if test="${hr.approvalStatus == 'PENDING'}">
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/requests" style="display:inline;">
                                                            <input type="hidden" name="action" value="approve">
                                                            <input type="hidden" name="requestId" value="${hr.requestId}">
                                                            <button type="submit" class="btn-icon btn-success" title="Approve">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
                                                            </button>
                                                        </form>
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/requests" style="display:inline;">
                                                            <input type="hidden" name="action" value="reject">
                                                            <input type="hidden" name="requestId" value="${hr.requestId}">
                                                            <button type="submit" class="btn-icon btn-danger" title="Reject">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
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
                                        <td colspan="8" class="empty-state"><div class="empty-text">No help requests found</div></td>
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

