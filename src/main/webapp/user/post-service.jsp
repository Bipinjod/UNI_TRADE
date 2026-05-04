<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${formAction == 'edit' ? 'Edit Service' : 'Post a Service'} - UniTrade</title>
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
                <a href="${pageContext.request.contextPath}/user/services" class="nav-link active">Services</a>
                <a href="${pageContext.request.contextPath}/user/requests" class="nav-link">Help Requests</a>
                <a href="${pageContext.request.contextPath}/user/wishlist" class="nav-link">Wishlist</a>
            </div>
            <div class="nav-right">
                <div class="nav-avatar">${sessionScope.loggedInUser.fullName.substring(0,1)}</div>
                <a href="${pageContext.request.contextPath}/auth/logout" class="nav-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="page-wrapper">

        <div class="page-header">
            <div>
                <h1 class="page-title">${formAction == 'edit' ? 'Edit Service' : 'Offer a Service'}</h1>
                <p class="page-subtitle">${formAction == 'edit' ? 'Update your service listing' : 'Share your skills with other students'}</p>
            </div>
            <a href="${pageContext.request.contextPath}/user/services?action=my" class="btn btn-ghost">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
                Back
            </a>
        </div>

        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">
                <span><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></span> ${sessionScope.error}
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <div class="form-page-layout">
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/user/services" class="item-form">
                    <input type="hidden" name="action" value="${formAction}">
                    <c:if test="${formAction == 'edit' and not empty service}">
                        <input type="hidden" name="serviceId" value="${service.serviceId}">
                    </c:if>

                    <div class="form-group">
                        <label for="title" class="form-label">Service Title <span class="required">*</span></label>
                        <input type="text" id="title" name="title" class="form-input" required maxlength="150"
                               placeholder="e.g. Math Tutoring, Graphic Design, Photography"
                               value="${not empty service ? service.title : ''}">
                    </div>

                    <div class="form-row-2">
                        <div class="form-group">
                            <label for="categoryId" class="form-label">Category <span class="required">*</span></label>
                            <select id="categoryId" name="categoryId" class="form-select" required>
                                <option value="">Select Category</option>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}" ${not empty service and service.categoryId == cat.categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="price" class="form-label">Price (Rs.) <span class="required">*</span></label>
                            <div class="input-prefix-wrapper">
                                <span class="input-prefix">Rs.</span>
                                <input type="number" id="price" name="price" class="form-input with-prefix"
                                       required min="0" step="0.01" placeholder="0.00"
                                       value="${not empty service ? service.price : ''}">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description" class="form-label">Description <span class="required">*</span></label>
                        <textarea id="description" name="description" class="form-textarea" rows="5" required minlength="20"
                                  placeholder="Describe your service, what you offer, your experience...">${not empty service ? service.description : ''}</textarea>
                    </div>

                    <c:if test="${formAction == 'edit'}">
                        <div class="form-group">
                            <label for="availabilityStatus" class="form-label">Availability</label>
                            <select id="availabilityStatus" name="availabilityStatus" class="form-select">
                                <option value="AVAILABLE" ${not empty service and service.availabilityStatus == 'AVAILABLE' ? 'selected' : ''}>Available</option>
                                <option value="UNAVAILABLE" ${not empty service and service.availabilityStatus == 'UNAVAILABLE' ? 'selected' : ''}>Unavailable</option>
                            </select>
                        </div>
                    </c:if>

                    <div class="form-notice">
                        <span class="notice-icon"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg></span>
                        <span>Your service will be reviewed by an admin before it appears publicly.</span>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary btn-lg">
                            ${formAction == 'edit' ? 'Update Service' : 'Submit Service'}
                        </button>
                        <a href="${pageContext.request.contextPath}/user/services?action=my" class="btn btn-ghost btn-lg">Cancel</a>
                    </div>
                </form>
            </div>

            <div class="form-sidebar">
                <div class="tips-card">
                    <h3 class="tips-title">Service Tips</h3>
                    <ul class="tips-list">
                        <li>Use a clear, descriptive title</li>
                        <li>Explain your qualifications</li>
                        <li>Set a competitive price</li>
                        <li>Be specific about what's included</li>
                    </ul>
                </div>
            </div>
        </div>

    </div>

    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

