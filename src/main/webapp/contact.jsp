<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact — UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,400;0,500;0,600;0,700;0,800;1,500&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        html { scroll-behavior: smooth; }
        body {
            font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;
            background: #FAFAFA; color: #1a1a2e;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }
        a { text-decoration: none; color: inherit; }
        ::selection { background: rgba(233,69,96,0.15); }
        :root {
            --navy: #1a1a2e; --accent: #e94560; --bg: #FAFAFA;
            --gray-500: #6b7280; --gray-400: #9ca3af; --gray-200: #e5e7eb;
            --border: #e2e4ea; --white: #ffffff;
        }

        /* ─── NAV ─── */
        .nav {
            position: fixed; top: 0; left: 0; right: 0; z-index: 100;
            border-bottom: 1px solid var(--border);
            backdrop-filter: blur(8px); background: rgba(255,255,255,0.92);
        }
        .nav-inner {
            max-width: 1100px; margin: 0 auto; padding: 0 2rem;
            height: 56px; display: flex; align-items: center; justify-content: space-between;
        }
        .nav-left { display: flex; align-items: center; gap: 2.75rem; }
        .nav-brand {
            font-size: 1.05rem; font-weight: 800; color: var(--navy);
            display: flex; align-items: center; gap: 0.4rem; letter-spacing: -0.4px;
        }
        .nav-brand .dot { width: 7px; height: 7px; border-radius: 50%; background: var(--accent); }
        .nav-links { display: flex; gap: 1.5rem; }
        .nav-links a { font-size: 0.8125rem; font-weight: 500; color: var(--gray-500); transition: color 0.15s; position: relative; }
        .nav-links a:hover { color: var(--navy); }
        .nav-links a.active { color: var(--navy); }
        .nav-links a.active::after {
            content: ''; position: absolute; bottom: -18px; left: 0; right: 0;
            height: 1.5px; background: var(--navy);
        }
        .nav-right { display: flex; align-items: center; gap: 1rem; }
        .nav-signin { font-size: 0.8125rem; font-weight: 600; color: var(--navy); transition: color 0.15s; }
        .nav-signin:hover { color: var(--accent); }
        .nav-cta {
            padding: 0.4rem 0.95rem; background: var(--navy); color: var(--white);
            font-size: 0.78rem; font-weight: 600; border-radius: 6px; transition: background 0.2s;
        }
        .nav-cta:hover { background: var(--accent); }

        /* ─── PAGE HEADER ─── */
        .page-header {
            max-width: 1100px; margin: 0 auto; padding: 0 2rem;
            padding-top: 120px; padding-bottom: 3rem;
        }
        .page-label {
            font-size: 0.7rem; font-weight: 700; text-transform: uppercase;
            letter-spacing: 1.5px; color: var(--accent); margin-bottom: 0.5rem;
        }
        .page-h1 {
            font-size: clamp(1.75rem, 3.5vw, 2.5rem);
            font-weight: 800; color: var(--navy); letter-spacing: -1px;
            margin-bottom: 0.75rem; line-height: 1.15;
        }
        .page-lead {
            font-size: 1rem; color: var(--gray-500); line-height: 1.65;
            max-width: 480px;
        }

        /* ─── CONTACT LAYOUT ─── */
        .contact-wrap {
            max-width: 1100px; margin: 0 auto; padding: 0 2rem 4rem;
            display: grid; grid-template-columns: 1.2fr 0.8fr; gap: 3rem;
            align-items: start;
        }

        /* ─── FORM ─── */
        .contact-form {
            background: var(--white); border: 1px solid var(--border);
            border-radius: 10px; padding: 2rem;
        }
        .form-title {
            font-size: 1rem; font-weight: 700; color: var(--navy);
            margin-bottom: 1.5rem; letter-spacing: -0.2px;
        }
        .form-row {
            display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;
            margin-bottom: 1rem;
        }
        .form-group { margin-bottom: 1rem; }
        .form-label {
            display: block; font-size: 0.78rem; font-weight: 600;
            color: var(--navy); margin-bottom: 0.35rem;
        }
        .form-input, .form-textarea, .form-select {
            width: 100%; padding: 0.6rem 0.85rem;
            border: 1.5px solid var(--border); border-radius: 7px;
            font-size: 0.84rem; font-family: inherit; color: var(--navy);
            background: var(--bg); transition: border-color 0.15s;
        }
        .form-input:focus, .form-textarea:focus, .form-select:focus {
            outline: none; border-color: var(--navy);
        }
        .form-input::placeholder, .form-textarea::placeholder { color: var(--gray-400); }
        .form-textarea { resize: vertical; min-height: 100px; }
        .form-select { appearance: none; cursor: pointer; }
        .btn-submit {
            display: inline-flex; align-items: center; gap: 0.35rem;
            padding: 0.65rem 1.5rem; background: var(--navy); color: var(--white);
            font-size: 0.84rem; font-weight: 600; border-radius: 7px;
            border: none; cursor: pointer; font-family: inherit;
            transition: background 0.2s;
        }
        .btn-submit:hover { background: var(--accent); }

        /* ─── INFO CARDS ─── */
        .contact-info { display: flex; flex-direction: column; gap: 1rem; }
        .info-card {
            padding: 1.25rem; border: 1px solid var(--border);
            border-radius: 10px; background: var(--white);
        }
        .info-card-header {
            display: flex; align-items: center; gap: 0.6rem;
            margin-bottom: 0.5rem;
        }
        .info-icon {
            width: 32px; height: 32px; border-radius: 7px;
            display: flex; align-items: center; justify-content: center;
        }
        .ii-1 { background: rgba(233,69,96,0.08); color: var(--accent); }
        .ii-2 { background: rgba(99,102,241,0.08); color: #6366f1; }
        .ii-3 { background: rgba(22,163,74,0.08); color: #16a34a; }
        .info-card h4 {
            font-size: 0.84rem; font-weight: 700; color: var(--navy);
        }
        .info-card p {
            font-size: 0.8rem; color: var(--gray-500); line-height: 1.55;
        }
        .info-card a {
            color: var(--accent); font-weight: 600; transition: opacity 0.15s;
        }
        .info-card a:hover { opacity: 0.8; }

        /* ─── DIVIDER ─── */
        .section-line { max-width: 1100px; margin: 0 auto; padding: 0 2rem; }
        .section-line hr { border: none; height: 1px; background: var(--border); }

        /* ─── FAQ ─── */
        .faq {
            max-width: 1100px; margin: 0 auto;
            padding: 4rem 2rem;
        }
        .faq-label {
            font-size: 0.7rem; font-weight: 700; text-transform: uppercase;
            letter-spacing: 1.5px; color: var(--accent); margin-bottom: 0.45rem;
        }
        .faq-h2 {
            font-size: 1.55rem; font-weight: 800; color: var(--navy);
            letter-spacing: -0.5px; margin-bottom: 2rem;
        }
        .faq-grid {
            display: grid; grid-template-columns: 1fr 1fr; gap: 1.25rem;
        }
        .faq-item {
            padding: 1.25rem; border: 1px solid var(--border);
            border-radius: 10px; background: var(--white);
        }
        .faq-item h4 {
            font-size: 0.84rem; font-weight: 700; color: var(--navy);
            margin-bottom: 0.35rem; letter-spacing: -0.1px;
        }
        .faq-item p {
            font-size: 0.78rem; color: var(--gray-500); line-height: 1.55;
        }

        /* ─── FOOTER ─── */
        .foot {
            border-top: 1px solid var(--border); background: var(--white);
            padding: 1.25rem 2rem;
        }
        .foot-inner {
            max-width: 1100px; margin: 0 auto;
            display: flex; align-items: center; justify-content: space-between;
            font-size: 0.72rem; color: var(--gray-400);
        }
        .foot-links { display: flex; gap: 1.25rem; }
        .foot-links a { font-size: 0.72rem; color: var(--gray-500); font-weight: 500; transition: color 0.15s; }
        .foot-links a:hover { color: var(--navy); }

        /* ─── MOBILE NAV TOGGLE ─── */
        .mobile-nav-toggle {
            display: none; flex-direction: column; gap: 5px;
            background: none; border: none; cursor: pointer; padding: 6px;
            border-radius: 6px; transition: background 0.2s;
        }
        .mobile-nav-toggle:hover { background: rgba(0,0,0,0.04); }
        .mobile-nav-toggle span {
            display: block; width: 22px; height: 2px;
            background: var(--navy); border-radius: 2px;
            transition: transform 0.25s, opacity 0.25s;
        }
        .mobile-nav-toggle.active span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
        .mobile-nav-toggle.active span:nth-child(2) { opacity: 0; }
        .mobile-nav-toggle.active span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

        /* ─── RESPONSIVE ─── */
        @media (max-width: 992px) {
            .contact-wrap { grid-template-columns: 1fr; gap: 2rem; }
            .faq-grid { grid-template-columns: 1fr; }
            .nav-links { display: none; }
            .nav-links.nav-open {
                display: flex; flex-direction: column;
                position: absolute; top: 56px; left: 0; right: 0;
                background: rgba(255,255,255,0.98);
                padding: 0.75rem 2rem 1rem;
                border-bottom: 1px solid var(--border);
                box-shadow: 0 4px 12px rgba(26,26,46,0.06);
                gap: 0.5rem; z-index: 99;
            }
            .mobile-nav-toggle { display: flex; }
        }
        @media (max-width: 768px) {
            .nav-inner { padding: 0 1.25rem; }
            .page-header { padding: 5rem 1.25rem 2rem; }
            .page-h1 { font-size: 1.75rem; }
            .contact, .faq { padding-left: 1.25rem; padding-right: 1.25rem; }
        }
        @media (max-width: 576px) {
            .page-h1 { font-size: 1.5rem; }
            .form-row { grid-template-columns: 1fr; }
            .foot-inner { flex-direction: column; gap: 0.5rem; }
            .nav-inner { height: 50px; }
            .nav-brand { font-size: 0.95rem; }
            .nav-cta { padding: 0.35rem 0.75rem; font-size: 0.72rem; }
            .page-header { padding: 4.5rem 1rem 1.5rem; }
            .page-lead { font-size: 0.84rem; }
            .contact, .faq { padding: 2rem 1rem; }
        }
    </style>
</head>
<body>

    <!-- NAV -->
    <nav class="nav">
        <div class="nav-inner">
            <div class="nav-left">
                <a href="${pageContext.request.contextPath}/" class="nav-logo">
                    <img src="${pageContext.request.contextPath}/assets/images/unitrade-logo.svg" alt="UniTrade logo" class="brand-logo-img">
                    UniTrade
                </a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Home</a>
                    <a href="${pageContext.request.contextPath}/user/browse">Browse</a>
                    <a href="${pageContext.request.contextPath}/about.jsp">About</a>
                    <a href="${pageContext.request.contextPath}/contact.jsp" class="active">Contact</a>
                </div>
            </div>
            <div class="nav-right">
                <a href="${pageContext.request.contextPath}/auth/login" class="nav-signin">Sign In</a>
                <a href="${pageContext.request.contextPath}/auth/register" class="nav-cta">Post Listing</a>
                <button class="mobile-nav-toggle" aria-label="Toggle navigation" onclick="this.classList.toggle('active');this.closest('.nav-inner').querySelector('.nav-links').classList.toggle('nav-open');">
                    <span></span><span></span><span></span>
                </button>
            </div>
        </div>
    </nav>

    <!-- PAGE HEADER -->
    <header class="page-header">
        <h1 class="page-h1">Get in touch</h1>
        <p class="page-lead">
            Have a question, found a bug, or just want to say hi? We'd love to hear from you.
            Fill out the form or use one of the channels below.
        </p>
    </header>

    <!-- CONTACT CONTENT -->
    <section class="contact-wrap">

        <!-- FORM -->
        <div class="contact-form">
            <div class="form-title">Send us a message</div>
            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Full Name</label>
                    <input type="text" class="form-input" placeholder="Your name">
                </div>
                <div class="form-group">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-input" placeholder="you@college.edu.np">
                </div>
            </div>
            <div class="form-group">
                <label class="form-label">Subject</label>
                <select class="form-select">
                    <option value="">Select a topic</option>
                    <option value="account">Account Issue</option>
                    <option value="listing">Listing Problem</option>
                    <option value="report">Report a User</option>
                    <option value="feedback">General Feedback</option>
                    <option value="other">Other</option>
                </select>
            </div>
            <div class="form-group">
                <label class="form-label">Message</label>
                <textarea class="form-textarea" placeholder="Describe your issue or question..."></textarea>
            </div>
            <button type="button" class="btn-submit">Send Message &rarr;</button>
        </div>

        <!-- INFO CARDS -->
        <div class="contact-info">
            <div class="info-card">
                <div class="info-card-header">
                    <div class="info-icon ii-1">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
                    </div>
                    <h4>Email us</h4>
                </div>
                <p>For general enquiries or support requests, email us and we'll respond within 24 hours.</p>
                <p style="margin-top:0.5rem;"><a href="mailto:support@unitrade.edu.np">support@unitrade.edu.np</a></p>
            </div>
            <div class="info-card">
                <div class="info-card-header">
                    <div class="info-icon ii-2">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
                    </div>
                    <h4>Account issues</h4>
                </div>
                <p>If your account is pending approval or has been blocked, write to us with your registered email and we'll look into it.</p>
            </div>
            <div class="info-card">
                <div class="info-card-header">
                    <div class="info-icon ii-3">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>
                    </div>
                    <h4>Report a problem</h4>
                </div>
                <p>Found a suspicious listing or user? Report it to <a href="mailto:report@unitrade.edu.np">report@unitrade.edu.np</a> and we'll take action.</p>
            </div>
        </div>
    </section>

    <div class="section-line"><hr></div>

    <!-- FAQ -->
    <section class="faq">
        <div class="faq-label">Frequently asked</div>
        <h2 class="faq-h2">Common questions</h2>
        <div class="faq-grid">
            <div class="faq-item">
                <h4>How long does account approval take?</h4>
                <p>Most accounts are reviewed and approved within 24 hours on business days. You'll be able to sign in once approved.</p>
            </div>
            <div class="faq-item">
                <h4>Is UniTrade free to use?</h4>
                <p>Yes, completely free. There are no listing fees, transaction fees, or hidden charges. Just sign up and start trading.</p>
            </div>
            <div class="faq-item">
                <h4>Can I sell services, not just items?</h4>
                <p>Currently UniTrade supports physical items only. Peer services (tutoring, design, etc.) may be added in the future.</p>
            </div>
            <div class="faq-item">
                <h4>How do I meet the buyer/seller?</h4>
                <p>Once you place or receive an order, coordinate via the platform message or your college email to meet on campus.</p>
            </div>
            <div class="faq-item">
                <h4>What if I have a problem with a listing?</h4>
                <p>You can report any listing or user. Our admin team reviews reports and takes appropriate action promptly.</p>
            </div>
            <div class="faq-item">
                <h4>Can I edit or delete my listing?</h4>
                <p>Yes. Go to "My Listings" in your dashboard. You can edit details, update price, or remove a listing anytime.</p>
            </div>
        </div>
    </section>

    <!-- FOOTER -->
    <footer class="foot">
        <div class="foot-inner">
            <span>&copy; 2026 UniTrade. All rights reserved.</span>
            <div class="foot-links">
                <a href="${pageContext.request.contextPath}/">Home</a>
                <a href="${pageContext.request.contextPath}/about.jsp">About</a>
                <a href="${pageContext.request.contextPath}/auth/login">Sign In</a>
            </div>
        </div>
    </footer>

</body>
</html>

