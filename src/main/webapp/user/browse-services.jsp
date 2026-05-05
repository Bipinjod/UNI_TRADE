<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Services - UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.css">
</head>
<body class="user-page">

    <nav class="user-nav">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-logo">
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg" alt="UniTrade logo" class="brand-logo-img">
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

        <c:if test="${not empty sessionScope.success}">
            <div class="notice notice-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="notice notice-error">${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <div class="page-header">
            <h1>Browse Services</h1>
            <div class="header-actions">
                <a href="${pageContext.request.contextPath}/user/services?action=my" class="btn btn-secondary">My Services</a>
                <a href="${pageContext.request.contextPath}/user/services?action=add" class="btn btn-primary">+ Offer a Service</a>
            </div>
        </div>

        <!-- Search / Filter -->
        <form class="filter-bar" method="get" action="${pageContext.request.contextPath}/user/services">
            <input type="text" name="keyword" value="${keyword}" placeholder="Search services..." class="filter-input">
            <select name="categoryId" class="filter-select">
                <option value="">All Categories</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat.categoryId}" ${cat.categoryId == selectedCategoryId ? 'selected' : ''}>${cat.categoryName}</option>
                </c:forEach>
            </select>
            <button type="submit" class="btn btn-primary">Search</button>
        </form>

        <!-- Services Grid -->
        <c:choose>
            <c:when test="${empty services}">
                <div class="empty-state">
                    <div class="empty-icon"><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg></div>
                    <h3>No services found</h3>
                    <p>Be the first to offer a service!</p>
                    <a href="${pageContext.request.contextPath}/user/services?action=add" class="btn btn-primary">Offer a Service</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="items-grid">
                    <c:forEach var="s" items="${services}">
                        <div class="item-card">
                            <div class="card-body">
                                <div class="card-category">${s.categoryName}</div>
                                <h3 class="card-title">${s.title}</h3>
                                <p class="card-desc">${s.description}</p>
                                <div class="card-footer">
                                    <span class="card-price">Rs. <fmt:formatNumber value="${s.price}" pattern="#,##0"/></span>
                                    <span class="card-status status-${s.availabilityStatus.toLowerCase()}">${s.availabilityStatus}</span>
                                </div>
                                <a href="${pageContext.request.contextPath}/user/services?action=detail&serviceId=${s.serviceId}" class="btn btn-secondary btn-sm">View Details</a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

