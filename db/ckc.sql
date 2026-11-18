-- Design rules:
--   * Stock and member balances are NOT stored. They are computed from donations/usages and from
--     transactions, so there is only one source of truth.
--   * Trainer and Kitchen-Tech are subclasses in Java only; the database keeps one users table with a role.
--   * Wear is tracked per piece of equipment (hours used since last_maintenance_at compared with
--     maintenance_threshold_hours), not per component.
--   * Work orders are out of scope (agreed with the professor).

DROP DATABASE IF EXISTS ckc_db;
CREATE DATABASE ckc_db;
USE ckc_db;

-- ---------------------------------------------------------------- user
CREATE TABLE users (
    user_id        INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    role           ENUM('MEMBER','TRAINER','KITCHEN_TECH') NOT NULL DEFAULT 'MEMBER',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------- equipment
CREATE TABLE equipment (
    equipment_id                INT AUTO_INCREMENT PRIMARY KEY,
    asset_tag                   VARCHAR(20)  NOT NULL UNIQUE,
    make                        VARCHAR(50)  NOT NULL,
    model                       VARCHAR(50)  NOT NULL,
    category                    ENUM('OVEN','MIXER','DOUGH_SHEETER','PROOFER','COOLER') NOT NULL,
    credit_rate_per_hour        DECIMAL(10,2) NOT NULL,
    status                      ENUM('AVAILABLE','IN_USE','DOWN_FOR_MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE',
    maintenance_threshold_hours INT NOT NULL,
    last_maintenance_at         DATETIME NULL,
    created_at                  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_equipment_rate      CHECK (credit_rate_per_hour >= 0),
    CONSTRAINT chk_equipment_threshold CHECK (maintenance_threshold_hours > 0)
);

CREATE TABLE usage_sessions (
    session_id    INT AUTO_INCREMENT PRIMARY KEY,
    equipment_id  INT NOT NULL,
    user_id       INT NOT NULL,
    check_in_at   DATETIME NOT NULL,
    check_out_at  DATETIME NULL,                     -- NULL means the session is still active
    -- equals equipment_id only while the session is active, so UNIQUE allows one active session per equipment
    active_equipment_id INT AS (IF(check_out_at IS NULL, equipment_id, NULL)) STORED,
    CONSTRAINT fk_session_equipment FOREIGN KEY (equipment_id) REFERENCES equipment (equipment_id),
    CONSTRAINT fk_session_user      FOREIGN KEY (user_id)      REFERENCES users (user_id),
    CONSTRAINT uq_one_active_session UNIQUE (active_equipment_id),
    CONSTRAINT chk_session_times    CHECK (check_out_at IS NULL OR check_out_at >= check_in_at)
);

-- ---------------------------------------------------------------- inventory
CREATE TABLE consumables (
    consumable_id          INT AUTO_INCREMENT PRIMARY KEY,
    name                   VARCHAR(100) NOT NULL UNIQUE,
    unit                   ENUM('KG','L','UNIT') NOT NULL,
    credit_value_per_unit  DECIMAL(10,2) NOT NULL,      -- earned when donated, charged when used
    reorder_level          DECIMAL(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT chk_consumable_value CHECK (credit_value_per_unit >= 0)
);

CREATE TABLE donations (
    donation_id    INT AUTO_INCREMENT PRIMARY KEY,
    consumable_id  INT NOT NULL,
    user_id        INT NOT NULL,
    quantity       DECIMAL(10,2) NOT NULL,
    donated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_donation_consumable FOREIGN KEY (consumable_id) REFERENCES consumables (consumable_id),
    CONSTRAINT fk_donation_user       FOREIGN KEY (user_id)       REFERENCES users (user_id),
    CONSTRAINT chk_donation_quantity  CHECK (quantity > 0)
);

CREATE TABLE consumable_usages (
    usage_id       INT AUTO_INCREMENT PRIMARY KEY,
    consumable_id  INT NOT NULL,
    user_id        INT NOT NULL,
    session_id     INT NULL,                         -- the equipment session it was used in, if any (FR-03)
    quantity       DECIMAL(10,2) NOT NULL,
    used_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usage_consumable FOREIGN KEY (consumable_id) REFERENCES consumables (consumable_id),
    CONSTRAINT fk_usage_user       FOREIGN KEY (user_id)       REFERENCES users (user_id),
    CONSTRAINT fk_usage_session    FOREIGN KEY (session_id)    REFERENCES usage_sessions (session_id),
    CONSTRAINT chk_usage_quantity  CHECK (quantity > 0)
);

-- ---------------------------------------------------------------- ledger
CREATE TABLE transactions (
    transaction_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    type            ENUM('CREDIT','DEBIT','PAYMENT') NOT NULL,   -- amount is always positive, type gives the sign
    source          ENUM('DONATION','TRAINING','MAINTENANCE','EQUIPMENT_USE','CONSUMABLE_USE','SETTLEMENT') NOT NULL,
    amount          DECIMAL(10,2) NOT NULL,
    description     VARCHAR(255) NULL,                           -- human-readable reference, for example "Session 3"
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT chk_transaction_amount CHECK (amount > 0)
);

CREATE INDEX idx_transactions_user_date ON transactions (user_id, created_at);

-- ---------------------------------------------------------------- training
CREATE TABLE training_sessions (
    training_id         INT AUTO_INCREMENT PRIMARY KEY,
    trainer_id          INT NOT NULL,
    title               VARCHAR(100) NOT NULL,
    equipment_category  ENUM('OVEN','MIXER','DOUGH_SHEETER','PROOFER','COOLER') NOT NULL,  -- what attendance qualifies for
    scheduled_at        DATETIME NOT NULL,
    duration_minutes    INT NOT NULL,
    CONSTRAINT fk_training_trainer FOREIGN KEY (trainer_id) REFERENCES users (user_id),
    CONSTRAINT chk_training_duration CHECK (duration_minutes > 0)
);

CREATE TABLE training_attendance (
    training_id  INT NOT NULL,
    user_id      INT NOT NULL,
    PRIMARY KEY (training_id, user_id),
    CONSTRAINT fk_attendance_training FOREIGN KEY (training_id) REFERENCES training_sessions (training_id),
    CONSTRAINT fk_attendance_user     FOREIGN KEY (user_id)     REFERENCES users (user_id)
);

-- ---------------------------------------------------------------- maintenance
CREATE TABLE maintenance_tasks (
    task_id         INT AUTO_INCREMENT PRIMARY KEY,
    equipment_id    INT NOT NULL,
    tech_id         INT NULL,                        -- NULL until a Kitchen-Tech takes the task
    description     VARCHAR(255) NOT NULL,
    scheduled_for   DATETIME NOT NULL,
    completed_at    DATETIME NULL,
    minutes_logged  INT NULL,
    CONSTRAINT fk_task_equipment FOREIGN KEY (equipment_id) REFERENCES equipment (equipment_id),
    CONSTRAINT fk_task_tech      FOREIGN KEY (tech_id)      REFERENCES users (user_id),
    CONSTRAINT chk_task_minutes  CHECK (minutes_logged IS NULL OR minutes_logged >= 0)
);

CREATE TABLE maintenance_alerts (
    alert_id      INT AUTO_INCREMENT PRIMARY KEY,
    equipment_id  INT NOT NULL,
    reason        ENUM('WEAR','DIAGNOSTIC') NOT NULL,
    details       VARCHAR(255) NULL,
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at   DATETIME NULL,                     -- NULL means the alert is still open
    task_id       INT NULL,                          -- the maintenance task that handles it
    -- set only while the alert is open, so UNIQUE allows one open alert per equipment and reason
    open_alert_key VARCHAR(40) AS (IF(resolved_at IS NULL, CONCAT(equipment_id, '-', reason), NULL)) STORED,
    CONSTRAINT fk_alert_equipment FOREIGN KEY (equipment_id) REFERENCES equipment (equipment_id),
    CONSTRAINT fk_alert_task      FOREIGN KEY (task_id)      REFERENCES maintenance_tasks (task_id),
    CONSTRAINT uq_one_open_alert  UNIQUE (open_alert_key)
);

-- ================================================================= sample data
-- password_hash values are placeholders until we pick a hashing method.
INSERT INTO users (user_id, name, email, password_hash, role) VALUES
    (1, 'Alice Martin',  'alice@ckc.test', 'CHANGE_ME', 'MEMBER'),
    (2, 'Bob Leroux',    'bob@ckc.test',   'CHANGE_ME', 'MEMBER'),
    (3, 'Tara Nguyen',   'tara@ckc.test',  'CHANGE_ME', 'TRAINER'),
    (4, 'Ken Okafor',    'ken@ckc.test',   'CHANGE_ME', 'KITCHEN_TECH');

INSERT INTO equipment (equipment_id, asset_tag, make, model, category, credit_rate_per_hour, status,
                       maintenance_threshold_hours, last_maintenance_at) VALUES
    (1, 'OVN-001', 'Rational', 'iCombi Pro', 'OVEN',          12.00, 'AVAILABLE',            500,  '2026-10-01 11:00:00'),
    (2, 'OVN-002', 'Rational', 'iCombi Pro', 'OVEN',          12.00, 'IN_USE',               500,  '2026-09-15 10:00:00'),
    (3, 'MIX-001', 'Hobart',   'HL200',      'MIXER',          6.00, 'AVAILABLE',            300,  '2026-10-20 09:00:00'),
    (4, 'SHT-001', 'Rondo',    'Econom',     'DOUGH_SHEETER',  5.00, 'DOWN_FOR_MAINTENANCE', 200,  '2026-08-01 09:00:00'),
    (5, 'COL-001', 'True',     'T-49',       'COOLER',         2.00, 'AVAILABLE',            2000, '2026-07-01 09:00:00');

INSERT INTO training_sessions (training_id, trainer_id, title, equipment_category, scheduled_at, duration_minutes) VALUES
    (1, 3, 'Oven safety',  'OVEN',  '2026-10-25 10:00:00', 60),
    (2, 3, 'Mixer safety', 'MIXER', '2026-10-26 10:00:00', 45);

-- Alice is qualified for ovens, Bob for ovens and mixers.
INSERT INTO training_attendance (training_id, user_id) VALUES (1, 1), (1, 2), (2, 2);

-- Session 3 is active, which is why OVN-002 is IN_USE.
INSERT INTO usage_sessions (session_id, equipment_id, user_id, check_in_at, check_out_at) VALUES
    (1, 1, 1, '2026-11-02 09:00:00', '2026-11-02 11:00:00'),
    (2, 3, 2, '2026-11-03 10:00:00', '2026-11-03 11:30:00'),
    (3, 2, 2, '2026-11-17 08:00:00', NULL);

INSERT INTO consumables (consumable_id, name, unit, credit_value_per_unit, reorder_level) VALUES
    (1, 'All-purpose flour', 'KG',    1.50, 20),
    (2, 'White sugar',       'KG',    2.00, 10),
    (3, 'Unsalted butter',   'KG',    8.00,  5),
    (4, 'Eggs',              'UNIT',  0.30, 60),
    (5, 'Pastry box',        'UNIT',  0.50, 50);

INSERT INTO donations (donation_id, consumable_id, user_id, quantity, donated_at) VALUES
    (1, 1, 1,  50.00, '2026-11-01 09:00:00'),
    (2, 4, 2, 120.00, '2026-11-01 09:30:00'),
    (3, 3, 1,  10.00, '2026-11-05 14:00:00');

INSERT INTO consumable_usages (usage_id, consumable_id, user_id, session_id, quantity, used_at) VALUES
    (1, 1, 1, 1,  5.00, '2026-11-02 10:00:00'),
    (2, 4, 2, 2, 12.00, '2026-11-03 10:30:00'),
    (3, 3, 2, 3,  1.50, '2026-11-17 08:30:00');

INSERT INTO maintenance_tasks (task_id, equipment_id, tech_id, description, scheduled_for, completed_at, minutes_logged) VALUES
    (1, 1, 4, 'Descale steam generator and calibrate probe', '2026-10-01 09:00:00', '2026-10-01 11:00:00', 120),
    (2, 4, 4, 'Replace conveyor belt',                       '2026-11-20 09:00:00', NULL,                  NULL);

INSERT INTO maintenance_alerts (alert_id, equipment_id, reason, details, created_at, resolved_at, task_id) VALUES
    (1, 4, 'DIAGNOSTIC', 'Operator reported the conveyor belt slipping', '2026-11-10 08:00:00', NULL, 2);

-- Credit rates for training (20.00 per hour) and maintenance (15.00 per hour) come from the
-- CreditStrategy classes; the amounts below are what those rates produce.
INSERT INTO transactions (user_id, type, source, amount, description, created_at) VALUES
    (4, 'CREDIT', 'MAINTENANCE',    30.00, 'Task 1: 120 min on OVN-001',      '2026-10-01 11:00:00'),
    (3, 'CREDIT', 'TRAINING',       20.00, 'Training 1: Oven safety, 60 min',  '2026-10-25 11:00:00'),
    (3, 'CREDIT', 'TRAINING',       15.00, 'Training 2: Mixer safety, 45 min', '2026-10-26 10:45:00'),
    (1, 'CREDIT', 'DONATION',       75.00, 'Donation 1: 50 kg flour',          '2026-11-01 09:00:00'),
    (2, 'CREDIT', 'DONATION',       36.00, 'Donation 2: 120 eggs',             '2026-11-01 09:30:00'),
    (1, 'DEBIT',  'CONSUMABLE_USE',  7.50, 'Usage 1: 5 kg flour',              '2026-11-02 10:00:00'),
    (1, 'DEBIT',  'EQUIPMENT_USE',  24.00, 'Session 1: OVN-001, 2.0 h',        '2026-11-02 11:00:00'),
    (2, 'DEBIT',  'CONSUMABLE_USE',  3.60, 'Usage 2: 12 eggs',                 '2026-11-03 10:30:00'),
    (2, 'DEBIT',  'EQUIPMENT_USE',   9.00, 'Session 2: MIX-001, 1.5 h',        '2026-11-03 11:30:00'),
    (1, 'CREDIT', 'DONATION',       80.00, 'Donation 3: 10 kg butter',         '2026-11-05 14:00:00'),
    (2, 'DEBIT',  'CONSUMABLE_USE', 12.00, 'Usage 3: 1.5 kg butter',           '2026-11-17 08:30:00');
