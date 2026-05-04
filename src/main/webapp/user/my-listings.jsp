<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Listings - UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.css">
</head>
<body class="user-page">

    <!-- Top Navigation -->
    <nav class="user-nav">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-logo">
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg" alt="UniTrade" class="brand-logo-img">
                <span>UniTrade</span>
            </a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/user/items" class="nav-link">Browse</a>
                <a href="${pageContext.request.contextPath}/user/items?action=my-listings" class="nav-link active">My Listings</a>
                <a href="${pageContext.request.contextPath}/user/wishlist" class="nav-link">Wishlist</a>
            </div>
            <div class="nav-right">
                <a href="${pageContext.request.contextPath}/user/items?action=add" class="btn-post">+ Post Item</a>
                <div class="nav-avatar">${sessionScope.loggedInUser.fullName.substring(0,1)}</div>
                <a href="${pageContext.request.contextPath}/auth/logout" class="nav-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="page-wrapper">

        <!-- Page Header -->
        <div class="page-header">
            <div>
                <h1 class="page-title">My Listings</h1>
                <p class="page-subtitle">Manage the items you've posted for sale</p>
            </div>
            <a href="${pageContext.request.contextPath}/user/items?action=add" class="btn btn-primary">
                + Post New Item
            </a>
        </div>

        <!-- Alert Messages -->
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

        <!-- Listings Content -->
        <c:choose>
            <c:when test="${not empty items}">

                <!-- Listings Table Card -->
                <div class="table-card">
                    <div class="table-header">
                        <span class="table-count">${items.size()} listing<c:if test="${items.size() != 1}">s</c:if></span>
                    </div>
                    <div class="table-wrapper">
                        <table class="listings-table">
                            <thead>
                                <tr>
                                    <th>Item</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                    <th>Condition</th>
                                    <th>Status</th>
                                    <th>Posted</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td>
                                            <div class="table-item-cell">
                                                <div class="table-item-img">
                                                    <c:choose>
                                                        <c:when test="${not empty item.imagePath}">
                                                            <img src="${pageContext.request.contextPath}/assets/uploads/${item.imagePath}" alt="${item.title}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="img-placeholder-sm"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div>
                                                    <div class="table-item-title">${item.title}</div>
                                                    <div class="table-item-desc">
                                                        <c:choose>
                                                            <c:when test="${not empty item.description and fn:length(item.description) > 55}">
                                                                ${fn:substring(item.description, 0, 55)}...
                                                            </c:when>
                                                            <c:otherwise>${item.description}</c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="category-tag">${item.categoryName}</span>
                                        </td>
                                        <td>
                                            <span class="price-text">
                                                Rs. <fmt:formatNumber value="${item.price}" pattern="#,##0"/>
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.itemCondition == 'NEW'}">
                                                    <span class="badge badge-info">New</span>
                                                </c:when>
                                                <c:when test="${item.itemCondition == 'LIKE_NEW'}">
                                                    <span class="badge badge-success">Like New</span>
                                                </c:when>
                                                <c:when test="${item.itemCondition == 'GOOD'}">
                                                    <span class="badge badge-primary">Good</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-secondary">Used</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.listingStatus == 'APPROVED'}">
                                                    <span class="badge badge-success">Approved</span>
                                                </c:when>
                                                <c:when test="${item.listingStatus == 'PENDING'}">
                                                    <span class="badge badge-warning">Pending</span>
                                                </c:when>
                                                <c:when test="${item.listingStatus == 'REJECTED'}">
                                                    <span class="badge badge-danger">Rejected</span>
                                                </c:when>
                                                <c:when test="${item.listingStatus == 'SOLD'}">
                                                    <span class="badge badge-sold">Sold</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-secondary">Inactive</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <span class="date-text">
                                                <fmt:formatDate value="${item.createdAt}" pattern="MMM dd, yyyy"/>
                                            </span>
                                        </td>
                                        <td>
                                            <div class="row-actions">
                                                <!-- Edit Button -->
                                                <a href="${pageContext.request.contextPath}/user/items?action=edit&itemId=${item.itemId}"
                                                   class="action-btn action-btn-edit" title="Edit">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                                                </a>
                                                <!-- Delete Button -->
                                                <form method="post" action="${pageContext.request.contextPath}/user/items" style="display:inline;">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="itemId" value="${item.itemId}">
                                                    <button type="submit"
                                                            class="action-btn action-btn-delete"
                                                            title="Delete"
                                                            onclick="return confirm('Are you sure you want to delete \'${item.title}\'?')">
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
                <!-- Empty State -->
                <div class="empty-state">
                    <div class="empty-icon"><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></div>
                    <h3>No listings yet</h3>
                    <p>You haven't posted any items for sale. Start by listing something!</p>
                    <a href="${pageContext.request.contextPath}/user/items?action=add" class="btn btn-primary">
                        + Post Your First Item
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</body>
</html>


