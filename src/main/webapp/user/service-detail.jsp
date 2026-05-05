<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${service.title} - UniTrade</title>
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
            </a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/user/items" class="nav-link">Browse Items</a>
                <a href="${pageContext.request.contextPath}/user/services" class="nav-link active">Services</a>
                <a href="${pageContext.request.contextPath}/user/requests" class="nav-link">Help Requests</a>
                <a href="${pageContext.request.contextPath}/user/wishlist" class="nav-link">Wishlist</a>
            </div>
            <div class="nav-right">
                <a href="${pageContext.request.contextPath}/user/services?action=add" class="btn-post">+ Post Service</a>
                <a href="${pageContext.request.contextPath}/auth/logout" class="nav-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="page-wrapper">

        <div style="margin-bottom:1.5rem; font-size:0.875rem; color:var(--gray);">
            <a href="${pageContext.request.contextPath}/user/services" style="color:var(--primary); text-decoration:none;">
                <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="vertical-align:middle;margin-right:0.25rem;"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
                Back to Services
            </a>
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
            <c:when test="${not empty service}">
                <div class="form-card" style="max-width:800px;">
                    <div style="margin-bottom:0.75rem;">
                        <span class="item-category">${service.categoryName}</span>
                    </div>
                    <h1 style="font-family:'Poppins',sans-serif;font-size:1.75rem;font-weight:700;color:var(--dark);margin-bottom:0.75rem;">${service.title}</h1>
                    <div style="font-family:'Poppins',sans-serif;font-size:1.5rem;font-weight:700;color:var(--primary);margin-bottom:1rem;">
                        Rs. <fmt:formatNumber value="${service.price}" pattern="#,##0"/>
                    </div>
                    <div style="display:flex;gap:0.75rem;margin-bottom:1.25rem;">
                        <span class="badge badge-${service.availabilityStatus == 'AVAILABLE' ? 'success' : 'secondary'}">${service.availabilityStatus}</span>
                        <span class="badge badge-${service.approvalStatus == 'APPROVED' ? 'success' : service.approvalStatus == 'PENDING' ? 'warning' : 'danger'}">${service.approvalStatus}</span>
                    </div>
                    <p style="color:var(--gray);line-height:1.7;font-size:0.9375rem;margin-bottom:1.5rem;">${service.description}</p>
                    <div style="background:var(--light);border-radius:var(--radius);padding:1rem 1.25rem;font-size:0.875rem;color:var(--gray);margin-bottom:1.5rem;">
                        <strong style="color:var(--dark);">Provider:</strong> ${service.providerName}
                    </div>

                    <c:choose>
                        <c:when test="${isOwner}">
                            <div style="display:flex;gap:1rem;">
                                <a href="${pageContext.request.contextPath}/user/services?action=edit&serviceId=${service.serviceId}" class="btn btn-primary">Edit Service</a>
                                <form method="post" action="${pageContext.request.contextPath}/user/services" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="serviceId" value="${service.serviceId}">
                                    <button type="submit" class="btn btn-ghost" onclick="return confirm('Delete this service?')">Delete</button>
                                </form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="form-card" style="background:var(--light);border:1px solid var(--border);">
                                <h3 class="tips-title">Request This Service</h3>
                                <form method="post" action="${pageContext.request.contextPath}/user/services">
                                    <input type="hidden" name="action" value="order">
                                    <input type="hidden" name="serviceId" value="${service.serviceId}">
                                    <div class="form-group">
                                        <textarea name="message" class="form-textarea" rows="3" placeholder="Write a message to the provider..."></textarea>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Send Request</button>
                                </form>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-error">Service not found.</div>
                <a href="${pageContext.request.contextPath}/user/services" class="btn btn-ghost">Back to Services</a>
            </c:otherwise>
        </c:choose>

    </div>

    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

