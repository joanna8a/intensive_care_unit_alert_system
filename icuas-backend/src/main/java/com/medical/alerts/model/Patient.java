package com.medical.alerts.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "patients")
public class Patient {
    
    @Id
    private String id = UUID.randomUUID().toString();
    
    @Column(name = "medical_record_number", unique = true, nullable = false)
    private String medicalRecordNumber;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    
    @Column(name = "condition_type", nullable = false)
    private String conditionType = "ADULT_RESTING";
    
    @Column(name = "admission_date")
    private LocalDateTime admissionDate;
    
    @Column(name = "room_number")
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatientStatus status = PatientStatus.ACTIVE;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    public enum PatientStatus {
        ACTIVE, DISCHARGED, TRANSFERRED
    }
}