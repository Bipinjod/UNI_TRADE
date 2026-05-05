<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - UniTrade</title>
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
                <h1>Join the student marketplace.</h1>
                <p>Create your free account and start trading with verified students across Nepal.</p>
            </div>

            <div class="auth-stats">
                <div class="auth-stat">
                    <span class="auth-stat-num">500+</span>
                    <span class="auth-stat-label">Active students</span>
                </div>
                <div class="auth-stat">
                    <span class="auth-stat-num">1,200+</span>
                    <span class="auth-stat-label">Items listed</span>
                </div>
                <div class="auth-stat">
                    <span class="auth-stat-num">30+</span>
                    <span class="auth-stat-label">Colleges</span>
                </div>
            </div>

            <ul class="auth-checklist">
                <li>
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                    <div>
                        <strong>Free to use</strong>
                        <span>No fees, no commissions</span>
                    </div>
                </li>
                <li>
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                    <div>
                        <strong>Admin verified</strong>
                        <span>All accounts manually approved for safety</span>
                    </div>
                </li>
                <li>
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                    <div>
                        <strong>Items, services, and help requests</strong>
                        <span>Everything campus life needs</span>
                    </div>
                </li>
            </ul>

            <div class="auth-social-proof">
                <div class="auth-avatars">
                    <span class="auth-avatar">R</span>
                    <span class="auth-avatar">A</span>
                    <span class="auth-avatar">P</span>
                </div>
                <span class="auth-social-text">Trusted by students at <strong>TU, KU, PU</strong> and more</span>
            </div>

        </div>
    </div>

    <!-- RIGHT PANEL -->
    <div class="auth-form-panel">
        <div class="auth-form-box auth-form-box--register">

            <a href="${pageContext.request.contextPath}/" class="auth-back-link">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
                Back to home
            </a>

            <div class="auth-form-header">
                <h2>Create your account</h2>
                <p>Fill in your details to get started on UniTrade</p>
            </div>

            <!-- Error alert -->
            <c:if test="${not empty error}">
                <div class="auth-msg auth-msg-error">
                    <strong>Registration failed</strong>
                    <span>${error}</span>
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.error}">
                <div class="auth-msg auth-msg-error">
                    <strong>Error</strong>
                    <span>${sessionScope.error}</span>
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="auth-msg auth-msg-success">
                    <strong>Success</strong>
                    <span>${successMessage}</span>
                </div>
            </c:if>

            <!-- Register form -->
            <form method="post" action="${pageContext.request.contextPath}/auth/register" class="auth-form" autocomplete="on">

                <!-- Full Name -->
                <div class="field">
                    <label for="fullName">Full name</label>
                    <input type="text" id="fullName" name="fullName" placeholder="e.g. Aarav Sharma" required minlength="3" value="${param.fullName}">
                </div>

                <!-- Email & Phone -->
                <div class="field-row">
                    <div class="field">
                        <label for="email">Email address</label>
                        <input type="email" id="email" name="email" placeholder="you@college.edu.np" required value="${param.email}">
                    </div>
                    <div class="field">
                        <label for="phone">Phone number</label>
                        <input type="tel" id="phone" name="phone" placeholder="98XXXXXXXX" required pattern="[0-9]{10}" title="Enter a 10-digit phone number" value="${param.phone}">
                    </div>
                </div>

                <!-- College -->
                <div class="field">
                    <label for="collegeName">College / University</label>
                    <input type="text" id="collegeName" name="collegeName" placeholder="e.g. Tribhuvan University" required value="${param.collegeName}">
                </div>

                <!-- Course & Academic Year -->
                <div class="field-row">
                    <div class="field">
                        <label for="courseName">Course / Program</label>
                        <input type="text" id="courseName" name="courseName" placeholder="e.g. BCA, BIT, BSc CSIT" required value="${param.courseName}">
                    </div>
                    <div class="field">
                        <label for="academicYear">Academic year</label>
                        <input type="text" id="academicYear" name="academicYear" placeholder="e.g. 2024-2025" required value="${param.academicYear}">
                    </div>
                </div>

                <!-- Password & Confirm -->
                <div class="field-row">
                    <div class="field">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Min. 6 characters" required minlength="6">
                        <span class="field-hint">At least 6 characters</span>
                    </div>
                    <div class="field">
                        <label for="confirmPassword">Confirm password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Repeat password" required minlength="6">
                    </div>
                </div>

                <!-- Terms -->
                <div class="auth-terms">
                    <input type="checkbox" id="terms" name="terms" required>
                    <label for="terms">I agree to the <a href="#">Terms of Service</a> and <a href="#">Privacy Policy</a></label>
                </div>

                <!-- Submit -->
                <button type="submit" class="auth-btn">Create account</button>

            </form>

            <!-- Login link -->
            <div class="auth-alt">
                Already have an account? <a href="${pageContext.request.contextPath}/auth/login">Sign in</a>
            </div>

        </div>
    </div>

</div>

</body>
</html>
