<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.css">
</head>
<body class="user-page">

    <nav class="user-nav">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-logo" style="display:flex;align-items:center;gap:8px;">
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg" alt="UniTrade logo" class="brand-logo-img">
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
                <h1 class="page-title">My Profile</h1>
                <p class="page-subtitle">Manage your account details</p>
            </div>
            <a href="${pageContext.request.contextPath}/user/dashboard" class="btn btn-ghost">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
                Back to Dashboard
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

        <div class="form-page-layout">
            <!-- Profile Form -->
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/user/profile" class="item-form">
                    <div class="form-group">
                        <label for="fullName" class="form-label">Full Name <span class="required">*</span></label>
                        <input type="text" id="fullName" name="fullName" class="form-input" required value="${profileUser.fullName}">
                    </div>
                    <div class="form-row-2">
                        <div class="form-group">
                            <label for="email" class="form-label">Email <span class="required">*</span></label>
                            <input type="email" id="email" name="email" class="form-input" required value="${profileUser.email}">
                        </div>
                        <div class="form-group">
                            <label for="phone" class="form-label">Phone <span class="required">*</span></label>
                            <input type="text" id="phone" name="phone" class="form-input" required value="${profileUser.phone}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="collegeName" class="form-label">College <span class="required">*</span></label>
                        <input type="text" id="collegeName" name="collegeName" class="form-input" required value="${profileUser.collegeName}">
                    </div>
                    <div class="form-row-2">
                        <div class="form-group">
                            <label for="courseName" class="form-label">Course <span class="required">*</span></label>
                            <input type="text" id="courseName" name="courseName" class="form-input" required value="${profileUser.courseName}">
                        </div>
                        <div class="form-group">
                            <label for="academicYear" class="form-label">Academic Year <span class="required">*</span></label>
                            <input type="text" id="academicYear" name="academicYear" class="form-input" required value="${profileUser.academicYear}">
                        </div>
                    </div>
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary btn-lg">Update Profile</button>
                    </div>
                </form>
            </div>

            <!-- Change Password Sidebar -->
            <div class="form-sidebar">
                <div class="tips-card">
                    <h3 class="tips-title">Change Password</h3>
                    <form method="post" action="${pageContext.request.contextPath}/user/profile">
                        <input type="hidden" name="action" value="changePassword">
                        <div class="form-group" style="margin-bottom:0.75rem;">
                            <label class="form-label" style="font-size:0.8125rem;">Current Password</label>
                            <input type="password" name="currentPassword" class="form-input" required>
                        </div>
                        <div class="form-group" style="margin-bottom:0.75rem;">
                            <label class="form-label" style="font-size:0.8125rem;">New Password</label>
                            <input type="password" name="newPassword" class="form-input" required minlength="6">
                        </div>
                        <div class="form-group" style="margin-bottom:1rem;">
                            <label class="form-label" style="font-size:0.8125rem;">Confirm Password</label>
                            <input type="password" name="confirmPassword" class="form-input" required>
                        </div>
                        <button type="submit" class="btn btn-ghost" style="width:100%;">Change Password</button>
                    </form>
                </div>
                <div class="tips-card">
                    <h3 class="tips-title">Account Status</h3>
                    <p class="tips-text">
                        <strong>Role:</strong> ${profileUser.role}<br>
                        <strong>Approval:</strong> ${profileUser.approvalStatus}<br>
                        <strong>Status:</strong> ${profileUser.accountStatus}
                    </p>
                </div>
            </div>
        </div>

    </div>

    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

