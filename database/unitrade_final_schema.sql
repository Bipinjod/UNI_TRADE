-- ============================================================
-- UniTrade - Full Database Schema
-- Student Marketplace and Service Platform
-- ============================================================
-- Run this file against your MySQL server:
--   mysql -u root -p < unitrade_final_schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS unitrade_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE unitrade_db;

-- ──────────────────────────────────────────────
-- 1. users
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    user_id          INT AUTO_INCREMENT PRIMARY KEY,
    full_name        VARCHAR(100)  NOT NULL,
    email            VARCHAR(100)  NOT NULL UNIQUE,
    phone            VARCHAR(20)   NOT NULL UNIQUE,
    password_hash    VARCHAR(255)  NOT NULL,
    role             ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
    approval_status  ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    account_status   ENUM('ACTIVE','INACTIVE','BLOCKED') NOT NULL DEFAULT 'ACTIVE',
    college_name     VARCHAR(150)  NOT NULL,
    course_name      VARCHAR(100)  NOT NULL,
    academic_year    VARCHAR(20)   NOT NULL,
    profile_image    VARCHAR(255)  DEFAULT NULL,
    created_at       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 2. categories  (supports ITEM / SERVICE / REQUEST types)
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS categories (
    category_id    INT AUTO_INCREMENT PRIMARY KEY,
    category_name  VARCHAR(100)  NOT NULL,
    category_type  ENUM('ITEM','SERVICE','REQUEST') NOT NULL DEFAULT 'ITEM',
    description    VARCHAR(255)  DEFAULT NULL,
    status         ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_cat_name_type (category_name, category_type)
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 3. items
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS items (
    item_id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT            NOT NULL,
    category_id     INT            NOT NULL,
    title           VARCHAR(150)   NOT NULL,
    description     TEXT           NOT NULL,
    price           DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    item_condition  ENUM('NEW','LIKE_NEW','GOOD','USED') NOT NULL DEFAULT 'USED',
    image_path      VARCHAR(255)   DEFAULT NULL,
    listing_status  ENUM('PENDING','APPROVED','REJECTED','SOLD','INACTIVE') NOT NULL DEFAULT 'PENDING',
    created_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)     REFERENCES users(user_id)      ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    INDEX idx_items_status (listing_status),
    INDEX idx_items_user   (user_id)
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 4. item_orders
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS item_orders (
    order_id      INT AUTO_INCREMENT PRIMARY KEY,
    item_id       INT            NOT NULL,
    buyer_id      INT            NOT NULL,
    seller_id     INT            NOT NULL,
    quantity      INT            NOT NULL DEFAULT 1,
    message       VARCHAR(255)   DEFAULT NULL,
    order_status  ENUM('PENDING','ACCEPTED','REJECTED','COMPLETED','CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id)   REFERENCES items(item_id)  ON DELETE CASCADE,
    FOREIGN KEY (buyer_id)  REFERENCES users(user_id)  ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users(user_id)  ON DELETE CASCADE,
    INDEX idx_orders_buyer  (buyer_id),
    INDEX idx_orders_seller (seller_id)
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 5. wishlists
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS wishlists (
    wishlist_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT       NOT NULL,
    item_id      INT       NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_wishlist (user_id, item_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 6. remember_tokens
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS remember_tokens (
    token_id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT          NOT NULL,
    selector        VARCHAR(32)  NOT NULL UNIQUE,
    validator_hash  VARCHAR(255) NOT NULL,
    expires_at      DATETIME     NOT NULL,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 7. services
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS services (
    service_id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id             INT            NOT NULL,
    category_id         INT            NOT NULL,
    title               VARCHAR(150)   NOT NULL,
    description         TEXT           NOT NULL,
    price               DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    availability_status ENUM('AVAILABLE','UNAVAILABLE') NOT NULL DEFAULT 'AVAILABLE',
    approval_status     ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)     REFERENCES users(user_id)          ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    INDEX idx_svc_approval (approval_status),
    INDEX idx_svc_user     (user_id)
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 8. service_orders
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS service_orders (
    service_order_id  INT AUTO_INCREMENT PRIMARY KEY,
    service_id        INT          NOT NULL,
    buyer_id          INT          NOT NULL,
    provider_id       INT          NOT NULL,
    request_message   VARCHAR(255) DEFAULT NULL,
    order_status      ENUM('PENDING','ACCEPTED','REJECTED','COMPLETED','CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (service_id)  REFERENCES services(service_id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id)    REFERENCES users(user_id)       ON DELETE CASCADE,
    FOREIGN KEY (provider_id) REFERENCES users(user_id)       ON DELETE CASCADE
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 9. help_requests
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS help_requests (
    request_id       INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT            NOT NULL,
    category_id      INT            NOT NULL,
    title            VARCHAR(150)   NOT NULL,
    description      TEXT           NOT NULL,
    budget           DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    urgency_level    ENUM('LOW','MEDIUM','HIGH') NOT NULL DEFAULT 'MEDIUM',
    request_status   ENUM('OPEN','CLOSED','FULFILLED') NOT NULL DEFAULT 'OPEN',
    approval_status  ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)     REFERENCES users(user_id)          ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    INDEX idx_hr_approval (approval_status),
    INDEX idx_hr_user     (user_id)
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 10. request_responses
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS request_responses (
    response_id       INT AUTO_INCREMENT PRIMARY KEY,
    request_id        INT          NOT NULL,
    responder_id      INT          NOT NULL,
    response_message  VARCHAR(255) NOT NULL,
    response_status   ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id)  REFERENCES help_requests(request_id) ON DELETE CASCADE,
    FOREIGN KEY (responder_id) REFERENCES users(user_id)           ON DELETE CASCADE
) ENGINE=InnoDB;

-- ──────────────────────────────────────────────
-- 11. admin_logs
-- ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS admin_logs (
    log_id             INT AUTO_INCREMENT PRIMARY KEY,
    admin_id           INT          NOT NULL,
    action_type        VARCHAR(50)  NOT NULL,
    target_table       VARCHAR(50)  NOT NULL,
    target_id          INT          NOT NULL,
    action_description VARCHAR(255) NOT NULL,
    created_at         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_logs_admin (admin_id)
) ENGINE=InnoDB;

-- ============================================================
-- SEED DATA
-- ============================================================

-- Admin account  (password: admin123  — BCrypt hash)
INSERT INTO users (full_name, email, phone, password_hash, role, approval_status, account_status,
                   college_name, course_name, academic_year)
VALUES ('Admin User', 'admin@unitrade.com', '9800000000',
        '$2a$12$LJ3m4ys3Gl4hPMALKM8aOesYFatHijQLPTx2KRjBOgaBfSMvhHxEq',
        'ADMIN', 'APPROVED', 'ACTIVE',
        'UniTrade Admin', 'Platform Administration', '2026');

-- ── Item categories ──
INSERT INTO categories (category_name, category_type, description, status) VALUES
('Textbooks',       'ITEM', 'Academic textbooks and study materials',  'ACTIVE'),
('Electronics',     'ITEM', 'Laptops, phones, chargers, accessories', 'ACTIVE'),
('Lab Equipment',   'ITEM', 'Lab coats, kits, instruments',           'ACTIVE'),
('Stationery',      'ITEM', 'Pens, notebooks, art supplies',          'ACTIVE'),
('Hostel Supplies', 'ITEM', 'Bedding, fans, kitchenware',             'ACTIVE'),
('Clothing',        'ITEM', 'Uniforms, casual wear',                  'ACTIVE'),
('Calculators',     'ITEM', 'Scientific and graphing calculators',    'ACTIVE'),
('Project Kits',    'ITEM', 'Arduino, Raspberry Pi, breadboards',     'ACTIVE');

-- ── Service categories ──
INSERT INTO categories (category_name, category_type, description, status) VALUES
('Tutoring',          'SERVICE', 'Academic tutoring and coaching',        'ACTIVE'),
('Design',            'SERVICE', 'Graphic design and UI/UX work',        'ACTIVE'),
('Programming',       'SERVICE', 'Coding, debugging, project help',      'ACTIVE'),
('Writing',           'SERVICE', 'Essay writing, editing, proofreading',  'ACTIVE'),
('Photography',       'SERVICE', 'Photo and video shoots',               'ACTIVE');

-- ── Request categories ──
INSERT INTO categories (category_name, category_type, description, status) VALUES
('Academic Help',     'REQUEST', 'Need help with coursework',            'ACTIVE'),
('Tech Support',      'REQUEST', 'Software or hardware issues',          'ACTIVE'),
('Moving Help',       'REQUEST', 'Need help moving hostel items',        'ACTIVE'),
('Group Projects',    'REQUEST', 'Looking for project teammates',        'ACTIVE'),
('General',           'REQUEST', 'Miscellaneous help requests',          'ACTIVE');

