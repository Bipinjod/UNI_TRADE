<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Services - UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.css">
</head>
<body class="user-page">

    <!-- Debug: contextPath = ${pageContext.request.contextPath} -->
    <nav class="user-nav">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-logo" style="display:flex;align-items:center;gap:8px;">
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg" alt="UniTrade logo" class="brand-logo-img">
                <span>UniTrade</span>
            </a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/user/items" class="nav-link">Browse Items</a>
                <a href="${pageContext.request.contextPath}/user/services" class="nav-link active">Services</a>
                <a href="${pageContext.request.contextPath}/user/requests" class="nav-link">Help Requests</a>
                <a href="${pageContext.request.contextPath}/user/wishlist" class="nav-link">Wishlist</a>
            </div>
            <div class="nav-right">
                <a href="${pageContext.request.contextPath}/user/services?action=add" class="btn-post">+ Post Service</a>
                <a href="${pageContext.request.contextPath}/user/profile" class="nav-avatar">${sessionScope.loggedInUser.fullName.substring(0,1)}</a>
                <a href="${pageContext.request.contextPath}/auth/logout" class="nav-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="page-wrapper">

        <div class="page-header">
            <div>
                <h1 class="page-title">My Services</h1>
                <p class="page-subtitle">Manage the services you offer</p>
            </div>
            <div style="display:flex;gap:0.75rem;">
                <a href="${pageContext.request.contextPath}/user/services" class="btn btn-ghost">Browse Services</a>
                <a href="${pageContext.request.contextPath}/user/services?action=add" class="btn btn-primary">+ Offer New Service</a>
            </div>
        </div>

        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">
                <span><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg></span> ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">
                <span><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></span> ${sessionScope.error}
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <c:choose>
            <c:when test="${not empty services}">
                <div class="table-card">
                    <div class="table-header">
                        <span class="table-count">${services.size()} service<c:if test="${services.size() != 1}">s</c:if></span>
                    </div>
                    <div class="table-wrapper">
                        <table class="listings-table">
                            <thead>
                                <tr>
                                    <th>Service</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                    <th>Availability</th>
                                    <th>Approval</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="s" items="${services}">
                                    <tr>
                                        <td>
                                            <div class="table-item-title">${s.title}</div>
                                        </td>
                                        <td><span class="category-tag">${s.categoryName}</span></td>
                                        <td><span class="price-text">Rs. <fmt:formatNumber value="${s.price}" pattern="#,##0"/></span></td>
                                        <td>
                                            <span class="badge badge-${s.availabilityStatus == 'AVAILABLE' ? 'success' : 'secondary'}">${s.availabilityStatus}</span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${s.approvalStatus == 'APPROVED'}"><span class="badge badge-success">Approved</span></c:when>
                                                <c:when test="${s.approvalStatus == 'PENDING'}"><span class="badge badge-warning">Pending</span></c:when>
                                                <c:otherwise><span class="badge badge-danger">Rejected</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="row-actions">
                                                <a href="${pageContext.request.contextPath}/user/services?action=edit&serviceId=${s.serviceId}" class="action-btn action-btn-edit" title="Edit">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                                                </a>
                                                <form method="post" action="${pageContext.request.contextPath}/user/services" style="display:inline;">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="serviceId" value="${s.serviceId}">
                                                    <button type="submit" class="action-btn action-btn-delete" title="Delete" onclick="return confirm('Delete this service?')">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/></svg>
                                                    </button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-icon"><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg></div>
                    <h3>No services yet</h3>
                    <p>Start offering your skills to other students!</p>
                    <a href="${pageContext.request.contextPath}/user/services?action=add" class="btn btn-primary">Offer Your First Service</a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

