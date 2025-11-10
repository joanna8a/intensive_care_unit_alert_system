package com.medical.alerts.model.dto;

import com.medical.alerts.model.MedicalAlert;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlertResponse {
    private String id;
    private String patientId;
    private MedicalAlert.AlertSeverity severity;
    private String alertType;
    private String message;
    private LocalDateTime triggeredAt;
    private boolean requiresAcknowledgment;
    private MedicalAlert.AlertStatus status;
    
    public static AlertResponse fromEntity(MedicalAlert alert) {
        AlertResponse response = new AlertResponse();
        response.setId(alert.getId());
        response.setPatientId(alert.getPatientId());
        response.setSeverity(alert.getSeverity());
        response.setAlertType(alert.getAlertType());
        response.setMessage(alert.getMessageKey()); // You can localize this
        response.setTriggeredAt(alert.getTriggeredAt());
        response.setRequiresAcknowledgment(alert.isRequiresAcknowledgment());
        response.setStatus(alert.getStatus());
        return response;
    }
}