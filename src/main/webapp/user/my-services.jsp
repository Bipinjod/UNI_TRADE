<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<%-- ============================================================
     my-services.jsp  –  User "My Services" dashboard page
     Shows:
       1. A table of services posted by the logged-in user
          (with edit / delete actions and approval badge).
       2. A section for incoming service orders placed by others.
     Data provided by ServiceServlet (action=my):
       • ${services}       – List<Service> owned by current user
       • ${receivedOrders} – List<ServiceOrder> directed at current user
     ============================================================ --%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Services - UniTrade</title>
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <!-- Application stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.css">
</head>
<body class="user-page">

    <%-- Debug helper: visible in page source – useful during development --%>
    <!-- Debug: contextPath = ${pageContext.request.contextPath} -->

    <%-- ===== Top Navigation Bar ===== --%>
    <nav class="user-nav">
        <div class="nav-container">
            <%-- Brand logo links back to the user dashboard --%>
            <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-logo">
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg" alt="UniTrade logo" class="brand-logo-img">
            </a>
            <%-- Primary navigation links (Services is the active section) --%>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/user/items" class="nav-link">Browse Items</a>
                <a href="${pageContext.request.contextPath}/user/services" class="nav-link active">Services</a>
                <a href="${pageContext.request.contextPath}/user/requests" class="nav-link">Help Requests</a>
                <a href="${pageContext.request.contextPath}/user/wishlist" class="nav-link">Wishlist</a>
            </div>
            <%-- Right side: Post button, avatar initial, and logout --%>
            <div class="nav-right">
                <a href="${pageContext.request.contextPath}/user/services?action=add" class="btn-post">+ Post Service</a>
                <a href="${pageContext.request.contextPath}/user/profile" class="nav-avatar">${sessionScope.loggedInUser.fullName.substring(0,1)}</a>
                <a href="${pageContext.request.contextPath}/auth/logout" class="nav-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="page-wrapper">

        <%-- ===== Page Header ===== --%>
        <div class="page-header">
            <div>
                <h1 class="page-title">My Services</h1>
                <p class="page-subtitle">Manage the services you offer</p>
            </div>
            <%-- Action buttons: browse all services or post a new one --%>
            <div style="display:flex;gap:0.75rem;">
                <a href="${pageContext.request.contextPath}/user/services" class="btn btn-ghost">Browse Services</a>
                <a href="${pageContext.request.contextPath}/user/services?action=add" class="btn btn-primary">+ Offer New Service</a>
            </div>
        </div>

        <%-- ===== Flash Messages ===== --%>
        <%-- Success message (e.g. service added/deleted/updated successfully) --%>
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">
                <span><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg></span> ${sessionScope.success}
            </div>
            <%-- Remove flash message after displaying so it doesn't reappear on refresh --%>
            <c:remove var="success" scope="session"/>
        </c:if>
        <%-- Error message (e.g. validation failure or permission denied) --%>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">
                <span><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></span> ${sessionScope.error}
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <%-- ===== My Services Table ===== --%>
        <%-- Show table if user has at least one service; otherwise show empty-state --%>
        <c:choose>
            <c:when test="${not empty services}">
                <div class="table-card">
                    <%-- Row count shown in the table header --%>
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
                                <%-- Iterate over each service posted by the current user --%>
                                <c:forEach var="s" items="${services}">
                                    <tr>
                                        <%-- Service title --%>
                                        <td>
                                            <div class="table-item-title">${s.title}</div>
                                        </td>
                                        <%-- Category badge --%>
                                        <td><span class="category-tag">${s.categoryName}</span></td>
                                        <%-- Formatted price (e.g. Rs. 1,500) --%>
                                        <td><span class="price-text">Rs. <fmt:formatNumber value="${s.price}" pattern="#,##0"/></span></td>
                                        <%-- Availability status badge – green if AVAILABLE, grey otherwise --%>
                                        <td>
                                            <span class="badge badge-${s.availabilityStatus == 'AVAILABLE' ? 'success' : 'secondary'}">${s.availabilityStatus}</span>
                                        </td>
                                        <%-- Admin approval status badge --%>
                                        <td>
                                            <c:choose>
                                                <c:when test="${s.approvalStatus == 'APPROVED'}"><span class="badge badge-success">Approved</span></c:when>
                                                <c:when test="${s.approvalStatus == 'PENDING'}"><span class="badge badge-warning">Pending</span></c:when>
                                                <c:otherwise><span class="badge badge-danger">Rejected</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <%-- Row actions: edit (GET) and delete (POST with confirmation) --%>
                                        <td>
                                            <div class="row-actions">
                                                <%-- Edit button – navigates to the edit form --%>
                                                <a href="${pageContext.request.contextPath}/user/services?action=edit&serviceId=${s.serviceId}" class="action-btn action-btn-edit" title="Edit">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                                                </a>
                                                <%-- Delete button – submits POST action=delete; requires confirmation --%>
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
            <%-- Empty state shown when the user has not posted any services yet --%>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-icon"><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg></div>
                    <h3>No services yet</h3>
                    <p>Start offering your skills to other students!</p>
                    <a href="${pageContext.request.contextPath}/user/services?action=add" class="btn btn-primary">Offer Your First Service</a>
                </div>
            </c:otherwise>
        </c:choose>

        <%-- ===== Incoming Service Requests Section ===== --%>
        <%-- Shows orders that other users have placed for the current user's services --%>
        <div style="margin-top:2.5rem;">
            <div class="page-header" style="margin-bottom:1.25rem;">
                <div>
                    <h2 class="page-title" style="font-size:1.25rem;">Incoming Requests</h2>
                    <p class="page-subtitle">Orders placed by others for your services</p>
                </div>
            </div>

            <c:choose>
                <c:when test="${not empty receivedOrders}">
                    <div class="table-card">
                        <div class="table-wrapper">
                            <table class="listings-table">
                                <thead>
                                    <tr>
                                        <th>Service</th>
                                        <th>From (Buyer)</th>
                                        <th>Message</th>
                                        <th>Status</th>
                                        <th>Date</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%-- Iterate over each incoming order --%>
                                    <c:forEach var="o" items="${receivedOrders}">
                                        <tr>
                                            <%-- Name of the service that was ordered --%>
                                            <td><div class="table-item-title">${o.serviceTitle}</div></td>
                                            <%-- Name of the buyer who placed the order --%>
                                            <td><span style="font-weight:500;">${o.buyerName}</span></td>
                                            <%-- Optional request message from the buyer --%>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty o.requestMessage}">
                                                        <span style="font-size:.85rem;color:var(--gray);">${o.requestMessage}</span>
                                                    </c:when>
                                                    <%-- Show dash when no message was provided --%>
                                                    <c:otherwise><span style="color:var(--gray-light);font-size:.8rem;">—</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <%-- Order status badge with colour coding --%>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${o.orderStatus == 'PENDING'}"><span class="badge badge-warning">Pending</span></c:when>
                                                    <c:when test="${o.orderStatus == 'ACCEPTED'}"><span class="badge badge-success">Accepted</span></c:when>
                                                    <c:when test="${o.orderStatus == 'COMPLETED'}"><span class="badge badge-sold">Completed</span></c:when>
                                                    <c:when test="${o.orderStatus == 'REJECTED'}"><span class="badge badge-danger">Rejected</span></c:when>
                                                    <c:when test="${o.orderStatus == 'CANCELLED'}"><span class="badge badge-secondary">Cancelled</span></c:when>
                                                    <c:otherwise><span class="badge badge-secondary">${o.orderStatus}</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <%-- Date the order was placed --%>
                                            <td style="font-size:.82rem;color:var(--gray);">
                                                <fmt:formatDate value="${o.createdAt}" pattern="MMM d, yyyy"/>
                                            </td>
                                            <%-- Context-sensitive action buttons depending on the current order status --%>
                                            <td>
                                                <div class="row-actions">
                                                    <%-- PENDING: Provider can Accept or Reject --%>
                                                    <c:if test="${o.orderStatus == 'PENDING'}">
                                                        <%-- Accept order --%>
                                                        <form method="post" action="${pageContext.request.contextPath}/user/services" style="display:inline;">
                                                            <input type="hidden" name="action" value="acceptOrder">
                                                            <input type="hidden" name="orderId" value="${o.serviceOrderId}">
                                                            <button type="submit" class="action-btn action-btn-edit" title="Accept" style="background:rgba(22,163,74,.1);color:#16a34a;border-color:rgba(22,163,74,.2);">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                                                            </button>
                                                        </form>
                                                        <%-- Reject order (with confirmation) --%>
                                                        <form method="post" action="${pageContext.request.contextPath}/user/services" style="display:inline;">
                                                            <input type="hidden" name="action" value="rejectOrder">
                                                            <input type="hidden" name="orderId" value="${o.serviceOrderId}">
                                                            <button type="submit" class="action-btn action-btn-delete" title="Reject" onclick="return confirm('Reject this request?')">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                                                            </button>
                                                        </form>
                                                    </c:if>
                                                    <%-- ACCEPTED: Provider can mark the work as Complete --%>
                                                    <c:if test="${o.orderStatus == 'ACCEPTED'}">
                                                        <form method="post" action="${pageContext.request.contextPath}/user/services" style="display:inline;">
                                                            <input type="hidden" name="action" value="completeOrder">
                                                            <input type="hidden" name="orderId" value="${o.serviceOrderId}">
                                                            <button type="submit" class="action-btn action-btn-edit" title="Mark Complete" style="background:rgba(15,118,110,.1);color:#0f766e;border-color:rgba(15,118,110,.2);">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 11-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
                                                            </button>
                                                        </form>
                                                    </c:if>
                                                    <%-- PENDING or ACCEPTED: either party can Cancel --%>
                                                    <c:if test="${o.orderStatus == 'PENDING' or o.orderStatus == 'ACCEPTED'}">
                                                        <form method="post" action="${pageContext.request.contextPath}/user/services" style="display:inline;">
                                                            <input type="hidden" name="action" value="cancelOrder">
                                                            <input type="hidden" name="orderId" value="${o.serviceOrderId}">
                                                            <button type="submit" class="action-btn" title="Cancel" style="background:rgba(100,116,139,.1);color:var(--gray);border:1px solid rgba(100,116,139,.2);" onclick="return confirm('Cancel this order?')">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>
                                                            </button>
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
                <%-- Empty state when no incoming orders exist yet --%>
                <c:otherwise>
                    <div class="empty-state" style="padding:2rem 1.5rem;">
                        <div class="empty-icon" style="margin-bottom:.75rem;"><svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.35"><path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/></svg></div>
                        <h3 style="font-size:1rem;">No requests yet</h3>
                        <p style="font-size:.875rem;">When someone requests your service, it will appear here.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

    </div><%-- end .page-wrapper --%>

    <%-- Application JavaScript --%>
    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

