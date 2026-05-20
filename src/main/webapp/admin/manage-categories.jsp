<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Categories - Admin - UniTrade</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">

    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body class="admin-page">

    <!-- Admin Layout -->
    <div class="admin-layout">

        <!-- Sidebar -->
        <aside class="admin-sidebar">
            <div class="sidebar-header">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-logo">
                    <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo-light.svg" alt="UniTrade" class="admin-logo-img">
                </a>
                <div class="admin-badge">Admin</div>
            </div>

            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg></span>
                    <span class="nav-text">Dashboard</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/users" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg></span>
                    <span class="nav-text">Users</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/categories" class="nav-item active">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg></span>
                    <span class="nav-text">Categories</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/items" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></span>
                    <span class="nav-text">Items</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/services" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg></span>
                    <span class="nav-text">Services</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/requests" class="nav-item">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg></span>
                    <span class="nav-text">Help Requests</span>
                </a>
            </nav>

            <div class="sidebar-footer">
                <a href="${pageContext.request.contextPath}/auth/logout" class="logout-btn">
                    <span class="nav-icon"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg></span>
                    <span class="nav-text">Logout</span>
                </a>
            </div>
        </aside>

        <!-- Main Content -->
        <div class="admin-main">

            <!-- Top Header -->
            <header class="admin-header">
                <div class="header-left">
                    <h1 class="page-title">Manage Categories</h1>
                    <p class="page-subtitle">Organize your marketplace categories</p>
                </div>
                <div class="header-right">
                    <div class="user-info">
                        <div class="user-avatar">
                            <span class="avatar-text">${sessionScope.loggedInUser.fullName.substring(0,1)}</span>
                        </div>
                        <div class="user-details">
                            <div class="user-name">${sessionScope.loggedInUser.fullName}</div>
                            <div class="user-role">Administrator</div>
                        </div>
                    </div>
                </div>
            </header>

            <!-- Content Area -->
            <div class="admin-content">

                <!-- Alert Messages -->
                <c:if test="${not empty sessionScope.success}">
                    <div class="alert alert-success">
                        <span class="alert-text">${sessionScope.success}</span>
                    </div>
                    <c:remove var="success" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-error">
                        <span class="alert-text">${sessionScope.error}</span>
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <!-- Two Column Layout -->
                <div class="two-column-layout">

                    <!-- Left Column - Form -->
                    <div class="column">
                        <div class="form-card">
                            <div class="card-header">
                                <h2 class="card-title">
                                    <c:choose>
                                        <c:when test="${not empty editCategory}">
                                            Edit Category
                                        </c:when>
                                        <c:otherwise>
                                            Add New Category
                                        </c:otherwise>
                                    </c:choose>
                                </h2>
                            </div>
                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/admin/categories" class="admin-form">

                                    <c:choose>
                                        <c:when test="${not empty editCategory}">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="categoryId" value="${editCategory.categoryId}">
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="action" value="add">
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="form-group">
                                        <label for="categoryName" class="form-label">
                                            Category Name
                                            <span class="required">*</span>
                                        </label>
                                        <input
                                            type="text"
                                            id="categoryName"
                                            name="categoryName"
                                            class="form-input"
                                            placeholder="e.g., Electronics, Books, Furniture"
                                            required
                                            value="${editCategory.categoryName}"
                                        >
                                    </div>

                                    <div class="form-group">
                                        <label for="description" class="form-label">
                                            Description
                                        </label>
                                        <textarea
                                            id="description"
                                            name="description"
                                            class="form-textarea"
                                            rows="3"
                                            placeholder="Brief description of the category"
                                        >${editCategory.description}</textarea>
                                    </div>

                                    <div class="form-group">
                                        <label for="status" class="form-label">
                                            Status
                                            <span class="required">*</span>
                                        </label>
                                        <select id="status" name="status" class="form-select" required>
                                            <option value="ACTIVE" ${editCategory.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                                            <option value="INACTIVE" ${editCategory.status == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                                        </select>
                                    </div>

                                    <div class="form-actions">
                                        <button type="submit" class="btn btn-primary btn-block">
                                            <c:choose>
                                                <c:when test="${not empty editCategory}">
                                                    Update Category
                                                </c:when>
                                                <c:otherwise>
                                                    Add Category
                                                </c:otherwise>
                                            </c:choose>
                                        </button>
                                        <c:if test="${not empty editCategory}">
                                            <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-secondary btn-block">
                                                Cancel
                                            </a>
                                        </c:if>
                                    </div>

                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Right Column - Categories List -->
                    <div class="column">
                        <div class="table-card">
                            <div class="card-header">
                                <h2 class="card-title">Categories</h2>
                                <div class="filter-tabs-small">
                                    <a href="${pageContext.request.contextPath}/admin/categories"
                                       class="filter-tab ${empty param.filter ? 'active' : ''}">
                                        All
                                    </a>
                                    <a href="${pageContext.request.contextPath}/admin/categories?filter=active"
                                       class="filter-tab ${param.filter == 'active' ? 'active' : ''}">
                                        Active
                                    </a>
                                </div>
                            </div>
                            <div class="card-body no-padding">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Category</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty categories}">
                                                <c:forEach var="category" items="${categories}">
                                                    <tr>
                                                        <td>
                                                            <div class="cell-title">${category.categoryName}</div>
                                                            <c:if test="${not empty category.description}">
                                                                <div class="cell-subtitle">${category.description}</div>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${category.status == 'ACTIVE'}">
                                                                    <span class="badge badge-success">Active</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge badge-secondary">Inactive</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <div class="action-buttons">
                                                                <a href="${pageContext.request.contextPath}/admin/categories?edit=${category.categoryId}"
                                                                   class="btn-icon btn-primary"
                                                                   title="Edit">
                                                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                                                                </a>
                                                                <form method="post" action="${pageContext.request.contextPath}/admin/categories" style="display:inline;">
                                                                    <input type="hidden" name="action" value="delete">
                                                                    <input type="hidden" name="categoryId" value="${category.categoryId}">
                                                                    <button type="submit" class="btn-icon btn-danger" title="Delete" onclick="return confirm('Are you sure you want to delete this category?')">
                                                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/></svg>
                                                                    </button>
                                                                </form>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td colspan="3" class="empty-state">
                                                        <div class="empty-text">No categories found</div>
                                                    </td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>

            </div>

        </div>

    </div>

    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>

