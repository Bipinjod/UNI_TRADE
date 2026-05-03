-- UniTrade DB Migration v2
-- Adds missing columns and tables to existing unitrade_db

USE unitrade_db;

-- 1. Add category_type to categories if it doesn't exist
ALTER TABLE categories
  ADD COLUMN IF NOT EXISTS category_type ENUM('ITEM','SERVICE','REQUEST') NOT NULL DEFAULT 'ITEM' AFTER category_name;

-- 2. Drop old unique key and add new composite unique key if not already there
ALTER TABLE categories
  DROP INDEX IF EXISTS category_name;

ALTER TABLE categories
  ADD UNIQUE INDEX IF NOT EXISTS uq_cat_name_type (category_name, category_type);

-- 3. Create services table
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

-- 4. Create service_orders table
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

-- 5. Create help_requests table
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

-- 6. Create request_responses table
CREATE TABLE IF NOT EXISTS request_responses (
    response_id       INT AUTO_INCREMENT PRIMARY KEY,
    request_id        INT          NOT NULL,
    responder_id      INT          NOT NULL,
    response_message  VARCHAR(255) NOT NULL,
    response_status   ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id)   REFERENCES help_requests(request_id) ON DELETE CASCADE,
    FOREIGN KEY (responder_id) REFERENCES users(user_id)            ON DELETE CASCADE
) ENGINE=InnoDB;

-- 7. Create admin_logs table
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

-- 8. Ensure admin user exists (skip if already present)
INSERT IGNORE INTO users (full_name, email, phone, password_hash, role, approval_status, account_status,
                           college_name, course_name, academic_year)
VALUES ('Admin User', 'admin@unitrade.com', '9800000000',
        '$2a$12$LJ3m4ys3Gl4hPMALKM8aOesYFatHijQLPTx2KRjBOgaBfSMvhHxEq',
        'ADMIN', 'APPROVED', 'ACTIVE',
        'UniTrade Admin', 'Platform Administration', '2026');

-- 9. Insert missing item categories (using INSERT IGNORE to skip duplicates)
INSERT IGNORE INTO categories (category_name, category_type, description, status) VALUES
('Textbooks',       'ITEM', 'Academic textbooks and study materials',  'ACTIVE'),
('Electronics',     'ITEM', 'Laptops, phones, chargers, accessories', 'ACTIVE'),
('Lab Equipment',   'ITEM', 'Lab coats, kits, instruments',           'ACTIVE'),
('Stationery',      'ITEM', 'Pens, notebooks, art supplies',          'ACTIVE'),
('Hostel Supplies', 'ITEM', 'Bedding, fans, kitchenware',             'ACTIVE'),
('Clothing',        'ITEM', 'Uniforms, casual wear',                  'ACTIVE'),
('Calculators',     'ITEM', 'Scientific and graphing calculators',    'ACTIVE'),
('Project Kits',    'ITEM', 'Arduino, Raspberry Pi, breadboards',     'ACTIVE');

-- 10. Insert service categories
INSERT IGNORE INTO categories (category_name, category_type, description, status) VALUES
('Tutoring',    'SERVICE', 'Academic tutoring and coaching',        'ACTIVE'),
('Design',      'SERVICE', 'Graphic design and UI/UX work',        'ACTIVE'),
('Programming', 'SERVICE', 'Coding, debugging, project help',      'ACTIVE'),
('Writing',     'SERVICE', 'Essay writing, editing, proofreading', 'ACTIVE'),
('Photography', 'SERVICE', 'Photo and video shoots',               'ACTIVE');

-- 11. Insert request categories
INSERT IGNORE INTO categories (category_name, category_type, description, status) VALUES
('Academic Help',  'REQUEST', 'Need help with coursework',         'ACTIVE'),
('Tech Support',   'REQUEST', 'Software or hardware issues',       'ACTIVE'),
('Moving Help',    'REQUEST', 'Need help moving hostel items',     'ACTIVE'),
('Group Projects', 'REQUEST', 'Looking for project teammates',     'ACTIVE'),
('General',        'REQUEST', 'Miscellaneous help requests',       'ACTIVE');

SELECT 'Migration v2 completed successfully!' AS status;
