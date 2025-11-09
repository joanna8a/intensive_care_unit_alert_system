package com.medical.alerts.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "medical_alerts")
public class MedicalAlert {
    
    @Id
    private String id = UUID.randomUUID().toString();
    
    @Column(name = "patient_id", nullable = false)
    private String patientId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;
    
    @Column(name = "alert_type", nullable = false)
    private String alertType;
    
    @Column(name = "message_key", nullable = false)
    private String messageKey;
    
    @Column(name = "triggered_at", nullable = false)
    private LocalDateTime triggeredAt = LocalDateTime.now();
    
    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;
    
    @Column(name = "acknowledged_by")
    private String acknowledgedBy;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status = AlertStatus.ACTIVE;
    
    @Column(name = "requires_acknowledgment", nullable = false)
    private boolean requiresAcknowledgment = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public enum AlertSeverity {
        CRITICAL, WARNING, INFO
    }
    
    public enum AlertStatus {
        ACTIVE, ACKNOWLEDGED, RESOLVED
    }
}