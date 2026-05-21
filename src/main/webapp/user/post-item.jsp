<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty item ? 'Edit Item' : 'Post Item'} - UniTrade</title>
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
                <a href="${pageContext.request.contextPath}/user/items" class="nav-link">Browse</a>
                <a href="${pageContext.request.contextPath}/user/items?action=my-listings" class="nav-link">My Listings</a>
                <a href="${pageContext.request.contextPath}/user/wishlist" class="nav-link">Wishlist</a>
            </div>
            <div class="nav-right">
                <a href="${pageContext.request.contextPath}/user/items?action=add" class="btn-post active">+ Post Item</a>
                <div class="nav-avatar">${sessionScope.loggedInUser.fullName.substring(0,1)}</div>
                <a href="${pageContext.request.contextPath}/auth/logout" class="nav-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="page-wrapper">

        <!-- Page Header -->
        <div class="page-header">
            <div>
                <h1 class="page-title">${not empty item ? 'Edit Item' : 'Post an Item'}</h1>
                <p class="page-subtitle">
                    <c:choose>
                        <c:when test="${not empty item}">Update your listing details below</c:when>
                        <c:otherwise>Fill in the details to list your item for sale</c:otherwise>
                    </c:choose>
                </p>
            </div>
            <a href="${pageContext.request.contextPath}/user/items?action=my-listings" class="btn btn-ghost">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
                Back to Listings
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

        <!-- Form Card -->
        <div class="form-page-layout">
            <div class="form-card">
                <form method="post" action="${pageContext.request.contextPath}/user/items" class="item-form" enctype="multipart/form-data">

                    <c:choose>
                        <c:when test="${not empty item}">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="itemId" value="${item.itemId}">
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="action" value="add">
                        </c:otherwise>
                    </c:choose>

                    <!-- Title -->
                    <div class="form-group">
                        <label for="title" class="form-label">
                            Item Title <span class="required">*</span>
                        </label>
                        <input
                            type="text"
                            id="title"
                            name="title"
                            class="form-input"
                            placeholder="e.g. Calculus Textbook 3rd Edition"
                            required
                            maxlength="150"
                            value="${not empty item ? item.title : ''}"
                        >
                        <div class="form-hint">Be specific and descriptive (max 150 characters)</div>
                    </div>

                    <!-- Category & Condition Row -->
                    <div class="form-row-2">
                        <div class="form-group">
                            <label for="categoryId" class="form-label">
                                Category <span class="required">*</span>
                            </label>
                            <select id="categoryId" name="categoryId" class="form-select" required>
                                <option value="">Select Category</option>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}"
                                        ${not empty item and item.categoryId == cat.categoryId ? 'selected' : ''}>
                                        ${cat.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="itemCondition" class="form-label">
                                Condition <span class="required">*</span>
                            </label>
                            <select id="itemCondition" name="itemCondition" class="form-select" required>
                                <option value="">Select Condition</option>
                                <option value="NEW"      ${not empty item and item.itemCondition == 'NEW'      ? 'selected' : ''}>New</option>
                                <option value="LIKE_NEW" ${not empty item and item.itemCondition == 'LIKE_NEW' ? 'selected' : ''}>Like New</option>
                                <option value="GOOD"     ${not empty item and item.itemCondition == 'GOOD'     ? 'selected' : ''}>Good</option>
                                <option value="USED"     ${not empty item and item.itemCondition == 'USED'     ? 'selected' : ''}>Used</option>
                            </select>
                        </div>
                    </div>

                    <!-- Description -->
                    <div class="form-group">
                        <label for="description" class="form-label">
                            Description <span class="required">*</span>
                        </label>
                        <textarea
                            id="description"
                            name="description"
                            class="form-textarea"
                            rows="5"
                            placeholder="Describe your item: what it is, its condition, why you're selling it, any defects..."
                            required
                            minlength="20"
                        >${not empty item ? item.description : ''}</textarea>
                        <div class="form-hint">Minimum 20 characters. More detail = faster sale!</div>
                    </div>

                    <!-- Price & Image Row -->
                    <div class="form-row-2">
                        <div class="form-group">
                            <label for="price" class="form-label">
                                Price (Rs.) <span class="required">*</span>
                            </label>
                            <div class="input-prefix-wrapper">
                                <span class="input-prefix">Rs.</span>
                                <input
                                    type="number"
                                    id="price"
                                    name="price"
                                    class="form-input with-prefix"
                                    placeholder="0.00"
                                    required
                                    min="0"
                                    step="0.01"
                                    value="${not empty item ? item.price : ''}"
                                >
                            </div>
                            <div class="form-hint">Enter 0 for free items</div>
                        </div>
                        <div class="form-group">
                            <label for="itemImage" class="form-label">
                                Item Image
                            </label>
                            <%-- Show existing image preview when editing --%>
                            <c:if test="${not empty item and not empty item.imagePath}">
                                <div class="upload-preview-wrapper">
                                    <img src="${pageContext.request.contextPath}/assets/uploads/${item.imagePath}"
                                         alt="Current image" class="upload-preview-img">
                                    <span class="upload-preview-label">Current image</span>
                                </div>
                            </c:if>
                            <input
                                type="file"
                                id="itemImage"
                                name="itemImage"
                                class="form-input-file"
                                accept="image/png,image/jpeg,image/webp"
                            >
                            <div class="form-hint">
                                JPG, PNG or WebP, max 5 MB.
                                <c:if test="${not empty item and not empty item.imagePath}">
                                    Leave blank to keep the current image.
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <!-- Info Notice -->
                    <div class="form-notice">
                        <span class="notice-icon"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg></span>
                        <span>Your listing will be reviewed by an admin before it appears publicly. This usually takes a few hours.</span>
                    </div>

                    <!-- Submit -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <c:choose>
                                <c:when test="${not empty item}">Update Listing</c:when>
                                <c:otherwise>Submit Listing</c:otherwise>
                            </c:choose>
                        </button>
                        <a href="${pageContext.request.contextPath}/user/items?action=my-listings" class="btn btn-ghost btn-lg">
                            Cancel
                        </a>
                    </div>

                </form>
            </div>

            <!-- Sidebar Tips -->
            <div class="form-sidebar">
                <div class="tips-card">
                    <h3 class="tips-title">Listing Tips</h3>
                    <ul class="tips-list">
                        <li>Add a clear, specific title</li>
                        <li>Be honest about condition</li>
                        <li>Price fairly - check similar listings</li>
                        <li>Include key details in description</li>
                        <li>Mention any defects upfront</li>
                    </ul>
                </div>
                <div class="tips-card">
                    <h3 class="tips-title">Approval Process</h3>
                    <p class="tips-text">All new listings are reviewed within 24 hours. You'll see your item's status in <strong>My Listings</strong>.</p>
                </div>
            </div>
        </div>

    </div>

</body>
</html>

