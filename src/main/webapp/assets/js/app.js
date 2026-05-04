/* ============================================================
   UNITRADE — app.js
   Minimal JS utilities: flash messages, confirms, nav toggle,
   form helpers, and general UX enhancements.
   ============================================================ */

(function () {
    'use strict';

    /* --------------------------------------------------------
       1. AUTO-DISMISS FLASH NOTICES & ALERTS
       -------------------------------------------------------- */
    function initFlashMessages() {
        var selectors = '.notice, .alert, .notice-success, .notice-error, .alert-success, .alert-error, .alert-info';
        var flashes = document.querySelectorAll(selectors);

        flashes.forEach(function (el) {
            // Fade-in
            el.style.opacity = '0';
            el.style.transform = 'translateY(-8px)';
            el.style.transition = 'opacity 0.35s ease, transform 0.35s ease';
            requestAnimationFrame(function () {
                el.style.opacity = '1';
                el.style.transform = 'translateY(0)';
            });

            // Auto-dismiss after 5 seconds
            setTimeout(function () {
                el.style.opacity = '0';
                el.style.transform = 'translateY(-8px)';
                setTimeout(function () {
                    if (el.parentNode) el.parentNode.removeChild(el);
                }, 400);
            }, 5000);

            // Click to dismiss immediately
            el.style.cursor = 'pointer';
            el.addEventListener('click', function () {
                el.style.opacity = '0';
                el.style.transform = 'translateY(-8px)';
                setTimeout(function () {
                    if (el.parentNode) el.parentNode.removeChild(el);
                }, 400);
            });
        });
    }

    /* --------------------------------------------------------
       2. DELETE / DESTRUCTIVE ACTION CONFIRMATIONS
       -------------------------------------------------------- */
    function initConfirmActions() {
        document.addEventListener('click', function (e) {
            var btn = e.target.closest('[data-confirm]');
            if (!btn) return;
            var message = btn.getAttribute('data-confirm') || 'Are you sure?';
            if (!confirm(message)) {
                e.preventDefault();
                e.stopImmediatePropagation();
            }
        });
    }

    /* --------------------------------------------------------
       3. MOBILE NAV TOGGLE
       -------------------------------------------------------- */
    function initMobileNav() {
        // --- User nav hamburger ---
        var userNav = document.querySelector('.user-nav .nav-container');
        if (userNav && !userNav.querySelector('.nav-toggle')) {
            var toggle = document.createElement('button');
            toggle.className = 'nav-toggle';
            toggle.setAttribute('aria-label', 'Toggle navigation');
            toggle.innerHTML = '<span></span><span></span><span></span>';
            userNav.insertBefore(toggle, userNav.querySelector('.nav-links'));

            toggle.addEventListener('click', function () {
                var links = userNav.querySelector('.nav-links');
                if (links) {
                    links.classList.toggle('nav-open');
                    toggle.classList.toggle('active');
                }
            });
        }

        // --- Admin sidebar hamburger ---
        var adminHeader = document.querySelector('.admin-header');
        var adminSidebar = document.querySelector('.admin-sidebar');
        if (adminHeader && adminSidebar && !adminHeader.querySelector('.admin-sidebar-toggle')) {
            // Create overlay
            var overlay = document.createElement('div');
            overlay.className = 'admin-sidebar-overlay';
            document.body.appendChild(overlay);

            // Create toggle button
            var sidebarToggle = document.createElement('button');
            sidebarToggle.className = 'admin-sidebar-toggle';
            sidebarToggle.setAttribute('aria-label', 'Toggle sidebar');
            sidebarToggle.innerHTML = '<span></span><span></span><span></span>';
            var headerLeft = adminHeader.querySelector('.header-left');
            if (headerLeft) {
                headerLeft.parentNode.insertBefore(sidebarToggle, headerLeft);
            }

            function closeSidebar() {
                adminSidebar.classList.remove('open');
                overlay.classList.remove('active');
                sidebarToggle.classList.remove('active');
            }

            function openSidebar() {
                adminSidebar.classList.add('open');
                overlay.classList.add('active');
                sidebarToggle.classList.add('active');
            }

            sidebarToggle.addEventListener('click', function () {
                if (adminSidebar.classList.contains('open')) {
                    closeSidebar();
                } else {
                    openSidebar();
                }
            });

            overlay.addEventListener('click', closeSidebar);

            // Close sidebar on nav item click (mobile)
            var navItems = adminSidebar.querySelectorAll('.nav-item, .logout-btn');
            navItems.forEach(function (item) {
                item.addEventListener('click', function () {
                    if (window.innerWidth <= 992) {
                        closeSidebar();
                    }
                });
            });
        }
    }

    /* --------------------------------------------------------
       4. FORM VALIDATION HELPERS
       -------------------------------------------------------- */
    function initFormValidation() {
        var forms = document.querySelectorAll('.item-form, .service-form, .request-form');

        forms.forEach(function (form) {
            form.addEventListener('submit', function (e) {
                var invalid = false;
                var required = form.querySelectorAll('[required]');
                required.forEach(function (field) {
                    field.classList.remove('input-error');
                    if (!field.value.trim()) {
                        field.classList.add('input-error');
                        invalid = true;
                    }
                });

                if (invalid) {
                    e.preventDefault();
                    var first = form.querySelector('.input-error');
                    if (first) first.focus();
                }
            });

            // Clear error style on input
            form.querySelectorAll('[required]').forEach(function (field) {
                field.addEventListener('input', function () {
                    if (field.value.trim()) {
                        field.classList.remove('input-error');
                    }
                });
            });
        });
    }

    /* --------------------------------------------------------
       5. CHARACTER COUNTER FOR TEXTAREAS
       -------------------------------------------------------- */
    function initCharCounters() {
        var textareas = document.querySelectorAll('textarea[maxlength]');
        textareas.forEach(function (ta) {
            var max = parseInt(ta.getAttribute('maxlength'), 10);
            var counter = document.createElement('div');
            counter.className = 'char-counter';
            counter.textContent = ta.value.length + ' / ' + max;
            ta.parentNode.appendChild(counter);

            ta.addEventListener('input', function () {
                counter.textContent = ta.value.length + ' / ' + max;
                counter.classList.toggle('char-limit', ta.value.length >= max * 0.9);
            });
        });
    }

    /* --------------------------------------------------------
       6. SMOOTH SCROLL-TO-TOP ON PAGE LOAD
       -------------------------------------------------------- */
    function scrollToTop() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    /* --------------------------------------------------------
       7. ACTIVE NAV LINK HIGHLIGHT
       -------------------------------------------------------- */
    function initActiveNav() {
        var path = window.location.pathname;
        var links = document.querySelectorAll('.nav-link, .nav-item');
        links.forEach(function (link) {
            var href = link.getAttribute('href');
            if (href && path.indexOf(href) !== -1) {
                link.classList.add('active');
            }
        });
    }

    /* --------------------------------------------------------
       8. SEARCH INPUT — CLEAR BUTTON
       -------------------------------------------------------- */
    function initSearchClear() {
        var searchInputs = document.querySelectorAll('.search-input, .filter-input');
        searchInputs.forEach(function (input) {
            if (!input.value) return;

            var wrapper = input.closest('.search-input-wrapper') || input.parentNode;
            if (wrapper.querySelector('.search-clear')) return;

            var clearBtn = document.createElement('button');
            clearBtn.type = 'button';
            clearBtn.className = 'search-clear';
            clearBtn.innerHTML = '&times;';
            clearBtn.title = 'Clear search';
            wrapper.style.position = 'relative';
            wrapper.appendChild(clearBtn);

            clearBtn.addEventListener('click', function () {
                input.value = '';
                input.focus();
                // If inside a form, optionally submit to reset results
                var form = input.closest('form');
                if (form) form.submit();
            });
        });
    }

    /* --------------------------------------------------------
       9. PRINT TIMESTAMP UTILITY
       -------------------------------------------------------- */
    function formatRelativeDate(dateStr) {
        var date = new Date(dateStr);
        var now = new Date();
        var diff = Math.floor((now - date) / 1000);
        if (diff < 60) return 'just now';
        if (diff < 3600) return Math.floor(diff / 60) + 'm ago';
        if (diff < 86400) return Math.floor(diff / 3600) + 'h ago';
        if (diff < 604800) return Math.floor(diff / 86400) + 'd ago';
        return date.toLocaleDateString();
    }

    /* --------------------------------------------------------
       10. LOADING STATE FOR FORM SUBMIT BUTTONS
       -------------------------------------------------------- */
    function initSubmitGuard() {
        document.querySelectorAll('form').forEach(function (form) {
            form.addEventListener('submit', function () {
                var btn = form.querySelector('button[type="submit"], .btn-primary[type="submit"]');
                if (btn && !btn.disabled) {
                    btn.disabled = true;
                    btn.dataset.originalText = btn.textContent;
                    btn.textContent = 'Please wait…';
                    // Re-enable after 5s in case of error
                    setTimeout(function () {
                        btn.disabled = false;
                        if (btn.dataset.originalText) {
                            btn.textContent = btn.dataset.originalText;
                        }
                    }, 5000);
                }
            });
        });
    }

    /* --------------------------------------------------------
       INIT — Run on DOM ready
       -------------------------------------------------------- */
    function init() {
        initFlashMessages();
        initConfirmActions();
        initMobileNav();
        initFormValidation();
        initCharCounters();
        initActiveNav();
        initSearchClear();
        initSubmitGuard();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    // Expose utility for inline use if needed
    window.UniTrade = {
        formatRelativeDate: formatRelativeDate
    };

})();

