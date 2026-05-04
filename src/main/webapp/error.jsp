<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error — UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;
            background: #FAFAFA; color: #1a1a2e;
            -webkit-font-smoothing: antialiased;
            min-height: 100vh; display: flex; flex-direction: column;
            align-items: center; justify-content: center; padding: 2rem;
        }
        a { text-decoration: none; color: inherit; }
        ::selection { background: rgba(233,69,96,0.15); }
        :root {
            --navy: #1a1a2e; --accent: #e94560; --bg: #FAFAFA;
            --gray-500: #6b7280; --gray-400: #9ca3af; --gray-200: #e5e7eb;
            --border: #e2e4ea; --white: #ffffff; --danger: #dc3545;
        }

        .error-card {
            background: var(--white); border: 1px solid var(--border);
            border-radius: 10px; padding: 3rem 2.5rem;
            max-width: 480px; width: 100%; text-align: center;
            box-shadow: 0 1px 2px rgba(26,26,46,0.04), 0 8px 24px rgba(26,26,46,0.06);
        }

        /* Logo */
        .err-logo {
            display: inline-flex; align-items: center; gap: 0.4rem;
            margin-bottom: 2.25rem; font-size: 1.05rem; font-weight: 800;
            color: var(--navy); letter-spacing: -0.4px;
        }
        .err-logo .dot {
            width: 7px; height: 7px; border-radius: 50%; background: var(--accent);
        }

        /* Illustration */
        .err-visual {
            width: 72px; height: 72px; border-radius: 50%;
            background: rgba(220,53,69,0.06);
            display: flex; align-items: center; justify-content: center;
            margin: 0 auto 1.5rem;
        }
        .err-visual svg { color: var(--danger); }

        /* Code badge */
        .err-code {
            display: inline-block;
            font-size: 0.68rem; font-weight: 700; text-transform: uppercase;
            letter-spacing: 1.2px; color: var(--danger);
            background: rgba(220,53,69,0.07); padding: 0.3rem 0.8rem;
            border-radius: 5px; margin-bottom: 1rem;
        }

        .err-title {
            font-size: 1.35rem; font-weight: 800; color: var(--navy);
            letter-spacing: -0.5px; margin-bottom: 0.65rem; line-height: 1.2;
        }

        .err-desc {
            font-size: 0.875rem; color: var(--gray-500); line-height: 1.6;
            margin-bottom: 0.5rem;
        }

        /* Custom message box */
        .err-msg-box {
            background: rgba(220,53,69,0.05); border: 1px solid rgba(220,53,69,0.12);
            border-radius: 7px; padding: 0.7rem 1rem; margin: 1rem 0 1.25rem;
            font-size: 0.84rem; color: var(--danger); font-weight: 500;
        }

        /* Actions */
        .err-actions {
            display: flex; gap: 0.6rem; justify-content: center;
            flex-wrap: wrap; margin-top: 1.75rem;
        }
        .btn-dark {
            display: inline-flex; align-items: center; gap: 0.35rem;
            padding: 0.6rem 1.25rem; background: var(--navy); color: var(--white);
            font-size: 0.84rem; font-weight: 600; border-radius: 7px;
            transition: background 0.2s;
        }
        .btn-dark:hover { background: var(--accent); }
        .btn-ghost {
            display: inline-flex; align-items: center; gap: 0.35rem;
            padding: 0.6rem 1.25rem; background: transparent;
            border: 1.5px solid var(--gray-200); color: var(--navy);
            font-size: 0.84rem; font-weight: 600; border-radius: 7px;
            transition: border-color 0.15s;
        }
        .btn-ghost:hover { border-color: var(--navy); }

        /* Divider */
        .err-divider {
            height: 1px; background: var(--border); margin: 1.75rem 0 1.25rem;
        }

        /* Footer note */
        .err-note {
            font-size: 0.75rem; color: var(--gray-400);
        }
        .err-note a {
            color: var(--accent); font-weight: 600; transition: opacity 0.15s;
        }
        .err-note a:hover { opacity: 0.8; }

        @media (max-width: 576px) {
            .error-card { padding: 2rem 1.5rem; max-width: 95vw; }
            .err-title { font-size: 1.15rem; }
            .err-actions { flex-direction: column; }
            .btn-dark, .btn-ghost { width: 100%; justify-content: center; }
            .err-visual { width: 56px; height: 56px; }
        }
    </style>
</head>
<body>

    <div class="error-card">

        <!-- Logo -->
        <a href="${pageContext.request.contextPath}/" class="err-logo">
            <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg" alt="UniTrade logo" class="brand-logo-img">
            UniTrade
        </a>

        <!-- Visual -->
        <div class="err-visual">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <c:choose>
                    <c:when test="${not empty param.message and param.message.contains('denied')}">
                        <circle cx="12" cy="12" r="10"/><line x1="4.93" y1="4.93" x2="19.07" y2="19.07"/>
                    </c:when>
                    <c:when test="${not empty param.message and param.message.contains('found')}">
                        <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
                    </c:when>
                    <c:otherwise>
                        <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/>
                    </c:otherwise>
                </c:choose>
            </svg>
        </div>

        <!-- Code -->
        <div class="err-code">Something went wrong</div>

        <!-- Title -->
        <h1 class="err-title">
            <c:choose>
                <c:when test="${not empty param.message and param.message.contains('denied')}">
                    Access Denied
                </c:when>
                <c:when test="${not empty param.message and param.message.contains('found')}">
                    Page Not Found
                </c:when>
                <c:when test="${not empty param.message and param.message.contains('login')}">
                    Login Required
                </c:when>
                <c:otherwise>
                    Oops, something broke
                </c:otherwise>
            </c:choose>
        </h1>

        <!-- Description -->
        <p class="err-desc">
            <c:choose>
                <c:when test="${not empty param.message and param.message.contains('denied')}">
                    You don't have permission to view this page. Make sure you're signed in with the right account.
                </c:when>
                <c:when test="${not empty param.message and param.message.contains('found')}">
                    The page you're looking for doesn't exist or may have been moved.
                </c:when>
                <c:when test="${not empty param.message and param.message.contains('login')}">
                    You need to sign in to continue. Please log in to your UniTrade account.
                </c:when>
                <c:otherwise>
                    Something unexpected happened on our end. Don't worry &mdash; it's not your fault.
                </c:otherwise>
            </c:choose>
        </p>

        <!-- Custom error message -->
        <c:if test="${not empty param.message}">
            <div class="err-msg-box">${param.message}</div>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="err-msg-box">${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <!-- Actions -->
        <div class="err-actions">
            <a href="${pageContext.request.contextPath}/" class="btn-dark">
                Go to Homepage &rarr;
            </a>
            <c:choose>
                <c:when test="${not empty sessionScope.loggedInUser}">
                    <a href="${pageContext.request.contextPath}/user/dashboard" class="btn-ghost">
                        My Dashboard
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/auth/login" class="btn-ghost">
                        Sign In
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="err-divider"></div>

        <p class="err-note">
            Need help? <a href="${pageContext.request.contextPath}/contact.jsp">Contact Support</a>
            &nbsp;&bull;&nbsp;
            <a href="javascript:history.back()">Go Back</a>
        </p>

    </div>

</body>
</html>

