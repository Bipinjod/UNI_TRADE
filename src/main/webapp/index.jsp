<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UniTrade — Nepal's Campus Marketplace</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,400;0,500;0,600;0,700;0,800;1,500&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        html { scroll-behavior: smooth; }
        body {
            font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;
            background: #f8fafc;
            color: #0f172a;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
            overflow-x: hidden;
        }
        a { text-decoration: none; color: inherit; }
        ::selection { background: rgba(15, 118, 110, 0.15); }
        :root {
            --primary: #0f766e;
            --primary-dark: #115e59;
            --primary-light: #ccfbf1;
            --success: #16a34a;
            --accent: #10b981;
            --bg: #f8fafc;
            --gray-500: #64748b;
            --gray-400: #94a3b8;
            --gray-200: #e2e8f0;
            --border: #e2e8f0;
            --white: #ffffff;
        }

        /* ─── BRAND LOGO IMG ─── */
        .brand-logo-img {
            height: 42px;
            width: auto;
            max-width: 220px;
            object-fit: contain;
            display: block;
            flex-shrink: 0;
        }

        /* ─── NAV ─── */
        .nav {
            position: fixed; top: 0; left: 0; right: 0; z-index: 100;
            border-bottom: 1px solid var(--border);
            backdrop-filter: blur(8px);
            background: rgba(255,255,255,0.92);
        }
        .nav-inner {
            max-width: 1100px; margin: 0 auto; padding: 0 2rem;
            height: 56px; display: flex; align-items: center; justify-content: space-between;
        }
        .nav-left { display: flex; align-items: center; gap: 2.75rem; }
        .nav-brand {
            font-size: 1.05rem; font-weight: 800; color: var(--primary-dark);
            display: flex; align-items: center; gap: 0.4rem;
            letter-spacing: -0.4px;
        }
        .nav-brand .dot {
            width: 7px; height: 7px; border-radius: 50%;
            background: var(--primary); display: inline-block; flex-shrink: 0;
        }
        .nav-links { display: flex; gap: 1.5rem; }
        .nav-links a {
            font-size: 0.8125rem; font-weight: 500; color: var(--gray-500);
            transition: color 0.15s; position: relative;
        }
        .nav-links a:hover { color: var(--primary-dark); }
        .nav-links a.active { color: var(--primary-dark); }
        .nav-links a.active::after {
            content: ''; position: absolute; bottom: -18px; left: 0; right: 0;
            height: 1.5px; background: var(--primary-dark);
        }
        .nav-right { display: flex; align-items: center; gap: 1rem; }
        .nav-signin {
            font-size: 0.8125rem; font-weight: 600; color: var(--primary-dark);
            padding: 0.35rem 0; transition: color 0.15s;
        }
        .nav-signin:hover { color: var(--primary); }
        .nav-cta {
            padding: 0.4rem 0.95rem; background: var(--primary); color: var(--white);
            font-size: 0.78rem; font-weight: 600; border-radius: 6px;
            transition: background 0.2s;
        }
        .nav-cta:hover { background: var(--primary-dark); }

        /* ─── HERO ─── */
        .hero {
            max-width: 1100px; margin: 0 auto; padding: 0 2rem;
            padding-top: 132px; padding-bottom: 64px;
            display: grid; grid-template-columns: 1.1fr 0.9fr; gap: 3rem;
            align-items: center;
        }
        .hero-text { text-align: left; }

        .pill {
            display: inline-flex; align-items: center; gap: 0.35rem;
            border: 1.5px solid var(--gray-200); border-radius: 100px;
            padding: 0.25rem 0.75rem 0.25rem 0.6rem;
            font-size: 0.72rem; font-weight: 600; color: var(--gray-500);
            margin-bottom: 1.5rem; background: var(--white);
            letter-spacing: 0.1px;
        }
        .pill-dot {
            width: 5px; height: 5px; border-radius: 50%;
            background: var(--primary); display: inline-block;
        }

        .hero-h1 {
            font-size: clamp(2.25rem, 4.2vw, 3.25rem);
            font-weight: 800; line-height: 1.1; color: var(--primary-dark);
            letter-spacing: -1.5px; margin-bottom: 1.15rem;
        }
        .campus-word {
            display: inline;
            background-image: url("data:image/svg+xml,%3Csvg width='120' height='8' viewBox='0 0 120 8' fill='none' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M2 5.5C20 2.5 40 6 60 3.5C80 1 100 5.5 118 3' stroke='%230f766e' stroke-width='3' stroke-linecap='round'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: bottom center;
            background-size: 100% 8px;
            padding-bottom: 6px;
        }

        .hero-sub {
            font-size: 1rem; line-height: 1.65; color: var(--gray-500);
            max-width: 400px; margin-bottom: 2rem; font-weight: 400;
            letter-spacing: -0.1px;
        }

        .hero-actions { display: flex; gap: 0.6rem; align-items: center; flex-wrap: wrap; }
        .btn-dark {
            display: inline-flex; align-items: center; gap: 0.35rem;
            padding: 0.65rem 1.35rem; background: var(--primary); color: var(--white);
            font-size: 0.875rem; font-weight: 600; border-radius: 7px;
            transition: background 0.2s, transform 0.15s;
        }
        .btn-dark:hover { background: var(--primary-dark); }
        .btn-dark:active { transform: scale(0.98); }
        .btn-dark .arrow { font-size: 0.95rem; transition: transform 0.2s; }
        .btn-dark:hover .arrow { transform: translateX(2px); }
        .btn-ghost {
            display: inline-flex; align-items: center; gap: 0.35rem;
            padding: 0.65rem 1.35rem; background: transparent;
            border: 1.5px solid var(--gray-200); color: var(--primary-dark);
            font-size: 0.875rem; font-weight: 600; border-radius: 7px;
            transition: border-color 0.15s;
        }
        .btn-ghost:hover { border-color: var(--primary); color: var(--primary); }

        /* ─── FLOATING CARD ─── */
        .hero-visual {
            display: flex; justify-content: center; align-items: center;
            position: relative; padding: 2rem 0;
        }
        .mock-card {
            background: var(--white); border: 1px solid var(--border);
            border-radius: 10px; width: 280px; overflow: hidden;
            box-shadow:
                0 1px 2px rgba(26,26,46,0.04),
                0 8px 24px rgba(26,26,46,0.06),
                0 24px 48px rgba(26,26,46,0.06);
            transform: rotate(-3deg);
            transition: transform 0.35s cubic-bezier(.4,0,.2,1), box-shadow 0.35s ease;
        }
        .mock-card:hover {
            transform: rotate(0deg) translateY(-6px);
            box-shadow:
                0 1px 2px rgba(26,26,46,0.04),
                0 12px 32px rgba(26,26,46,0.08),
                0 32px 64px rgba(26,26,46,0.07);
        }
        .mock-img {
            height: 160px; background: #f3f4f6;
            display: flex; align-items: center; justify-content: center;
            position: relative; overflow: hidden;
        }
        .mock-img-icon {
            width: 60px; height: 72px; border-radius: 4px;
            background: var(--gray-200); position: relative;
        }
        .mock-img-icon::before {
            content: ''; position: absolute; top: 10px; left: 8px; right: 8px;
            height: 3px; background: var(--gray-400); border-radius: 2px; opacity: 0.5;
        }
        .mock-img-icon::after {
            content: ''; position: absolute; top: 18px; left: 8px; width: 24px;
            height: 3px; background: var(--gray-400); border-radius: 2px; opacity: 0.35;
        }
        .mock-tag {
            position: absolute; top: 10px; left: 10px;
            background: var(--success); color: var(--white);
            font-size: 0.625rem; font-weight: 700; text-transform: uppercase;
            padding: 0.15rem 0.45rem; border-radius: 3px; letter-spacing: 0.5px;
        }
        .mock-body { padding: 0.85rem 1rem 1rem; }
        .mock-cat {
            font-size: 0.65rem; font-weight: 700; text-transform: uppercase;
            letter-spacing: 0.8px; color: var(--primary); margin-bottom: 0.25rem;
        }
        .mock-title {
            font-size: 0.9rem; font-weight: 700; color: var(--primary-dark);
            margin-bottom: 0.2rem; letter-spacing: -0.2px;
        }
        .mock-desc {
            font-size: 0.72rem; color: var(--gray-500); margin-bottom: 0.65rem;
        }
        .foot-bottom {
            display: flex; align-items: center; justify-content: space-between;
            flex-wrap: wrap; gap: 0.5rem;
        }
        .mock-price {
            font-size: 1.05rem; font-weight: 800; color: var(--primary-dark); letter-spacing: -0.3px;
        }
        .mock-seller { display: flex; align-items: center; gap: 0.3rem; }
        .mock-avatar {
            width: 20px; height: 20px; border-radius: 50%;
            background: var(--primary); color: var(--white);
            font-size: 0.5rem; font-weight: 700;
            display: flex; align-items: center; justify-content: center;
        }
        .mock-name { font-size: 0.65rem; color: var(--gray-400); font-weight: 500; }

        .depth-card {
            position: absolute; z-index: -1;
            width: 260px; height: 200px;
            background: var(--white); border: 1px solid var(--border);
            border-radius: 10px;
            transform: rotate(4deg) translate(40px, 24px);
            box-shadow: 0 8px 24px rgba(26,26,46,0.04);
            opacity: 0.5;
        }

        /* ─── SOCIAL PROOF ─── */
        .proof {
            max-width: 1100px; margin: 0 auto; padding: 0 2rem 4.5rem;
            display: flex; align-items: center; gap: 0.85rem;
        }
        .av-stack { display: flex; }
        .av-stack .av {
            width: 30px; height: 30px; border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            font-size: 0.6rem; font-weight: 700; color: var(--white);
            border: 2px solid var(--bg);
        }
        .av-stack .av:not(:first-child) { margin-left: -8px; }
        .av-1 { background: #0f766e; }
        .av-2 { background: #16a34a; }
        .av-3 { background: #10b981; }
        .av-4 { background: #0284c7; }
        .proof-text {
            font-size: 0.825rem; color: var(--gray-500); font-weight: 500;
            letter-spacing: -0.1px;
        }
        .proof-text strong { color: var(--primary-dark); font-weight: 700; }

        /* ─── SHARED ─── */
        .section-line { max-width: 1100px; margin: 0 auto; padding: 0 2rem; }
        .section-line hr { border: none; height: 1px; background: var(--border); }
        .sec-label {
            font-size: 0.7rem; font-weight: 700; text-transform: uppercase;
            letter-spacing: 1.5px; color: var(--primary); margin-bottom: 0.45rem;
        }
        .sec-h2 {
            font-size: 1.55rem; font-weight: 800; color: var(--primary-dark);
            letter-spacing: -0.5px; margin-bottom: 0.5rem;
        }
        .sec-sub {
            font-size: 0.9rem; color: var(--gray-500); max-width: 480px;
            line-height: 1.6;
        }

        /* ─── HOW IT WORKS ─── */
        .how {
            max-width: 1100px; margin: 0 auto;
            padding: 4.5rem 2rem 4rem;
        }
        .how-grid {
            display: grid; grid-template-columns: repeat(3, 1fr); gap: 2rem;
            margin-top: 2.5rem;
        }
        .how-step {
            padding: 1.5rem; border: 1px solid var(--border); border-radius: 10px;
            background: var(--white);
            transition: box-shadow 0.2s, transform 0.2s;
        }
        .how-step:hover {
            box-shadow: 0 8px 24px rgba(26,26,46,0.05);
            transform: translateY(-2px);
        }
        .step-num {
            width: 28px; height: 28px; border-radius: 6px;
            background: var(--navy); color: var(--white);
            font-size: 0.7rem; font-weight: 700;
            display: flex; align-items: center; justify-content: center;
            margin-bottom: 0.85rem;
        }
        .how-step h4 {
            font-size: 0.9rem; font-weight: 700; color: var(--navy);
            margin-bottom: 0.3rem; letter-spacing: -0.1px;
        }
        .how-step p {
            font-size: 0.8rem; color: var(--gray-500); line-height: 1.55;
        }

        /* ─── FEATURES ─── */
        .features {
            max-width: 1100px; margin: 0 auto;
            padding: 4.5rem 2rem 4rem;
        }
        .feat-grid {
            display: grid; grid-template-columns: repeat(2, 1fr); gap: 1.25rem;
            margin-top: 2.5rem;
        }
        .feat-card {
            padding: 1.35rem; border: 1px solid var(--border); border-radius: 10px;
            background: var(--white); transition: box-shadow 0.2s, transform 0.2s;
        }
        .feat-card:hover {
            box-shadow: 0 8px 24px rgba(26,26,46,0.05);
            transform: translateY(-2px);
        }
        .feat-icon {
            width: 36px; height: 36px; border-radius: 8px;
            display: flex; align-items: center; justify-content: center;
            margin-bottom: 0.85rem; font-size: 0.85rem; font-weight: 800;
        }
        .feat-icon.fi-1 { background: rgba(16,185,129,0.08); color: var(--primary); }
        .feat-icon.fi-2 { background: rgba(2,132,199,0.08); color: #0284c7; }
        .feat-icon.fi-3 { background: rgba(22,163,74,0.08); color: #16a34a; }
        .feat-icon.fi-4 { background: rgba(245,158,11,0.08); color: #f59e0b; }
        .feat-card h4 {
            font-size: 0.85rem; font-weight: 700; color: var(--primary-dark);
            margin-bottom: 0.25rem; letter-spacing: -0.1px;
        }
        .feat-card p {
            font-size: 0.78rem; color: var(--gray-500); line-height: 1.5;
        }

        /* ─── CATEGORIES ─── */
        .cats {
            max-width: 1100px; margin: 0 auto;
            padding: 0 2rem 4.5rem;
        }
        .cats-h3 {
            font-size: 0.8125rem; font-weight: 700; color: var(--navy);
            margin-bottom: 1rem; letter-spacing: -0.2px;
        }
        .cats-row { display: flex; flex-wrap: wrap; gap: 1rem; }
        .cat-chip {
            padding: 0.4rem 0.85rem; border: 1.5px solid var(--border);
            border-radius: 6px; font-size: 0.75rem; font-weight: 600;
            color: var(--gray-500); background: var(--white);
            transition: border-color 0.15s, color 0.15s;
            cursor: default;
        }
        .cat-chip:hover { border-color: var(--primary); color: var(--primary); }

        /* ─── TRUST ─── */
        .trust {
            max-width: 1100px; margin: 0 auto;
            padding: 4.5rem 2rem 4rem;
        }
        .trust-grid {
            display: grid; grid-template-columns: repeat(3, 1fr); gap: 1.5rem;
            margin-top: 2.5rem;
        }
        .trust-card {
            padding: 1.5rem; border: 1px solid var(--border); border-radius: 10px;
            background: var(--white);
        }
        .trust-quote {
            font-size: 0.84rem; color: var(--gray-500); line-height: 1.6;
            margin-bottom: 1rem; font-style: italic;
        }
        .trust-author {
            display: flex; align-items: center; gap: 0.5rem;
        }
        .trust-av {
            width: 28px; height: 28px; border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            font-size: 0.55rem; font-weight: 700; color: var(--white);
        }
        .trust-name { font-size: 0.75rem; font-weight: 600; color: var(--primary-dark); }
        .trust-role { font-size: 0.68rem; color: var(--gray-400); }

        /* ─── BOTTOM CTA ─── */
        .bottom-cta {
            max-width: 1100px; margin: 0 auto 3rem;
            padding: 2.5rem;
            background: var(--primary-dark); border-radius: 10px;
            display: flex; align-items: center; justify-content: space-between;
            gap: 1.5rem; flex-wrap: wrap;
        }
        .bottom-cta h2 {
            font-size: 1.25rem; font-weight: 800; color: var(--white);
            letter-spacing: -0.3px; margin-bottom: 0.2rem;
        }
        .bottom-cta p { color: var(--gray-400); font-size: 0.85rem; }
        .btn-accent {
            display: inline-flex; align-items: center; gap: 0.35rem;
            padding: 0.6rem 1.35rem; background: var(--primary); color: var(--white);
            font-size: 0.84rem; font-weight: 600; border-radius: 7px;
            transition: opacity 0.15s; white-space: nowrap;
        }
        .btn-accent:hover { opacity: 0.9; background: var(--primary-dark); }

        /* ─── FOOTER ─── */
        .foot {
            border-top: 1px solid var(--border); background: var(--white);
            padding: 2.5rem 2rem 1.5rem;
        }
        .foot-inner {
            max-width: 1100px; margin: 0 auto;
        }
        .foot-top {
            display: grid; grid-template-columns: 1.5fr 1fr 1fr 1fr; gap: 2rem;
            padding-bottom: 2rem; border-bottom: 1px solid var(--border);
            margin-bottom: 1.5rem;
        }
        .foot-brand {
            font-size: 1rem; font-weight: 800; color: var(--primary-dark);
            display: flex; align-items: center; gap: 0.35rem;
            margin-bottom: 0.65rem;
        }
        .foot-brand .dot {
            width: 6px; height: 6px; border-radius: 50%; background: var(--primary);
        }
        .foot-tagline {
            font-size: 0.8rem; color: var(--gray-400); line-height: 1.55;
            max-width: 240px;
        }
        .foot-col h5 {
            font-size: 0.72rem; font-weight: 700; text-transform: uppercase;
            letter-spacing: 1px; color: var(--navy); margin-bottom: 0.75rem;
        }
        .foot-col a {
            display: block; font-size: 0.8rem; color: var(--gray-500);
            margin-bottom: 0.45rem; font-weight: 500; transition: color 0.15s;
        }
        .foot-col a:hover { color: var(--primary-dark); }
        .foot-bottom {
            display: flex; align-items: center; justify-content: space-between;
            font-size: 0.72rem; color: var(--gray-400);
        }

        /* ─── CTA SECTION WRAPPER ─── */
        .cta-wrap-section { padding: 0 2rem; }
        @media (max-width: 768px) { .cta-wrap-section { padding: 0 1.25rem; } }
        @media (max-width: 576px) { .cta-wrap-section { padding: 0 1rem; } }

        /* ─── RESPONSIVE ─── */
        /* --- Mobile nav hamburger --- */
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
            display: block;
            width: 20px;
            height: 2px;
            background: var(--navy);
            border-radius: 2px;
            transition: transform 0.25s, opacity 0.25s;
        }
        .mobile-nav-toggle.active span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
        .mobile-nav-toggle.active span:nth-child(2) { opacity: 0; }
        .mobile-nav-toggle.active span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

        @media (max-width: 992px) {
            .hero {
                grid-template-columns: 1fr; gap: 2.5rem;
                padding-top: 112px; padding-bottom: 2.5rem;
            }
            .hero-visual { justify-content: flex-start; }
            .depth-card { display: none; }
            .how-grid { grid-template-columns: 1fr; gap: 1rem; }
            .feat-grid { grid-template-columns: repeat(2, 1fr); }
            .trust-grid { grid-template-columns: 1fr; gap: 1rem; }
            .nav-links { display: none; }
            .nav-links.nav-open {
                display: flex;
                flex-direction: column;
                position: absolute;
                top: 56px;
                left: 0;
                right: 0;
                background: rgba(255,255,255,0.98);
                padding: 0.75rem 2rem 1rem;
                border-bottom: 1px solid var(--border);
                box-shadow: 0 4px 12px rgba(26,26,46,0.06);
                gap: 0.5rem;
                z-index: 99;
            }
            .mobile-nav-toggle { display: flex; }
            .bottom-cta {
                flex-direction: column; text-align: center;
                margin-left: 1rem; margin-right: 1rem;
            }
            .foot-top { grid-template-columns: 1fr 1fr; }
        }
        @media (max-width: 768px) {
            .nav-inner { padding: 0 1.25rem; }
            .hero { padding: 96px 1.25rem 2rem; }
            .hero-h1 { font-size: 2.125rem; letter-spacing: -1px; }
            .hero-sub { font-size: 0.9375rem; }
            .how, .features, .trust { padding-left: 1.25rem; padding-right: 1.25rem; }
            .cats { padding-left: 1.25rem; padding-right: 1.25rem; }
            .proof { padding-left: 1.25rem; padding-right: 1.25rem; }
            .sec-h2 { font-size: 1.35rem; }
            .bottom-cta { padding: 2rem 1.5rem; margin-left: 0.75rem; margin-right: 0.75rem; }
            .bottom-cta h2 { font-size: 1.1rem; }
            .foot { padding: 2rem 1.25rem 1.25rem; }
        }
        @media (max-width: 576px) {
            .hero { padding-top: 88px; }
            .hero-h1 { font-size: 1.75rem; letter-spacing: -0.8px; }
            .hero-sub { font-size: 0.875rem; max-width: 320px; }
            .hero-actions { flex-direction: column; align-items: flex-start; }
            .mock-card { width: 240px; }
            .feat-grid { grid-template-columns: 1fr; }
            .cats-row { gap: 0.4rem; }
            .cat-chip { font-size: 0.7rem; padding: 0.35rem 0.7rem; }
            .foot-top { grid-template-columns: 1fr; gap: 1.5rem; }
            .foot-bottom { flex-direction: column; gap: 0.5rem; text-align: center; }
            .proof-text { font-size: 0.78rem; }
            .sec-h2 { font-size: 1.2rem; }
            .sec-sub { font-size: 0.84rem; }
            .pill { font-size: 0.68rem; }
            .nav-inner { height: 50px; }
            .nav-brand { font-size: 0.95rem; }
            .nav-cta { padding: 0.35rem 0.75rem; font-size: 0.72rem; }
            .bottom-cta { padding: 1.75rem 1.25rem; }
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
                <button class="mobile-nav-toggle" aria-label="Toggle navigation" aria-expanded="false" aria-controls="main-nav-links" onclick="this.classList.toggle('active');var navLinks=this.closest('.nav-inner').querySelector('.nav-links');navLinks.classList.toggle('nav-open');this.setAttribute('aria-expanded',navLinks.classList.contains('nav-open'))">
                    <span></span><span></span><span></span>
                </button>
                <div class="nav-links" id="main-nav-links" aria-label="Main navigation">
                    <a href="${pageContext.request.contextPath}/" class="active">Home</a>
                    <a href="${pageContext.request.contextPath}/user/browse">Browse</a>
                    <a href="${pageContext.request.contextPath}/about.jsp">About</a>
                    <a href="${pageContext.request.contextPath}/contact.jsp">Contact</a>
                </div>
            </div>
            <div class="nav-right">
                <a href="${pageContext.request.contextPath}/auth/login" class="nav-signin">Sign In</a>
                <a href="${pageContext.request.contextPath}/auth/register" class="nav-cta">Post Listing</a>
            </div>
        </div>
    </nav>

    <!-- HERO -->
    <section class="hero" role="main">
        <div class="hero-text">
            <div class="pill">
                <span class="pill-dot"></span> For Nepali college students
            </div>
            <h1 class="hero-h1">
                Nepal's <span class="campus-word">Campus</span><br>Marketplace
            </h1>
            <p class="hero-sub">
                Trade second-hand textbooks, lab gear, and everyday supplies
                with verified students from colleges across Nepal.
            </p>
            <div class="hero-actions">
                <a href="${pageContext.request.contextPath}/auth/register" class="btn-dark">
                    Create Account <span class="arrow">&rarr;</span>
                </a>
                <a href="${pageContext.request.contextPath}/user/browse" class="btn-ghost">
                    Browse Listings
                </a>
            </div>
        </div>

        <div class="hero-visual">
            <div class="depth-card"></div>
            <div class="mock-card">
                <div class="mock-img">
                    <div class="mock-img-icon"></div>
                    <span class="mock-tag">Like New</span>
                </div>
                <div class="mock-body">
                    <div class="mock-cat">Textbooks</div>
                    <div class="mock-title">Java: The Complete Reference</div>
                    <div class="mock-desc">11th Edition &middot; Herbert Schildt</div>
                    <div class="mock-footer">
                        <span class="mock-price">Rs. 350</span>
                        <div class="mock-seller">
                            <div class="mock-avatar">AP</div>
                            <span class="mock-name">Aarav P.</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>



    <div class="section-line"><hr></div>

    <!-- HOW IT WORKS -->
    <section class="how">
        <div class="sec-label">How it works</div>
        <h2 class="sec-h2">List, discover, trade &mdash; in minutes</h2>
        <p class="sec-sub">No middlemen, no fees. Just students helping students on campus.</p>
        <div class="how-grid">
            <div class="how-step">
                <div class="step-num">1</div>
                <h4>Create your account</h4>
                <p>Sign up with your college details. Accounts are verified by admin before going live.</p>
            </div>
            <div class="how-step">
                <div class="step-num">2</div>
                <h4>Post or browse</h4>
                <p>List items with a photo and price, or search what other students are selling nearby.</p>
            </div>
            <div class="how-step">
                <div class="step-num">3</div>
                <h4>Meet &amp; trade</h4>
                <p>Connect on campus, agree on a price, and complete the exchange in person safely.</p>
            </div>
        </div>
    </section>

    <div class="section-line"><hr></div>

    <!-- FEATURES -->
    <section class="features">
        <div class="sec-label">Why UniTrade</div>
        <h2 class="sec-h2">Everything you need, nothing you don't</h2>
        <p class="sec-sub">Built specifically for how Nepali college students actually trade.</p>
        <div class="feat-grid">
            <div class="feat-card">
                <div class="feat-icon fi-1">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/></svg>
                </div>
                <h4>Post in seconds</h4>
                <p>Add a photo, set your price, and your listing goes live after a quick admin review.</p>
            </div>
            <div class="feat-card">
                <div class="feat-icon fi-2">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
                </div>
                <h4>Search &amp; filter</h4>
                <p>Find exactly what you need by category, condition, price range, or keyword.</p>
            </div>
            <div class="feat-card">
                <div class="feat-icon fi-3">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
                </div>
                <h4>Verified students</h4>
                <p>Every account is reviewed. Trade only with real, approved students from your campus.</p>
            </div>
            <div class="feat-card">
                <div class="feat-icon fi-4">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
                </div>
                <h4>Save to wishlist</h4>
                <p>Bookmark items you want. Get back to them anytime from your personal dashboard.</p>
            </div>
        </div>
    </section>

    <!-- CATEGORIES -->
    <section class="cats">
        <div class="cats-h3">Popular categories</div>
        <div class="cats-row">
            <span class="cat-chip">Textbooks</span>
            <span class="cat-chip">Electronics</span>
            <span class="cat-chip">Lab Equipment</span>
            <span class="cat-chip">Stationery</span>
            <span class="cat-chip">Hostel Supplies</span>
            <span class="cat-chip">Calculators</span>
            <span class="cat-chip">Project Kits</span>
            <span class="cat-chip">Clothing</span>
        </div>
    </section>

    <div class="section-line"><hr></div>

    <!-- TRUST / TESTIMONIALS -->
    <section class="trust">
        <div class="sec-label">Trusted by students</div>
        <h2 class="sec-h2">What students are saying</h2>
        <div class="trust-grid">
            <div class="trust-card">
                <p class="trust-quote">"Sold my entire semester's worth of textbooks in two days. Way better than posting in random Facebook groups."</p>
                <div class="trust-author">
                    <div class="trust-av" style="background:#1a1a2e;">SP</div>
                    <div>
                        <div class="trust-name">Srijana P.</div>
                        <div class="trust-role">CSIT, Tribhuvan University</div>
                    </div>
                </div>
            </div>
            <div class="trust-card">
                <p class="trust-quote">"Found a second-hand scientific calculator for Rs. 800 &mdash; it was Rs. 3,500 new. The verified accounts make it feel safe."</p>
                <div class="trust-author">
                    <div class="trust-av" style="background:#e94560;">RB</div>
                    <div>
                        <div class="trust-name">Rohan B.</div>
                        <div class="trust-role">BCA, Kathmandu University</div>
                    </div>
                </div>
            </div>
            <div class="trust-card">
                <p class="trust-quote">"The wishlist feature is clutch &mdash; I saved items I wanted and bought them when I had the budget. Simple and clean."</p>
                <div class="trust-author">
                    <div class="trust-av" style="background:#16a34a;">AM</div>
                    <div>
                        <div class="trust-name">Anisha M.</div>
                        <div class="trust-role">BIT, Pokhara University</div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- BOTTOM CTA -->
    <section class="cta-wrap-section">
        <div class="bottom-cta">
            <div>
                <h2>Ready to start trading?</h2>
                <p>Create a free account and list your first item today.</p>
            </div>
            <a href="${pageContext.request.contextPath}/auth/register" class="btn-accent">
                Get Started &rarr;
            </a>
        </div>
    </section>

    <!-- FOOTER -->
    <footer class="foot">
        <div class="foot-inner">
            <div class="foot-top">
                <div>
                    <div class="foot-brand"><span class="dot" style="width:6px;height:6px;border-radius:50%;background:var(--accent);display:inline-block;"></span> UniTrade</div>
                    <p class="foot-tagline">A student-to-student marketplace built for Nepali college campuses. Trade smarter, spend less.</p>
                </div>
                <div class="foot-col">
                    <h5>Platform</h5>
                    <a href="${pageContext.request.contextPath}/user/browse">Browse Items</a>
                    <a href="${pageContext.request.contextPath}/auth/register">Create Account</a>
                    <a href="${pageContext.request.contextPath}/auth/login">Sign In</a>
                </div>
                <div class="foot-col">
                    <h5>Company</h5>
                    <a href="${pageContext.request.contextPath}/about.jsp">About</a>
                    <a href="${pageContext.request.contextPath}/contact.jsp">Contact</a>
                </div>
                <div class="foot-col">
                    <h5>Support</h5>
                    <a href="${pageContext.request.contextPath}/contact.jsp">Help Centre</a>
                    <a href="mailto:support@unitrade.edu.np">Email Us</a>
                </div>
            </div>
            <div class="foot-bottom">
                <span>&copy; 2026 UniTrade. All rights reserved.</span>
                <span>Made with care in Nepal</span>
            </div>
        </div>
    </footer>

</body>
</html>
