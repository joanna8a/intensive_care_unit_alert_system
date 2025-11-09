-- Medical Alert System - Stored Procedures and Functions
-- This script creates useful stored procedures for common operations

USE `medical_alerts`;

DELIMITER //

-- Procedure: Acknowledge Medical Alert
DROP PROCEDURE IF EXISTS `sp_acknowledge_alert`//
CREATE PROCEDURE `sp_acknowledge_alert`(
    IN p_alert_id VARCHAR(36),
    IN p_acknowledged_by VARCHAR(100),
    IN p_notes TEXT
)
BEGIN
    DECLARE v_old_status VARCHAR(20);
    DECLARE v_patient_id VARCHAR(36);
    
    -- Get current status and patient ID
    SELECT status, patient_id INTO v_old_status, v_patient_id
    FROM medical_alerts 
    WHERE id = p_alert_id;
    
    -- Update alert status
    UPDATE medical_alerts 
    SET 
        status = 'ACKNOWLEDGED',
        acknowledged_at = NOW(),
        acknowledged_by = p_acknowledged_by,
        updated_at = NOW()
    WHERE id = p_alert_id;
    
    -- Log history
    INSERT INTO alert_history (id, alert_id, old_status, new_status, action_type, performed_by, notes)
    VALUES (UUID(), p_alert_id, v_old_status, 'ACKNOWLEDGED', 'ACKNOWLEDGE', p_acknowledged_by, p_notes);
    
    -- Return success
    SELECT 1 AS success, 'Alert acknowledged successfully' AS message;
END//

-- Procedure: Create New Medical Alert
DROP PROCEDURE IF EXISTS `sp_create_medical_alert`//
CREATE PROCEDURE `sp_create_medical_alert`(
    IN p_patient_id VARCHAR(36),
    IN p_severity ENUM('CRITICAL', 'WARNING', 'INFO'),
    IN p_alert_type VARCHAR(100),
    IN p_message_key VARCHAR(200),
    IN p_requires_acknowledgment BOOLEAN
)
BEGIN
    DECLARE v_alert_id VARCHAR(36);
    DECLARE v_patient_exists INT;
    
    -- Check if patient exists
    SELECT COUNT(*) INTO v_patient_exists FROM patients WHERE id = p_patient_id;
    
    IF v_patient_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Patient not found';
    END IF;
    
    -- Generate alert ID
    SET v_alert_id = UUID();
    
    -- Insert alert
    INSERT INTO medical_alerts (id, patient_id, severity, alert_type, message_key, requires_acknowledgment)
    VALUES (v_alert_id, p_patient_id, p_severity, p_alert_type, p_message_key, p_requires_acknowledgment);
    
    -- Log history
    INSERT INTO alert_history (id, alert_id, old_status, new_status, action_type, performed_by, notes)
    VALUES (UUID(), v_alert_id, NULL, 'ACTIVE', 'CREATED', 'SYSTEM', CONCAT('Alert created: ', p_alert_type));
    
    -- Return alert ID
    SELECT v_alert_id AS alert_id, 1 AS success, 'Alert created successfully' AS message;
END//

-- Function: Calculate Patient Risk Level
-- Function: Calculate Patient Risk Level
DROP FUNCTION IF EXISTS `fn_calculate_patient_risk`//
CREATE FUNCTION `fn_calculate_patient_risk`(p_patient_id VARCHAR(36)) 
RETURNS VARCHAR(20)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE v_critical_alerts INT;
    DECLARE v_warning_alerts INT;
    DECLARE v_risk_level VARCHAR(20);
    
    -- Count active critical alerts
    SELECT COUNT(*) INTO v_critical_alerts 
    FROM medical_alerts 
    WHERE patient_id = p_patient_id 
    AND severity = 'CRITICAL' 
    AND status = 'ACTIVE';
    
    -- Count active warning alerts
    SELECT COUNT(*) INTO v_warning_alerts 
    FROM medical_alerts 
    WHERE patient_id = p_patient_id 
    AND severity = 'WARNING' 
    AND status = 'ACTIVE';
    
    -- Determine risk level
    IF v_critical_alerts > 0 THEN
        SET v_risk_level = 'HIGH';
    ELSEIF v_warning_alerts > 2 THEN
        SET v_risk_level = 'MEDIUM';
    ELSEIF v_warning_alerts > 0 THEN
        SET v_risk_level = 'LOW';
    ELSE
        SET v_risk_level = 'NONE';
    END IF;
    
    RETURN v_risk_level;
END//