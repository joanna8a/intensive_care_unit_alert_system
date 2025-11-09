-- Medical Alert System Database Schema
-- MySQL 8.0 Compatible Version

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

-- Create Database
CREATE DATABASE IF NOT EXISTS `medical_alerts` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE `medical_alerts`;

-- Create Enums
DROP TABLE IF EXISTS `enum_gender`;
CREATE TABLE `enum_gender` (
    `gender` ENUM('MALE', 'FEMALE', 'OTHER') PRIMARY KEY
);

DROP TABLE IF EXISTS `enum_patient_condition`;
CREATE TABLE `enum_patient_condition` (
    `condition_type` VARCHAR(50) PRIMARY KEY
);

DROP TABLE IF EXISTS `enum_patient_status`;
CREATE TABLE `enum_patient_status` (
    `status` ENUM('ACTIVE', 'DISCHARGED', 'TRANSFERRED') PRIMARY KEY
);

DROP TABLE IF EXISTS `enum_data_source`;
CREATE TABLE `enum_data_source` (
    `source` ENUM('MANUAL', 'MONITOR', 'IOT_DEVICE') PRIMARY KEY
);

DROP TABLE IF EXISTS `enum_alert_severity`;
CREATE TABLE `enum_alert_severity` (
    `severity` ENUM('CRITICAL', 'WARNING', 'INFO') PRIMARY KEY
);

DROP TABLE IF EXISTS `enum_alert_status`;
CREATE TABLE `enum_alert_status` (
    `status` ENUM('ACTIVE', 'ACKNOWLEDGED', 'RESOLVED') PRIMARY KEY
);

DROP TABLE IF EXISTS `enum_action_priority`;
CREATE TABLE `enum_action_priority` (
    `priority` ENUM('HIGH', 'MEDIUM', 'LOW') PRIMARY KEY
);

-- Patients table
CREATE TABLE IF NOT EXISTS `patients` (
    `id` VARCHAR(36) NOT NULL,
    `medical_record_number` VARCHAR(50) NOT NULL,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `date_of_birth` DATE NOT NULL,
    `gender` ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    `condition_type` VARCHAR(50) NOT NULL DEFAULT 'ADULT_RESTING',
    `admission_date` DATETIME NULL,
    `room_number` VARCHAR(20) NULL,
    `status` ENUM('ACTIVE', 'DISCHARGED', 'TRANSFERRED') NOT NULL DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_patients_mrn` (`medical_record_number`),
    INDEX `idx_patients_status` (`status`),
    INDEX `idx_patients_admission_date` (`admission_date`),
    INDEX `idx_patients_name` (`last_name`, `first_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Patient vital signs table
CREATE TABLE IF NOT EXISTS patient_vital_signs (
    id VARCHAR(36) PRIMARY KEY,
    patient_id VARCHAR(36) NOT NULL,
    heart_rate DECIMAL(5,2),
    oxygen_saturation DECIMAL(5,2),
    systolic_bp DECIMAL(5,2),
    diastolic_bp DECIMAL(5,2),
    temperature DECIMAL(4,2),
    respiratory_rate DECIMAL(5,2),
    source ENUM('MANUAL', 'MONITOR', 'IOT_DEVICE') DEFAULT 'MONITOR',
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_timestamp (timestamp),
    INDEX idx_patient_timestamp (patient_id, timestamp)
);

-- Medical alerts table
CREATE TABLE IF NOT EXISTS medical_alerts (
    id VARCHAR(36) PRIMARY KEY,
    patient_id VARCHAR(36) NOT NULL,
    severity ENUM('CRITICAL', 'WARNING', 'INFO') NOT NULL,
    alert_type VARCHAR(100) NOT NULL,
    message_key VARCHAR(200) NOT NULL,
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    acknowledged_at TIMESTAMP NULL,
    acknowledged_by VARCHAR(100),
    status ENUM('ACTIVE', 'ACKNOWLEDGED', 'RESOLVED') DEFAULT 'ACTIVE',
    requires_acknowledgment BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_severity (severity),
    INDEX idx_status (status),
    INDEX idx_triggered_at (triggered_at)
);

-- Alert message parameters table
CREATE TABLE IF NOT EXISTS alert_message_params (
    id VARCHAR(36) PRIMARY KEY,
    alert_id VARCHAR(36) NOT NULL,
    param_key VARCHAR(100) NOT NULL,
    param_value TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (alert_id) REFERENCES medical_alerts(id) ON DELETE CASCADE,
    INDEX idx_alert_id (alert_id)
);

-- Recommended actions table
CREATE TABLE IF NOT EXISTS recommended_actions (
    id VARCHAR(36) PRIMARY KEY,
    alert_id VARCHAR(36) NOT NULL,
    message_key VARCHAR(200) NOT NULL,
    priority ENUM('HIGH', 'MEDIUM', 'LOW') DEFAULT 'MEDIUM',
    completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP NULL,
    completed_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (alert_id) REFERENCES medical_alerts(id) ON DELETE CASCADE,
    INDEX idx_alert_id (alert_id),
    INDEX idx_priority (priority),
    INDEX idx_completed (completed)
);

-- Alert history table
CREATE TABLE IF NOT EXISTS alert_history (
    id VARCHAR(36) PRIMARY KEY,
    alert_id VARCHAR(36) NOT NULL,
    old_status ENUM('ACTIVE', 'ACKNOWLEDGED', 'RESOLVED'),
    new_status ENUM('ACTIVE', 'ACKNOWLEDGED', 'RESOLVED'),
    action_type VARCHAR(50) NOT NULL,
    performed_by VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (alert_id) REFERENCES medical_alerts(id) ON DELETE CASCADE,
    INDEX idx_alert_id (alert_id),
    INDEX idx_created_at (created_at)
);

-- Medical staff/users table
CREATE TABLE IF NOT EXISTS `medical_staff` (
    `id` VARCHAR(36) NOT NULL,
    `username` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `role` ENUM('DOCTOR', 'NURSE', 'ADMIN', 'TECHNICIAN') NOT NULL,
    `department` VARCHAR(100) NULL,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `last_login` TIMESTAMP NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_staff_username` (`username`),
    UNIQUE INDEX `uk_staff_email` (`email`),
    INDEX `idx_staff_role` (`role`),
    INDEX `idx_staff_department` (`department`),
    INDEX `idx_staff_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Patient assignments table
CREATE TABLE IF NOT EXISTS `patient_assignments` (
    `id` VARCHAR(36) NOT NULL,
    `patient_id` VARCHAR(36) NOT NULL,
    `staff_id` VARCHAR(36) NOT NULL,
    `assignment_type` ENUM('PRIMARY', 'SECONDARY', 'ON_CALL') NOT NULL DEFAULT 'PRIMARY',
    `assigned_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `assigned_by` VARCHAR(36) NOT NULL,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `ended_at` TIMESTAMP NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_assignments_patient` (`patient_id`),
    INDEX `idx_assignments_staff` (`staff_id`),
    INDEX `idx_assignments_active` (`is_active`),
    INDEX `idx_assignments_type` (`assignment_type`),
    UNIQUE INDEX `uk_assignments_active` (`patient_id`, `staff_id`, `is_active`),
    CONSTRAINT `fk_assignments_patient`
        FOREIGN KEY (`patient_id`)
        REFERENCES `patients` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_assignments_staff`
        FOREIGN KEY (`staff_id`)
        REFERENCES `medical_staff` (`id`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert enum values
INSERT IGNORE INTO `enum_gender` (`gender`) VALUES 
('MALE'), ('FEMALE'), ('OTHER');

INSERT IGNORE INTO `enum_patient_status` (`status`) VALUES 
('ACTIVE'), ('DISCHARGED'), ('TRANSFERRED');

INSERT IGNORE INTO `enum_data_source` (`source`) VALUES 
('MANUAL'), ('MONITOR'), ('IOT_DEVICE');

INSERT IGNORE INTO `enum_alert_severity` (`severity`) VALUES 
('CRITICAL'), ('WARNING'), ('INFO');

INSERT IGNORE INTO `enum_alert_status` (`status`) VALUES 
('ACTIVE'), ('ACKNOWLEDGED'), ('RESOLVED');

INSERT IGNORE INTO `enum_action_priority` (`priority`) VALUES 
('HIGH'), ('MEDIUM'), ('LOW');

INSERT IGNORE INTO `enum_patient_condition` (`condition_type`) VALUES 
('ADULT_RESTING'),
('POST_SURGERY'),
('PEDIATRIC'),
('GERIATRIC'),
('CARDIAC'),
('RESPIRATORY'),
('NEUROLOGICAL'),
('ICU'),
('EMERGENCY');

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;