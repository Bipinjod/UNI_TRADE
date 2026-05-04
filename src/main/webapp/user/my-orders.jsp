<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders - UniTrade</title>
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
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg" alt="UniTrade" class="brand-logo-img">
                <span>UniTrade</span>
            </a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/user/items" class="nav-link">Browse Items</a>
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

        <div class="page-header">
            <div>
                <h1 class="page-title">My Orders</h1>
                <p class="page-subtitle">Track your purchases and sales</p>
            </div>
            <a href="${pageContext.request.contextPath}/user/items" class="btn btn-primary">Browse Items</a>
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

        <!-- Buyer Orders -->
        <div class="section" style="margin-bottom:2rem;">
            <h2 class="section-title">Orders I Placed</h2>
            <c:choose>
                <c:when test="${not empty buyerOrders}">
                    <div class="table-card">
                        <div class="table-wrapper">
                            <table class="listings-table">
                                <thead>
                                    <tr>
                                        <th>Item</th>
                                        <th>Seller</th>
                                        <th>Status</th>
                                        <th>Date</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="order" items="${buyerOrders}">
                                        <tr>
                                            <td><span class="table-item-title">${order.itemTitle}</span></td>
                                            <td>${order.sellerName}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${order.orderStatus == 'PENDING'}"><span class="badge badge-warning">Pending</span></c:when>
                                                    <c:when test="${order.orderStatus == 'ACCEPTED'}"><span class="badge badge-success">Accepted</span></c:when>
                                                    <c:when test="${order.orderStatus == 'COMPLETED'}"><span class="badge badge-info">Completed</span></c:when>
                                                    <c:when test="${order.orderStatus == 'REJECTED'}"><span class="badge badge-danger">Rejected</span></c:when>
                                                    <c:otherwise><span class="badge badge-secondary">Cancelled</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><span class="date-text"><fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy"/></span></td>
                                            <td>
                                                <c:if test="${order.orderStatus == 'PENDING'}">
                                                    <form method="post" action="${pageContext.request.contextPath}/user/orders" style="display:inline;">
                                                        <input type="hidden" name="action" value="cancel">
                                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                                        <button type="submit" class="action-btn action-btn-delete" title="Cancel" onclick="return confirm('Cancel this order?')">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                                                        </button>
                                                    </form>
                                                </c:if>
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
                        <div class="empty-icon"><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6"/></svg></div>
                        <h3>No orders placed yet</h3>
                        <p>Browse items and place your first order.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Seller Orders -->
        <div class="section">
            <h2 class="section-title">Orders I Received</h2>
            <c:choose>
                <c:when test="${not empty sellerOrders}">
                    <div class="table-card">
                        <div class="table-wrapper">
                            <table class="listings-table">
                                <thead>
                                    <tr>
                                        <th>Item</th>
                                        <th>Buyer</th>
                                        <th>Message</th>
                                        <th>Status</th>
                                        <th>Date</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="order" items="${sellerOrders}">
                                        <tr>
                                            <td><span class="table-item-title">${order.itemTitle}</span></td>
                                            <td>${order.buyerName}</td>
                                            <td><span style="font-size:0.875rem;color:var(--gray);">${order.message}</span></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${order.orderStatus == 'PENDING'}"><span class="badge badge-warning">Pending</span></c:when>
                                                    <c:when test="${order.orderStatus == 'ACCEPTED'}"><span class="badge badge-success">Accepted</span></c:when>
                                                    <c:when test="${order.orderStatus == 'COMPLETED'}"><span class="badge badge-info">Completed</span></c:when>
                                                    <c:when test="${order.orderStatus == 'REJECTED'}"><span class="badge badge-danger">Rejected</span></c:when>
                                                    <c:otherwise><span class="badge badge-secondary">Cancelled</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><span class="date-text"><fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy"/></span></td>
                                            <td>
                                                <div class="row-actions">
                                                    <c:if test="${order.orderStatus == 'PENDING'}">
                                                        <form method="post" action="${pageContext.request.contextPath}/user/orders" style="display:inline;">
                                                            <input type="hidden" name="action" value="accept">
                                                            <input type="hidden" name="orderId" value="${order.orderId}">
                                                            <button type="submit" class="action-btn action-btn-edit" title="Accept">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><polyline points="20 6 9 17 4 12"/></svg>
                                                            </button>
                                                        </form>
                                                        <form method="post" action="${pageContext.request.contextPath}/user/orders" style="display:inline;">
                                                            <input type="hidden" name="action" value="reject">
                                                            <input type="hidden" name="orderId" value="${order.orderId}">
                                                            <button type="submit" class="action-btn action-btn-delete" title="Reject">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                                                            </button>
                                                        </form>
                                                    </c:if>
                                                    <c:if test="${order.orderStatus == 'ACCEPTED'}">
                                                        <form method="post" action="${pageContext.request.contextPath}/user/orders" style="display:inline;">
                                                            <input type="hidden" name="action" value="complete">
                                                            <input type="hidden" name="orderId" value="${order.orderId}">
                                                            <button type="submit" class="btn btn-primary btn-sm">Complete</button>
                                                        </form>
                                                    </c:if>
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
                        <div class="empty-icon"><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></div>
                        <h3>No orders received yet</h3>
                        <p>Post items for sale and buyers will send you orders.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

    </div>

    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

