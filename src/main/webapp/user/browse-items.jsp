<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Items - UniTrade</title>
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
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg" alt="UniTrade logo" class="brand-logo-img">
            </a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/user/items" class="nav-link active">Browse Items</a>
                <a href="${pageContext.request.contextPath}/user/services" class="nav-link">Services</a>
                <a href="${pageContext.request.contextPath}/user/requests" class="nav-link">Help Requests</a>
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
                <h1 class="page-title">Browse Items</h1>
                <p class="page-subtitle">Discover items from students across your campus</p>
            </div>
        </div>

        <!-- Search and Filter Bar -->
        <div class="search-bar-card">
            <form method="get" action="${pageContext.request.contextPath}/user/items" class="search-form">
                <input type="hidden" name="action" value="browse">
                <div class="search-input-wrapper">
                    <span class="search-icon"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg></span>
                    <input
                        type="text"
                        name="keyword"
                        class="search-input"
                        placeholder="Search for books, electronics, furniture..."
                        value="${keyword}"
                    >
                </div>
                <div class="filter-group">
                    <select name="categoryId" class="filter-select">
                        <option value="">All Categories</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.categoryId}"
                                ${selectedCategoryId == cat.categoryId ? 'selected' : ''}>
                                ${cat.categoryName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary search-btn">Search</button>
                <c:if test="${not empty keyword or not empty selectedCategoryId}">
                    <a href="${pageContext.request.contextPath}/user/items" class="btn btn-ghost">Clear</a>
                </c:if>
            </form>
        </div>

        <!-- Results Info -->
        <c:if test="${not empty keyword or not empty selectedCategoryId}">
            <div class="results-info">
                <span class="results-count">${items.size()} results</span>
                <c:if test="${not empty keyword}">
                    <span class="results-tag">Keyword: "${keyword}"</span>
                </c:if>
            </div>
        </c:if>

        <!-- Session Messages -->
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

        <!-- Items Grid -->
        <c:choose>
            <c:when test="${not empty items}">
                <div class="items-grid">
                    <c:forEach var="item" items="${items}">
                        <div class="item-card">
                            <!-- Item Image -->
                            <div class="item-image-wrapper">
                                <c:choose>
                                    <c:when test="${not empty item.imagePath}">
                                        <img src="${pageContext.request.contextPath}/assets/uploads/${item.imagePath}" alt="${item.title}" class="item-image">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="item-image-placeholder"><svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></div>
                                    </c:otherwise>
                                </c:choose>

                                <!-- Condition Badge -->
                                <span class="item-condition-badge condition-${item.itemCondition.toLowerCase().replace('_', '-')}">
                                    <c:choose>
                                        <c:when test="${item.itemCondition == 'NEW'}">New</c:when>
                                        <c:when test="${item.itemCondition == 'LIKE_NEW'}">Like New</c:when>
                                        <c:when test="${item.itemCondition == 'GOOD'}">Good</c:when>
                                        <c:otherwise>Used</c:otherwise>
                                    </c:choose>
                                </span>

                                <!-- Wishlist Button -->
                                <c:set var="isWishlisted" value="${sessionScope.wishlistItemIds.contains(item.itemId)}"/>
                                <form method="post" action="${pageContext.request.contextPath}/user/wishlist" class="wishlist-form">
                                    <input type="hidden" name="action" value="${isWishlisted ? 'remove' : 'add'}">
                                    <input type="hidden" name="itemId" value="${item.itemId}">
                                    <input type="hidden" name="returnUrl" value="/user/items?action=browse">
                                    <button type="submit" class="wishlist-btn ${isWishlisted ? 'wishlisted' : ''}" title="${isWishlisted ? 'Remove from wishlist' : 'Add to wishlist'}">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="${isWishlisted ? 'currentColor' : 'none'}" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 000-7.78z"/></svg>
                                    </button>
                                </form>
                            </div>

                            <!-- Item Details -->
                            <a href="${pageContext.request.contextPath}/user/items?action=detail&itemId=${item.itemId}" style="text-decoration:none;color:inherit;">
                                <div class="item-body">
                                    <div class="item-category">${item.categoryName}</div>
                                    <h3 class="item-title">${item.title}</h3>
                                    <p class="item-description">
                                        <c:choose>
                                            <c:when test="${not empty item.description and fn:length(item.description) > 70}">
                                                ${fn:substring(item.description, 0, 70)}...
                                            </c:when>
                                            <c:otherwise>${item.description}</c:otherwise>
                                        </c:choose>
                                    </p>
                                    <div class="item-footer">
                                        <div class="item-price">
                                            Rs. <fmt:formatNumber value="${item.price}" pattern="#,##0"/>
                                        </div>
                                        <div class="item-seller">by ${item.sellerName}</div>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-icon"><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg></div>
                    <h3>No items found</h3>
                    <p>Try adjusting your search or browse all categories</p>
                    <a href="${pageContext.request.contextPath}/user/items" class="btn btn-primary">Browse All Items</a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>


