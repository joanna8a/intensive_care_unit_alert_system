-- Medical Alert System - Sample Data
-- This script populates the database with realistic sample data for testing

USE `medical_alerts`;

-- Insert medical staff
INSERT IGNORE INTO `medical_staff` (`id`, `username`, `email`, `first_name`, `last_name`, `role`, `department`) VALUES
('STAFF-001', 'dr.smith', 'john.smith@hospital.com', 'John', 'Smith', 'DOCTOR', 'CARDIOLOGY'),
('STAFF-002', 'nurse.jones', 'sarah.jones@hospital.com', 'Sarah', 'Jones', 'NURSE', 'ICU'),
('STAFF-003', 'dr.wilson', 'michael.wilson@hospital.com', 'Michael', 'Wilson', 'DOCTOR', 'EMERGENCY'),
('STAFF-004', 'nurse.garcia', 'maria.garcia@hospital.com', 'Maria', 'Garcia', 'NURSE', 'CARDIOLOGY'),
('STAFF-005', 'admin.tech', 'tech.admin@hospital.com', 'Robert', 'Brown', 'ADMIN', 'IT');

-- Insert sample patients
INSERT IGNORE INTO `patients` (`id`, `medical_record_number`, `first_name`, `last_name`, `date_of_birth`, `gender`, `condition_type`, `admission_date`, `room_number`) VALUES
('PT-001', 'MRN-2024-001', 'John', 'Doe', '1960-05-15', 'MALE', 'POST_SURGERY', NOW() - INTERVAL 2 DAY, '301A'),
('PT-002', 'MRN-2024-002', 'Maria', 'Garcia', '1955-08-22', 'FEMALE', 'CARDIAC', NOW() - INTERVAL 1 DAY, '302B'),
('PT-003', 'MRN-2024-003', 'Robert', 'Johnson', '1972-12-10', 'MALE', 'ICU', NOW() - INTERVAL 3 DAY, '303A'),
('PT-004', 'MRN-2024-004', 'Susan', 'Williams', '1948-03-30', 'FEMALE', 'GERIATRIC', NOW() - INTERVAL 5 DAY, '304C'),
('PT-005', 'MRN-2024-005', 'David', 'Miller', '1985-07-14', 'MALE', 'RESPIRATORY', NOW() - INTERVAL 1 DAY, '305B');

-- Assign staff to patients
INSERT IGNORE INTO `patient_assignments` (`id`, `patient_id`, `staff_id`, `assignment_type`, `assigned_by`) VALUES
(UUID(), 'PT-001', 'STAFF-001', 'PRIMARY', 'STAFF-005'),
(UUID(), 'PT-001', 'STAFF-002', 'SECONDARY', 'STAFF-005'),
(UUID(), 'PT-002', 'STAFF-001', 'PRIMARY', 'STAFF-005'),
(UUID(), 'PT-003', 'STAFF-003', 'PRIMARY', 'STAFF-005'),
(UUID(), 'PT-004', 'STAFF-004', 'PRIMARY', 'STAFF-005'),
(UUID(), 'PT-005', 'STAFF-003', 'PRIMARY', 'STAFF-005');

-- Insert normal vital signs for patients
INSERT IGNORE INTO `patient_vital_signs` (`id`, `patient_id`, `heart_rate`, `oxygen_saturation`, `systolic_bp`, `diastolic_bp`, `temperature`, `respiratory_rate`, `source`, `timestamp`) VALUES
(UUID(), 'PT-001', 75.0, 98.5, 120.0, 80.0, 36.8, 16.0, 'MONITOR', NOW() - INTERVAL 2 HOUR),
(UUID(), 'PT-002', 82.0, 97.0, 118.0, 78.0, 36.9, 15.0, 'MONITOR', NOW() - INTERVAL 1 HOUR),
(UUID(), 'PT-003', 68.0, 99.0, 122.0, 82.0, 36.7, 14.0, 'MONITOR', NOW() - INTERVAL 3 HOUR),
(UUID(), 'PT-004', 85.0, 96.5, 135.0, 85.0, 37.0, 18.0, 'MONITOR', NOW() - INTERVAL 30 MINUTE),
(UUID(), 'PT-005', 78.0, 98.0, 125.0, 79.0, 36.6, 16.0, 'MONITOR', NOW() - INTERVAL 45 MINUTE);

-- Insert some critical vital signs to trigger alerts
INSERT IGNORE INTO `patient_vital_signs` (`id`, `patient_id`, `heart_rate`, `oxygen_saturation`, `systolic_bp`, `diastolic_bp`, `temperature`, `respiratory_rate`, `source`, `timestamp`) VALUES
-- Critical oxygen saturation
(UUID(), 'PT-001', 88.0, 89.5, 115.0, 75.0, 37.1, 22.0, 'MONITOR', NOW() - INTERVAL 10 MINUTE),
-- Critical heart rate
(UUID(), 'PT-002', 135.0, 95.0, 140.0, 90.0, 37.3, 20.0, 'MONITOR', NOW() - INTERVAL 5 MINUTE),
-- Critical blood pressure
(UUID(), 'PT-003', 65.0, 97.0, 85.0, 55.0, 36.5, 12.0, 'MONITOR', NOW() - INTERVAL 15 MINUTE),
-- Critical temperature
(UUID(), 'PT-004', 92.0, 96.0, 130.0, 82.0, 39.8, 24.0, 'MONITOR', NOW() - INTERVAL 8 MINUTE);

-- Insert active medical alerts
INSERT IGNORE INTO `medical_alerts` (`id`, `patient_id`, `severity`, `alert_type`, `message_key`, `triggered_at`, `requires_acknowledgment`) VALUES
-- Critical oxygen alert
('ALERT-001', 'PT-001', 'CRITICAL', 'OXYGEN_SATURATION', 'alert.critical.oxygen.saturation', NOW() - INTERVAL 10 MINUTE, TRUE),
-- Critical heart rate alert
('ALERT-002', 'PT-002', 'CRITICAL', 'HEART_RATE', 'alert.critical.heart_rate.high', NOW() - INTERVAL 5 MINUTE, TRUE),
-- Critical blood pressure alert
('ALERT-003', 'PT-003', 'CRITICAL', 'BLOOD_PRESSURE', 'alert.critical.blood_pressure.low', NOW() - INTERVAL 15 MINUTE, TRUE),
-- Critical temperature alert
('ALERT-004', 'PT-004', 'CRITICAL', 'TEMPERATURE', 'alert.critical.temperature.high', NOW() - INTERVAL 8 MINUTE, TRUE);

-- Insert alert message parameters
INSERT IGNORE INTO `alert_message_params` (`id`, `alert_id`, `param_key`, `param_value`) VALUES
(UUID(), 'ALERT-001', 'currentValue', '89.5'),
(UUID(), 'ALERT-001', 'threshold', '92.0'),
(UUID(), 'ALERT-001', 'patientName', 'John Doe'),
(UUID(), 'ALERT-002', 'currentValue', '135.0'),
(UUID(), 'ALERT-002', 'threshold', '130.0'),
(UUID(), 'ALERT-002', 'patientName', 'Maria Garcia'),
(UUID(), 'ALERT-003', 'currentValue', '85/55'),
(UUID(), 'ALERT-003', 'threshold', '90/60'),
(UUID(), 'ALERT-003', 'patientName', 'Robert Johnson'),
(UUID(), 'ALERT-004', 'currentValue', '39.8'),
(UUID(), 'ALERT-004', 'threshold', '38.5'),
(UUID(), 'ALERT-004', 'patientName', 'Susan Williams');

-- Insert recommended actions for alerts
INSERT IGNORE INTO `recommended_actions` (`id`, `alert_id`, `message_key`, `priority`) VALUES
-- Actions for oxygen alert
(UUID(), 'ALERT-001', 'recommendation.oxygen.supplemental', 'HIGH'),
(UUID(), 'ALERT-001', 'recommendation.notify.physician', 'HIGH'),
(UUID(), 'ALERT-001', 'recommendation.monitor.frequently', 'MEDIUM'),
-- Actions for heart rate alert
(UUID(), 'ALERT-002', 'recommendation.ecg.monitoring', 'HIGH'),
(UUID(), 'ALERT-002', 'recommendation.cardiology.consult', 'HIGH'),
(UUID(), 'ALERT-002', 'recommendation.vital.signs.check', 'MEDIUM'),
-- Actions for blood pressure alert
(UUID(), 'ALERT-003', 'recommendation.iv.fluids', 'HIGH'),
(UUID(), 'ALERT-003', 'recommendation.bp.monitoring', 'HIGH'),
(UUID(), 'ALERT-003', 'recommendation.trend.assessment', 'MEDIUM'),
-- Actions for temperature alert
(UUID(), 'ALERT-004', 'recommendation.antipyretics', 'HIGH'),
(UUID(), 'ALERT-004', 'recommendation.cooling.measures', 'HIGH'),
(UUID(), 'ALERT-004', 'recommendation.infection.workup', 'MEDIUM');

-- Insert some alert history
INSERT IGNORE INTO `alert_history` (`id`, `alert_id`, `old_status`, `new_status`, `action_type`, `performed_by`, `notes`) VALUES
(UUID(), 'ALERT-001', NULL, 'ACTIVE', 'CREATED', 'SYSTEM', 'Alert triggered by low oxygen saturation'),
(UUID(), 'ALERT-002', NULL, 'ACTIVE', 'CREATED', 'SYSTEM', 'Alert triggered by elevated heart rate'),
(UUID(), 'ALERT-003', NULL, 'ACTIVE', 'CREATED', 'SYSTEM', 'Alert triggered by low blood pressure'),
(UUID(), 'ALERT-004', NULL, 'ACTIVE', 'CREATED', 'SYSTEM', 'Alert triggered by high temperature');

COMMIT;