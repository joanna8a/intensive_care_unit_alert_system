package com.medical.alerts.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "patient_vital_signs")
public class PatientVitalSigns {
    
    @Id
    private String id = UUID.randomUUID().toString();
    
    @Column(name = "patient_id", nullable = false)
    private String patientId;
    
    @Column(name = "heart_rate", precision = 5, scale = 2)
    private BigDecimal heartRate;
    
    @Column(name = "oxygen_saturation", precision = 5, scale = 2)
    private BigDecimal oxygenSaturation;
    
    @Column(name = "systolic_bp", precision = 5, scale = 2)
    private BigDecimal systolicBP;
    
    @Column(name = "diastolic_bp", precision = 5, scale = 2)
    private BigDecimal diastolicBP;
    
    @Column(precision = 4, scale = 2)
    private BigDecimal temperature;
    
    @Column(name = "respiratory_rate", precision = 5, scale = 2)
    private BigDecimal respiratoryRate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DataSource source = DataSource.MONITOR;
    
    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum DataSource {
        MANUAL, MONITOR, IOT_DEVICE
    }
}