<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${item.title} - UniTrade</title>
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
            </a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/user/items" class="nav-link active">Browse Items</a>
                <a href="${pageContext.request.contextPath}/user/services" class="nav-link">Services</a>
                <a href="${pageContext.request.contextPath}/user/requests" class="nav-link">Help Requests</a>
                <a href="${pageContext.request.contextPath}/user/wishlist" class="nav-link">Wishlist</a>
            </div>
            <div class="nav-right">
                <a href="${pageContext.request.contextPath}/user/items?action=add" class="btn-post">+ Post Item</a>
                <a href="${pageContext.request.contextPath}/user/profile" class="nav-avatar">${sessionScope.loggedInUser.fullName.substring(0,1)}</a>
                <a href="${pageContext.request.contextPath}/auth/logout" class="nav-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="page-wrapper">

        <!-- Breadcrumb -->
        <div style="margin-bottom:1.5rem; font-size:0.875rem; color:var(--gray);">
            <a href="${pageContext.request.contextPath}/user/items" style="color:var(--primary); text-decoration:none;">&#8592; Back to Browse</a>
        </div>

        <!-- Flash Messages -->
        <c:if test="${not empty requestScope.error}">
            <div class="alert alert-error">${requestScope.error}</div>
        </c:if>

        <c:choose>
            <c:when test="${not empty item}">
                <div class="detail-grid">

                    <!-- Image -->
                    <div class="detail-image-box">
                        <c:choose>
                            <c:when test="${not empty item.imagePath}">
                                <img src="${pageContext.request.contextPath}/assets/uploads/${item.imagePath}" alt="${item.title}">
                            </c:when>
                            <c:otherwise><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.35"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg></c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Info -->
                    <div class="detail-info">
                        <div class="detail-category">${not empty item.categoryName ? item.categoryName : 'Item'}</div>
                        <h1 class="detail-title">${item.title}</h1>
                        <div class="detail-price">Rs. <fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></div>

                        <div class="detail-meta">
                            <span>Condition: ${item.itemCondition}</span>
                            <span>Status: ${item.listingStatus}</span>
                        </div>

                        <p class="detail-desc">${item.description}</p>

                        <div class="detail-seller">
                            <strong>Seller:</strong> ${not empty item.sellerName ? item.sellerName : 'Unknown'}
                        </div>

                        <div class="action-row">
                            <c:choose>
                                <c:when test="${isOwner}">
                                    <a href="${pageContext.request.contextPath}/user/items?action=edit&itemId=${item.itemId}" class="btn-primary">Edit Listing</a>
                                    <form method="post" action="${pageContext.request.contextPath}/user/items" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="itemId" value="${item.itemId}">
                                        <button type="submit" class="btn-ghost" onclick="return confirm('Delete this item?')">Delete</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${item.listingStatus == 'APPROVED'}">
                                        <form method="post" action="${pageContext.request.contextPath}/user/orders" style="display:inline;">
                                            <input type="hidden" name="action" value="create">
                                            <input type="hidden" name="itemId" value="${item.itemId}">
                                            <button type="submit" class="btn btn-primary" onclick="return confirm('Place an order for this item?')">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6"/></svg>
                                                Place Order
                                            </button>
                                        </form>
                                    </c:if>
                                    <form method="post" action="${pageContext.request.contextPath}/user/wishlist" style="display:inline;">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="itemId" value="${item.itemId}">
                                        <input type="hidden" name="returnUrl" value="/user/items?action=detail&itemId=${item.itemId}">
                                        <button type="submit" class="btn btn-ghost">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 000-7.78z"/></svg>
                                            Add to Wishlist
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-error">Item not found.</div>
                <a href="${pageContext.request.contextPath}/user/items" class="btn-ghost">&#8592; Back to Browse</a>
            </c:otherwise>
        </c:choose>

    </div>

</body>
</html>

