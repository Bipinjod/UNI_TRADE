<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About — UniTrade</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,400;0,500;0,600;0,700;0,800;1,500&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        html { scroll-behavior: smooth; overflow-x: hidden; }
        body {
            font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;
            background: #FAFAFA; color: #1a1a2e;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
            overflow-x: hidden;
        }
        img, svg, video { max-width: 100%; height: auto; display: block; }
        a { text-decoration: none; color: inherit; }
        /* Nav logo image sizing */
        .nav-logo img, .brand-logo-img {
            height: 36px; width: auto; max-width: 160px;
            object-fit: contain; flex-shrink: 0; display: block;
        }
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
            max-width: 560px;
        }

        /* ─── STORY ─── */
        .story {
            max-width: 1100px; margin: 0 auto; padding: 0 2rem 4rem;
            display: grid; grid-template-columns: 1fr 1fr; gap: 4rem;
            align-items: start;
        }
        .story-text h2 {
            font-size: 1.35rem; font-weight: 800; color: var(--navy);
            letter-spacing: -0.3px; margin-bottom: 1rem;
        }
        .story-text p {
            font-size: 0.9rem; color: var(--gray-500); line-height: 1.7;
            margin-bottom: 1rem;
        }
        .story-visual {
            background: var(--white); border: 1px solid var(--border);
            border-radius: 10px; padding: 2rem; position: relative;
        }
        .story-stat {
            margin-bottom: 1.5rem; padding-bottom: 1.5rem;
            border-bottom: 1px solid var(--border);
        }
        .story-stat:last-child { margin-bottom: 0; padding-bottom: 0; border-bottom: none; }
        .stat-number {
            font-size: 1.75rem; font-weight: 800; color: var(--navy);
            letter-spacing: -1px; margin-bottom: 0.15rem;
        }
        .stat-label { font-size: 0.78rem; color: var(--gray-400); font-weight: 500; }

        /* ─── DIVIDER ─── */
        .section-line { max-width: 1100px; margin: 0 auto; padding: 0 2rem; }
        .section-line hr { border: none; height: 1px; background: var(--border); }

        /* ─── VALUES ─── */
        .values {
            max-width: 1100px; margin: 0 auto;
            padding: 4rem 2rem;
        }
        .val-label {
            font-size: 0.7rem; font-weight: 700; text-transform: uppercase;
            letter-spacing: 1.5px; color: var(--accent); margin-bottom: 0.45rem;
        }
        .val-h2 {
            font-size: 1.55rem; font-weight: 800; color: var(--navy);
            letter-spacing: -0.5px; margin-bottom: 2.5rem;
        }
        .val-grid {
            display: grid; grid-template-columns: repeat(3, 1fr); gap: 1.5rem;
        }
        .val-card {
            padding: 1.5rem; border: 1px solid var(--border);
            border-radius: 10px; background: var(--white);
            transition: box-shadow 0.2s, transform 0.2s;
        }
        .val-card:hover {
            box-shadow: 0 8px 24px rgba(26,26,46,0.05);
            transform: translateY(-2px);
        }
        .val-icon {
            width: 36px; height: 36px; border-radius: 8px;
            display: flex; align-items: center; justify-content: center;
            margin-bottom: 0.85rem;
        }
        .vi-1 { background: rgba(233,69,96,0.08); color: var(--accent); }
        .vi-2 { background: rgba(22,163,74,0.08); color: #16a34a; }
        .vi-3 { background: rgba(99,102,241,0.08); color: #6366f1; }
        .val-card h4 {
            font-size: 0.9rem; font-weight: 700; color: var(--navy);
            margin-bottom: 0.3rem; letter-spacing: -0.1px;
        }
        .val-card p {
            font-size: 0.8rem; color: var(--gray-500); line-height: 1.55;
        }

        /* ─── PLATFORM ─── */
        .platform {
            max-width: 1100px; margin: 0 auto;
            padding: 4rem 2rem;
        }
        .plat-label {
            font-size: 0.7rem; font-weight: 700; text-transform: uppercase;
            letter-spacing: 1.5px; color: var(--accent); margin-bottom: 0.45rem;
        }
        .plat-h2 {
            font-size: 1.55rem; font-weight: 800; color: var(--navy);
            letter-spacing: -0.5px; margin-bottom: 0.5rem;
        }
        .plat-sub {
            font-size: 0.9rem; color: var(--gray-500); max-width: 500px;
            line-height: 1.6; margin-bottom: 2.5rem;
        }
        .plat-grid {
            display: grid; grid-template-columns: repeat(4, 1fr); gap: 1.25rem;
        }
        .plat-item {
            text-align: center; padding: 1.25rem;
            border: 1px solid var(--border); border-radius: 10px;
            background: var(--white);
        }
        .plat-num {
            width: 32px; height: 32px; border-radius: 6px;
            background: var(--navy); color: var(--white);
            font-size: 0.75rem; font-weight: 700;
            display: inline-flex; align-items: center; justify-content: center;
            margin-bottom: 0.75rem;
        }
        .plat-item h5 {
            font-size: 0.8rem; font-weight: 700; color: var(--navy);
            margin-bottom: 0.2rem;
        }
        .plat-item p {
            font-size: 0.72rem; color: var(--gray-400); line-height: 1.45;
        }

        /* ─── CTA ─── */
        .about-cta {
            max-width: 1100px; margin: 0 auto 3rem;
            padding: 2.5rem;
            background: var(--navy); border-radius: 10px;
            display: flex; align-items: center; justify-content: space-between; gap: 1.5rem;
        }
        .about-cta h2 {
            font-size: 1.25rem; font-weight: 800; color: var(--white);
            letter-spacing: -0.3px; margin-bottom: 0.2rem;
        }
        .about-cta p { color: var(--gray-400); font-size: 0.85rem; }
        .btn-accent {
            display: inline-flex; align-items: center; gap: 0.35rem;
            padding: 0.6rem 1.35rem; background: var(--accent); color: var(--white);
            font-size: 0.84rem; font-weight: 600; border-radius: 7px;
            transition: opacity 0.15s; white-space: nowrap;
        }
        .btn-accent:hover { opacity: 0.9; }

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

        /* ─── RESPONSIVE ─── */
        .mobile-nav-toggle {
            display: none;
            flex-direction: column;
            gap: 5px;
            background: none;
            border: none;
            cursor: pointer;
            padding: 6px;
        }
        .mobile-nav-toggle span {
            display: block; width: 20px; height: 2px;
            background: var(--navy); border-radius: 2px;
            transition: transform 0.25s, opacity 0.25s;
        }
        .mobile-nav-toggle.active span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
        .mobile-nav-toggle.active span:nth-child(2) { opacity: 0; }
        .mobile-nav-toggle.active span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

        @media (max-width: 992px) {
            .story { grid-template-columns: 1fr; gap: 2rem; }
            .val-grid { grid-template-columns: 1fr; gap: 1rem; }
            .plat-grid { grid-template-columns: repeat(2, 1fr); }
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
            .about-cta {
                flex-direction: column; text-align: center;
                margin-left: 1rem; margin-right: 1rem;
            }
        }
        @media (max-width: 768px) {
            .page-h1 { font-size: 1.875rem; }
            .nav-inner { padding: 0 1.25rem; }
            .page-header { padding: 5rem 1.25rem 2rem; }
        }
        @media (max-width: 576px) {
            .page-h1 { font-size: 1.5rem; }
            .plat-grid { grid-template-columns: 1fr; }
            .foot-inner { flex-direction: column; gap: 0.5rem; }
            .nav-inner { height: 50px; }
            .nav-brand { font-size: 0.95rem; }
            .nav-cta { padding: 0.35rem 0.75rem; font-size: 0.72rem; }
            .page-header { padding: 4.5rem 1rem 1.5rem; }
            .page-lead { font-size: 0.84rem; }
            .story, .values, .platform { padding: 2rem 1rem; }
            .nav-logo img, .brand-logo-img { height: 28px; max-width: 120px; }
        }
        @media (max-width: 375px) {
            .nav-inner { padding: 0 0.875rem; height: 48px; }
            .nav-signin { display: none; }
            .nav-cta { padding: 0.3rem 0.625rem; font-size: 0.7rem; }
            .nav-logo img, .brand-logo-img { height: 24px; max-width: 100px; }
            .page-header { padding: 4rem 0.875rem 1.25rem; }
            .page-h1 { font-size: 1.375rem; }
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
                </a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Home</a>
                    <a href="${pageContext.request.contextPath}/user/browse">Browse</a>
                    <a href="${pageContext.request.contextPath}/about.jsp" class="active">About</a>
                    <a href="${pageContext.request.contextPath}/contact.jsp">Contact</a>
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
        <h1 class="page-h1">Built by students,<br>for students</h1>
        <p class="page-lead">
            UniTrade started as a simple question: why is it so hard for college students in
            Nepal to trade second-hand stuff on campus? We built the answer.
        </p>
    </header>

    <!-- STORY -->
    <section class="story">
        <div class="story-text">
            <h2>The story behind UniTrade</h2>
            <p>
                Every semester, thousands of Nepali students buy textbooks, calculators, and supplies
                they only use for a few months. When the semester ends, those items pile up &mdash;
                while incoming students scramble to find the same things at full price.
            </p>
            <p>
                UniTrade was born from this cycle. We're a university project that grew into something
                real: a verified, student-only marketplace where you can list what you no longer need
                and find what you do &mdash; all within your college community.
            </p>
            <p>
                No middlemen. No shipping fees. No strangers. Just students helping students,
                face-to-face on campus.
            </p>
        </div>
        <div class="story-visual">
            <div class="story-stat">
                <div class="stat-number">500+</div>
                <div class="stat-label">Students registered on the platform</div>
            </div>
            <div class="story-stat">
                <div class="stat-number">1,200+</div>
                <div class="stat-label">Items listed since launch</div>
            </div>
            <div class="story-stat">
                <div class="stat-number">15+</div>
                <div class="stat-label">Colleges across Nepal represented</div>
            </div>
        </div>
    </section>

    <div class="section-line"><hr></div>

    <!-- VALUES -->
    <section class="values">
        <div class="val-label">Our values</div>
        <h2 class="val-h2">What we stand for</h2>
        <div class="val-grid">
            <div class="val-card">
                <div class="val-icon vi-1">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
                </div>
                <h4>Trust &amp; safety</h4>
                <p>Every user is verified by admin before they can trade. We keep the community clean and accountable so you can transact with confidence.</p>
            </div>
            <div class="val-card">
                <div class="val-icon vi-2">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>
                </div>
                <h4>Sustainability</h4>
                <p>Reuse what's already out there. By trading second-hand, students save money and reduce waste &mdash; it's better for wallets and the planet.</p>
            </div>
            <div class="val-card">
                <div class="val-icon vi-3">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
                </div>
                <h4>Community first</h4>
                <p>UniTrade is not a faceless marketplace. It's your campus community, digitised. Know who you're trading with &mdash; they sit in the next lecture hall.</p>
            </div>
        </div>
    </section>

    <div class="section-line"><hr></div>

    <!-- PLATFORM -->
    <section class="platform">
        <div class="plat-label">How the platform works</div>
        <h2 class="plat-h2">Simple by design</h2>
        <p class="plat-sub">No learning curve. No complex flows. Four steps from signup to your first trade.</p>
        <div class="plat-grid">
            <div class="plat-item">
                <div class="plat-num">1</div>
                <h5>Register</h5>
                <p>Sign up with your college name, course, and year</p>
            </div>
            <div class="plat-item">
                <div class="plat-num">2</div>
                <h5>Get verified</h5>
                <p>Admin reviews and approves your account</p>
            </div>
            <div class="plat-item">
                <div class="plat-num">3</div>
                <h5>List or browse</h5>
                <p>Post items for sale or find what you need</p>
            </div>
            <div class="plat-item">
                <div class="plat-num">4</div>
                <h5>Trade on campus</h5>
                <p>Meet the other student and exchange in person</p>
            </div>
        </div>
    </section>

    <!-- CTA -->
    <section style="padding: 0 2rem 0;">
        <div class="about-cta">
            <div>
                <h2>Join the UniTrade community</h2>
                <p>Create your free account and start trading with students near you.</p>
            </div>
            <a href="${pageContext.request.contextPath}/auth/register" class="btn-accent">
                Create Account &rarr;
            </a>
        </div>
    </section>

    <!-- FOOTER -->
    <footer class="foot">
        <div class="foot-inner">
            <span>&copy; 2026 UniTrade. All rights reserved.</span>
            <div class="foot-links">
                <a href="${pageContext.request.contextPath}/">Home</a>
                <a href="${pageContext.request.contextPath}/contact.jsp">Contact</a>
                <a href="${pageContext.request.contextPath}/auth/login">Sign In</a>
            </div>
        </div>
    </footer>

</body>
</html>

