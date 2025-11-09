-- Medical Alert System - Database Views and Reporting
-- This script creates useful views for reporting and analytics

USE `medical_alerts`;

-- View: Active Patient Alerts
DROP VIEW IF EXISTS `v_active_patient_alerts`;
CREATE VIEW `v_active_patient_alerts` AS
SELECT 
    ma.id AS alert_id,
    ma.severity,
    ma.alert_type,
    ma.triggered_at,
    ma.requires_acknowledgment,
    p.id AS patient_id,
    p.medical_record_number,
    CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
    p.room_number,
    p.condition_type,
    TIMESTAMPDIFF(MINUTE, ma.triggered_at, NOW()) AS minutes_since_triggered
FROM 
    medical_alerts ma
    INNER JOIN patients p ON ma.patient_id = p.id
WHERE 
    ma.status = 'ACTIVE'
ORDER BY 
    ma.severity DESC, 
    ma.triggered_at ASC;

-- View: Patient Vital Signs Summary
DROP VIEW IF EXISTS `v_patient_vital_summary`;
CREATE VIEW `v_patient_vital_summary` AS
SELECT 
    p.id AS patient_id,
    p.medical_record_number,
    CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
    p.room_number,
    p.condition_type,
    (SELECT heart_rate FROM patient_vital_signs 
     WHERE patient_id = p.id 
     ORDER BY timestamp DESC LIMIT 1) AS latest_heart_rate,
    (SELECT oxygen_saturation FROM patient_vital_signs 
     WHERE patient_id = p.id 
     ORDER BY timestamp DESC LIMIT 1) AS latest_oxygen_saturation,
    (SELECT systolic_bp FROM patient_vital_signs 
     WHERE patient_id = p.id 
     ORDER BY timestamp DESC LIMIT 1) AS latest_systolic_bp,
    (SELECT diastolic_bp FROM patient_vital_signs 
     WHERE patient_id = p.id 
     ORDER BY timestamp DESC LIMIT 1) AS latest_diastolic_bp,
    (SELECT temperature FROM patient_vital_signs 
     WHERE patient_id = p.id 
     ORDER BY timestamp DESC LIMIT 1) AS latest_temperature,
    (SELECT respiratory_rate FROM patient_vital_signs 
     WHERE patient_id = p.id 
     ORDER BY timestamp DESC LIMIT 1) AS latest_respiratory_rate,
    (SELECT COUNT(*) FROM medical_alerts 
     WHERE patient_id = p.id AND status = 'ACTIVE') AS active_alerts_count
FROM 
    patients p
WHERE 
    p.status = 'ACTIVE';

-- View: Alert Statistics by Type and Severity
DROP VIEW IF EXISTS `v_alert_statistics`;
CREATE VIEW `v_alert_statistics` AS
SELECT 
    alert_type,
    severity,
    COUNT(*) AS total_count,
    SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_count,
    SUM(CASE WHEN status = 'ACKNOWLEDGED' THEN 1 ELSE 0 END) AS acknowledged_count,
    SUM(CASE WHEN status = 'RESOLVED' THEN 1 ELSE 0 END) AS resolved_count,
    AVG(TIMESTAMPDIFF(MINUTE, triggered_at, COALESCE(acknowledged_at, NOW()))) AS avg_minutes_to_acknowledge
FROM 
    medical_alerts
GROUP BY 
    alert_type, severity
ORDER BY 
    alert_type, severity;

-- View: Staff Assignment Overview
DROP VIEW IF EXISTS `v_staff_assignments`;
CREATE VIEW `v_staff_assignments` AS
SELECT 
    ms.id AS staff_id,
    CONCAT(ms.first_name, ' ', ms.last_name) AS staff_name,
    ms.role,
    ms.department,
    COUNT(pa.id) AS assigned_patients_count,
    GROUP_CONCAT(DISTINCT p.medical_record_number) AS assigned_mrns
FROM 
    medical_staff ms
    LEFT JOIN patient_assignments pa ON ms.id = pa.staff_id AND pa.is_active = TRUE
    LEFT JOIN patients p ON pa.patient_id = p.id AND p.status = 'ACTIVE'
WHERE 
    ms.is_active = TRUE
GROUP BY 
    ms.id, ms.first_name, ms.last_name, ms.role, ms.department
ORDER BY 
    ms.role, ms.department, staff_name;

-- View: Critical Alert Timeline
DROP VIEW IF EXISTS `v_critical_alert_timeline`;
CREATE VIEW `v_critical_alert_timeline` AS
SELECT 
    ma.id AS alert_id,
    ma.alert_type,
    ma.severity,
    ma.triggered_at,
    ma.acknowledged_at,
    ma.acknowledged_by,
    p.medical_record_number,
    CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
    p.room_number,
    TIMESTAMPDIFF(MINUTE, ma.triggered_at, ma.acknowledged_at) AS minutes_to_acknowledge,
    CASE 
        WHEN ma.acknowledged_at IS NULL THEN 'PENDING'
        ELSE 'ACKNOWLEDGED'
    END AS acknowledgment_status
FROM 
    medical_alerts ma
    INNER JOIN patients p ON ma.patient_id = p.id
WHERE 
    ma.severity = 'CRITICAL'
ORDER BY 
    ma.triggered_at DESC;

-- View: Patient Risk Assessment
DROP VIEW IF EXISTS `v_patient_risk_assessment`;
CREATE VIEW `v_patient_risk_assessment` AS
SELECT 
    p.id AS patient_id,
    p.medical_record_number,
    CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
    p.room_number,
    p.condition_type,
    TIMESTAMPDIFF(YEAR, p.date_of_birth, CURDATE()) AS age,
    -- Risk scoring logic
    CASE 
        WHEN (SELECT oxygen_saturation FROM patient_vital_signs 
              WHERE patient_id = p.id 
              ORDER BY timestamp DESC LIMIT 1) < 92 THEN 3
        WHEN (SELECT oxygen_saturation FROM patient_vital_signs 
              WHERE patient_id = p.id 
              ORDER BY timestamp DESC LIMIT 1) < 95 THEN 1
        ELSE 0
    END + 
    CASE 
        WHEN (SELECT heart_rate FROM patient_vital_signs 
              WHERE patient_id = p.id 
              ORDER BY timestamp DESC LIMIT 1) > 130 THEN 2
        WHEN (SELECT heart_rate FROM patient_vital_signs 
              WHERE patient_id = p.id 
              ORDER BY timestamp DESC LIMIT 1) < 50 THEN 2
        WHEN (SELECT heart_rate FROM patient_vital_signs 
              WHERE patient_id = p.id 
              ORDER BY timestamp DESC LIMIT 1) > 110 THEN 1
        ELSE 0
    END +
    CASE 
        WHEN (SELECT systolic_bp FROM patient_vital_signs 
              WHERE patient_id = p.id 
              ORDER BY timestamp DESC LIMIT 1) < 90 THEN 2
        WHEN (SELECT systolic_bp FROM patient_vital_signs 
              WHERE patient_id = p.id 
              ORDER BY timestamp DESC LIMIT 1) > 180 THEN 2
        ELSE 0
    END AS risk_score,
    CASE 
        WHEN TIMESTAMPDIFF(YEAR, p.date_of_birth, CURDATE()) > 65 THEN 1
        ELSE 0
    END AS age_risk_factor,
    (SELECT COUNT(*) FROM medical_alerts 
     WHERE patient_id = p.id 
     AND triggered_at > NOW() - INTERVAL 24 HOUR) AS alerts_24h
FROM 
    patients p
WHERE 
    p.status = 'ACTIVE'
ORDER BY 
    risk_score DESC;