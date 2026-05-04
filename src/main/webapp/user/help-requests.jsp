<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help Requests - UniTrade</title>
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
                <a href="${pageContext.request.contextPath}/user/requests" class="nav-link active">Help Requests</a>
                <a href="${pageContext.request.contextPath}/user/wishlist" class="nav-link">Wishlist</a>
            </div>
            <div class="nav-right">
                <a href="${pageContext.request.contextPath}/user/requests?action=add" class="btn-post">+ Post Request</a>
                <a href="${pageContext.request.contextPath}/user/profile" class="nav-avatar">${sessionScope.loggedInUser.fullName.substring(0,1)}</a>
                <a href="${pageContext.request.contextPath}/auth/logout" class="nav-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="page-wrapper">

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

        <!-- Detail View -->
        <c:if test="${not empty helpRequest}">
            <div class="page-header">
                <div>
                    <h1 class="page-title">${helpRequest.title}</h1>
                    <p class="page-subtitle">Posted by ${helpRequest.posterName}</p>
                </div>
                <a href="${pageContext.request.contextPath}/user/requests" class="btn btn-ghost">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
                    Back
                </a>
            </div>
            <div class="form-card" style="margin-bottom:1.5rem;">
                <p style="color:var(--gray);line-height:1.7;margin-bottom:1rem;">${helpRequest.description}</p>
                <div style="display:flex;gap:1rem;flex-wrap:wrap;">
                    <c:if test="${not empty helpRequest.categoryName}">
                        <span class="badge badge-primary">${helpRequest.categoryName}</span>
                    </c:if>
                    <span class="badge badge-${helpRequest.urgencyLevel == 'HIGH' ? 'danger' : helpRequest.urgencyLevel == 'MEDIUM' ? 'warning' : 'info'}">${helpRequest.urgencyLevel}</span>
                    <c:if test="${helpRequest.budget != null && helpRequest.budget.doubleValue() > 0}">
                        <span class="badge badge-success">Budget: Rs. <fmt:formatNumber value="${helpRequest.budget}" pattern="#,##0"/></span>
                    </c:if>
                </div>
            </div>

            <!-- Responses -->
            <h2 class="section-title" style="margin-bottom:1rem;">Responses</h2>
            <c:choose>
                <c:when test="${not empty responses}">
                    <c:forEach var="resp" items="${responses}">
                        <div class="form-card" style="margin-bottom:1rem;">
                            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:0.5rem;">
                                <strong style="color:var(--dark);">${resp.responderName}</strong>
                                <span class="badge badge-${resp.responseStatus == 'ACCEPTED' ? 'success' : resp.responseStatus == 'REJECTED' ? 'danger' : 'warning'}">${resp.responseStatus}</span>
                            </div>
                            <p style="color:var(--gray);line-height:1.6;">${resp.responseMessage}</p>
                            <c:if test="${isOwner and resp.responseStatus == 'PENDING'}">
                                <form method="post" action="${pageContext.request.contextPath}/user/requests" style="margin-top:0.75rem;">
                                    <input type="hidden" name="action" value="acceptResponse">
                                    <input type="hidden" name="responseId" value="${resp.responseId}">
                                    <input type="hidden" name="requestId" value="${helpRequest.requestId}">
                                    <button type="submit" class="btn btn-primary btn-sm">Accept Response</button>
                                </form>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p style="color:var(--gray);font-size:0.9375rem;">No responses yet.</p>
                </c:otherwise>
            </c:choose>

            <!-- Respond Form (if not owner) -->
            <c:if test="${not isOwner}">
                <div class="form-card" style="margin-top:1.5rem;">
                    <h3 class="tips-title">Submit a Response</h3>
                    <form method="post" action="${pageContext.request.contextPath}/user/requests">
                        <input type="hidden" name="action" value="respond">
                        <input type="hidden" name="requestId" value="${helpRequest.requestId}">
                        <div class="form-group">
                            <textarea name="responseMessage" class="form-textarea" rows="4" placeholder="Write your response or offer..." required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Submit Response</button>
                    </form>
                </div>
            </c:if>
        </c:if>

        <!-- List View -->
        <c:if test="${empty helpRequest}">
            <div class="page-header">
                <div>
                    <h1 class="page-title">${viewMode == 'my' ? 'My Help Requests' : 'Help Requests'}</h1>
                    <p class="page-subtitle">${viewMode == 'my' ? 'Manage your posted requests' : 'See what students need help with'}</p>
                </div>
                <div style="display:flex;gap:0.75rem;">
                    <a href="${pageContext.request.contextPath}/user/requests" class="btn btn-ghost">All Requests</a>
                    <a href="${pageContext.request.contextPath}/user/requests?action=my" class="btn btn-ghost">My Requests</a>
                    <a href="${pageContext.request.contextPath}/user/requests?action=add" class="btn btn-primary">+ Post Request</a>
                </div>
            </div>

            <c:choose>
                <c:when test="${not empty requests}">
                    <div class="items-grid">
                        <c:forEach var="hr" items="${requests}">
                            <div class="item-card">
                                <div class="item-body">
                                    <div class="item-category">${hr.categoryName}</div>
                                    <h3 class="item-title" style="margin-bottom:0.5rem;">${hr.title}</h3>
                                    <p class="item-description">${hr.description}</p>
                                    <div style="display:flex;gap:0.5rem;flex-wrap:wrap;margin-bottom:0.75rem;">
                                        <span class="badge badge-${hr.urgencyLevel == 'HIGH' ? 'danger' : hr.urgencyLevel == 'MEDIUM' ? 'warning' : 'info'}">${hr.urgencyLevel}</span>
                                        <span class="badge badge-${hr.requestStatus == 'OPEN' ? 'success' : 'secondary'}">${hr.requestStatus}</span>
                                        <c:if test="${hr.responseCount > 0}">
                                            <span class="badge badge-primary">${hr.responseCount} responses</span>
                                        </c:if>
                                    </div>
                                    <div class="item-footer">
                                        <c:if test="${hr.budget != null && hr.budget.doubleValue() > 0}">
                                            <div class="item-price">Rs. <fmt:formatNumber value="${hr.budget}" pattern="#,##0"/></div>
                                        </c:if>
                                        <div class="item-seller">by ${hr.posterName}</div>
                                    </div>
                                    <div style="margin-top:0.75rem;display:flex;gap:0.5rem;">
                                        <a href="${pageContext.request.contextPath}/user/requests?action=detail&requestId=${hr.requestId}" class="btn btn-ghost btn-sm">View Details</a>
                                        <c:if test="${viewMode == 'my'}">
                                            <form method="post" action="${pageContext.request.contextPath}/user/requests" style="display:inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="requestId" value="${hr.requestId}">
                                                <button type="submit" class="btn btn-ghost btn-sm" style="color:var(--danger);" onclick="return confirm('Delete this request?')">Delete</button>
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <div class="empty-icon"><svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="opacity:0.4"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></div>
                        <h3>No help requests found</h3>
                        <p>Be the first to ask for help from the community.</p>
                        <a href="${pageContext.request.contextPath}/user/requests?action=add" class="btn btn-primary">Post a Request</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:if>

    </div>

    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

