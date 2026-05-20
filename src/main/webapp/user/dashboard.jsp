<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.css">
</head>
<body class="user-page">
<nav class="user-nav" id="userNav">
    <div class="nav-container">
        <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-logo">
            <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg"
                 class="brand-logo-img" alt="UniTrade" onerror="this.style.display='none'">
        </a>
        <button class="nav-toggle" id="navToggle" aria-label="Toggle navigation">
            <span></span><span></span><span></span>
        </button>
        <div class="nav-links" id="navLinks">
            <a href="${pageContext.request.contextPath}/user/items"    class="nav-link">Browse</a>
            <a href="${pageContext.request.contextPath}/user/services" class="nav-link">Services</a>
            <a href="${pageContext.request.contextPath}/user/requests" class="nav-link">Help</a>
            <a href="${pageContext.request.contextPath}/user/wishlist" class="nav-link">Wishlist</a>
        </div>
        <div class="nav-right">
            <a href="${pageContext.request.contextPath}/user/items?action=add" class="btn-post">
                <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                Post Item
            </a>
            <a href="${pageContext.request.contextPath}/user/profile" class="nav-avatar" title="${sessionScope.loggedInUser.fullName}">
                ${sessionScope.loggedInUser.fullName.substring(0,1)}
            </a>
            <a href="${pageContext.request.contextPath}/auth/logout" class="nav-logout">Sign out</a>
        </div>
    </div>
</nav>
<div class="page-wrapper">
    <c:if test="${not empty sessionScope.success}">
        <div class="notice notice-success">
            <span class="notice-icon"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg></span>
            <span>${sessionScope.success}</span>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="notice notice-error">
            <span class="notice-icon"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg></span>
            <span>${sessionScope.error}</span>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>
    <!-- HERO -->
    <div class="dash-hero">
        <div class="dash-hero-left">
            <div class="dash-pill-row">
                <c:choose>
                    <c:when test="${sessionScope.loggedInUser.approvalStatus == 'APPROVED'}">
                        <span class="dash-pill dash-pill--green">
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                            Verified Account
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="dash-pill dash-pill--amber">
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                            Pending Approval
                        </span>
                    </c:otherwise>
                </c:choose>
                <span class="dash-live-date" id="dashDate"></span>
            </div>
            <h1 class="dash-greeting">Hello, <span class="dash-name">${sessionScope.loggedInUser.fullName}</span></h1>
            <p class="dash-info">
                <span>${sessionScope.loggedInUser.collegeName}</span>
                <span class="info-sep">&middot;</span>
                <span>${sessionScope.loggedInUser.courseName}</span>
                <span class="info-sep">&middot;</span>
                <span>${sessionScope.loggedInUser.academicYear}</span>
            </p>
            <div class="dash-cta-row">
                <a href="${pageContext.request.contextPath}/user/items" class="btn btn-primary">Browse Marketplace</a>
                <a href="${pageContext.request.contextPath}/user/items?action=add" class="btn-ghost">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                    Post an Item
                </a>
            </div>
        </div>
        <div class="dash-hero-right">
            <div class="dash-snapshot">
                <p class="snapshot-eyebrow">At a glance</p>
                <div class="snapshot-grid">
                    <div class="snapshot-cell">
                        <span class="snapshot-num">${userItemsCount}</span>
                        <span class="snapshot-lbl">Listings</span>
                    </div>
                    <div class="snapshot-cell snapshot-cell--accent">
                        <span class="snapshot-num">${approvedItemsCount}</span>
                        <span class="snapshot-lbl">Active</span>
                    </div>
                    <div class="snapshot-cell">
                        <span class="snapshot-num">${pendingItemsCount}</span>
                        <span class="snapshot-lbl">Pending</span>
                    </div>
                    <div class="snapshot-cell">
                        <span class="snapshot-num">${wishlistCount}</span>
                        <span class="snapshot-lbl">Wishlist</span>
                    </div>
                    <div class="snapshot-cell">
                        <span class="snapshot-num">${userServicesCount}</span>
                        <span class="snapshot-lbl">Services</span>
                    </div>
                    <div class="snapshot-cell">
                        <span class="snapshot-num">${userRequestsCount}</span>
                        <span class="snapshot-lbl">Requests</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- MARKETPLACE SECTION -->
    <div class="dash-section">
        <div class="dash-section-head">
            <div class="dash-section-meta">
                <h2 class="dash-section-title">Marketplace</h2>
                <p class="dash-section-desc">Buy, sell and manage your item listings</p>
            </div>
            <a href="${pageContext.request.contextPath}/user/items" class="dash-more-link">
                View all <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg>
            </a>
        </div>
        <div class="acard-grid">
            <a href="${pageContext.request.contextPath}/user/items?action=add" class="acard acard--ink">
                <div class="acard-icon-box acard-icon-box--light">
                    <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                </div>
                <div class="acard-body"><h3 class="acard-title">Post an Item</h3><p class="acard-desc">List something for sale</p></div>
                <span class="acard-chevron"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg></span>
            </a>
            <a href="${pageContext.request.contextPath}/user/items" class="acard">
                <div class="acard-icon-box acard-icon-box--teal">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6"/></svg>
                </div>
                <div class="acard-body"><h3 class="acard-title">Browse Items</h3><p class="acard-desc">Explore all listings</p></div>
                <span class="acard-chevron"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg></span>
            </a>
            <a href="${pageContext.request.contextPath}/user/items?action=my-listings" class="acard">
                <div class="acard-icon-box acard-icon-box--slate">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>
                </div>
                <div class="acard-body"><h3 class="acard-title">My Listings</h3><p class="acard-desc">Manage your postings</p></div>
                <span class="acard-chevron"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg></span>
            </a>
            <a href="${pageContext.request.contextPath}/user/orders" class="acard">
                <div class="acard-icon-box acard-icon-box--amber">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg>
                </div>
                <div class="acard-body"><h3 class="acard-title">My Orders</h3><p class="acard-desc">Track purchases &amp; sales</p></div>
                <span class="acard-chevron"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg></span>
            </a>
        </div>
    </div>
    <!-- SERVICES SECTION -->
    <div class="dash-section">
        <div class="dash-section-head">
            <div class="dash-section-meta">
                <h2 class="dash-section-title">Services &amp; Community</h2>
                <p class="dash-section-desc">Share your skills and support fellow students</p>
            </div>
            <a href="${pageContext.request.contextPath}/user/services" class="dash-more-link">
                View all <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg>
            </a>
        </div>
        <div class="acard-grid">
            <a href="${pageContext.request.contextPath}/user/services?action=add" class="acard acard--teal">
                <div class="acard-icon-box acard-icon-box--light">
                    <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg>
                </div>
                <div class="acard-body"><h3 class="acard-title">Offer a Service</h3><p class="acard-desc">Share your skills</p></div>
                <span class="acard-chevron"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg></span>
            </a>
            <a href="${pageContext.request.contextPath}/user/services" class="acard">
                <div class="acard-icon-box acard-icon-box--teal">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                </div>
                <div class="acard-body"><h3 class="acard-title">Browse Services</h3><p class="acard-desc">Find peer services</p></div>
                <span class="acard-chevron"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg></span>
            </a>
            <a href="${pageContext.request.contextPath}/user/requests" class="acard">
                <div class="acard-icon-box acard-icon-box--orange">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/></svg>
                </div>
                <div class="acard-body"><h3 class="acard-title">Help Requests</h3><p class="acard-desc">Ask for or offer help</p></div>
                <span class="acard-chevron"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg></span>
            </a>
            <a href="${pageContext.request.contextPath}/user/profile" class="acard">
                <div class="acard-icon-box acard-icon-box--slate">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                </div>
                <div class="acard-body"><h3 class="acard-title">My Profile</h3><p class="acard-desc">Edit your information</p></div>
                <span class="acard-chevron"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></svg></span>
            </a>
        </div>
    </div>
    <c:if test="${sessionScope.loggedInUser.approvalStatus == 'APPROVED'}">
        <div class="notice notice-success" style="margin-bottom:2rem;">
            <span class="notice-icon"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg></span>
            <span>Your account is verified and active. You can trade freely!</span>
        </div>
    </c:if>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
<script>
(function(){
    var nav=document.getElementById('userNav');
    if(nav){window.addEventListener('scroll',function(){nav.classList.toggle('nav--elevated',window.scrollY>8);},{passive:true});}
    var toggle=document.getElementById('navToggle'),links=document.getElementById('navLinks');
    if(toggle&&links){toggle.addEventListener('click',function(){links.classList.toggle('nav-open');toggle.classList.toggle('toggle--active');});}
    var dateEl=document.getElementById('dashDate');
    if(dateEl){var d=new Date();dateEl.textContent=d.toLocaleDateString('en-US',{weekday:'short',month:'short',day:'numeric'});}
}());
</script>
</body>
</html>