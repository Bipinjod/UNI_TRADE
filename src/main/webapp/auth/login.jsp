<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In - UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Poppins:wght@600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body class="auth-body">

<div class="auth-split">

    <!-- LEFT PANEL -->
    <div class="auth-brand-panel">
        <div class="auth-brand-inner">

            <a href="${pageContext.request.contextPath}/" class="auth-logo">
                <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo-light.svg" alt="UniTrade logo" class="brand-logo-img">
            </a>

            <div class="auth-brand-copy">
                <h1>Your campus marketplace.</h1>
                <p>Buy, sell, and trade with verified students across Nepal. Safe, simple, and built for campus life.</p>
            </div>

            <ul class="auth-checklist">
                <li>
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                    <div>
                        <strong>Verified students only</strong>
                        <span>Every account is reviewed before activation</span>
                    </div>
                </li>
                <li>
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                    <div>
                        <strong>List in under 2 minutes</strong>
                        <span>Post items or services quickly and easily</span>
                    </div>
                </li>
                <li>
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                    <div>
                        <strong>Campus community</strong>
                        <span>Trade within your own college network</span>
                    </div>
                </li>
            </ul>

            <div class="auth-social-proof">
                <div class="auth-avatars">
                    <span class="auth-avatar">R</span>
                    <span class="auth-avatar">A</span>
                    <span class="auth-avatar">P</span>
                    <span class="auth-avatar">S</span>
                </div>
                <span class="auth-social-text"><strong>500+</strong> students already on UniTrade</span>
            </div>

        </div>
    </div>

    <!-- RIGHT PANEL -->
    <div class="auth-form-panel">
        <div class="auth-form-box">

            <a href="${pageContext.request.contextPath}/" class="auth-back-link">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
                Back to home
            </a>

            <div class="auth-form-header">
                <h2>Welcome back</h2>
                <p>Sign in to your UniTrade account</p>
            </div>

            <c:if test="${not empty error}">
                <div class="auth-msg auth-msg-error">
                    <strong>Sign in failed</strong>
                    <span>${error}</span>
                </div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="auth-msg auth-msg-success">
                    <strong>Success</strong>
                    <span>${success}</span>
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.success}">
                <div class="auth-msg auth-msg-success">
                    <strong>Success</strong>
                    <span>${sessionScope.success}</span>
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/auth/login" class="auth-form" autocomplete="on">

                <div class="field">
                    <label for="email">Email address</label>
                    <input type="email" id="email" name="email" placeholder="you@college.edu.np" required autocomplete="email" value="${param.email}">
                </div>

                <div class="field">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="Enter your password" required autocomplete="current-password">
                </div>

                <div class="auth-row">
                    <label class="auth-check">
                        <input type="checkbox" name="rememberMe">
                        <span>Remember me</span>
                    </label>
                    <a href="#" class="auth-link-sm">Forgot password?</a>
                </div>

                <button type="submit" class="auth-btn">Sign in</button>

            </form>

            <div class="auth-alt">
                Don't have an account? <a href="${pageContext.request.contextPath}/auth/register">Create account</a>
            </div>

        </div>
    </div>

</div>

</body>
</html>
