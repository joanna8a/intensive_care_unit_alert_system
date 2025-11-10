package com.medical.alerts.repository;

import com.medical.alerts.model.MedicalAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicalAlertRepository extends JpaRepository<MedicalAlert, String> {
    
    List<MedicalAlert> findByPatientId(String patientId);
    
    List<MedicalAlert> findByStatus(MedicalAlert.AlertStatus status);
    
    @Query("SELECT a FROM MedicalAlert a WHERE a.status = 'ACTIVE' ORDER BY a.triggeredAt DESC")
    List<MedicalAlert> findActiveAlerts();
    
    List<MedicalAlert> findByPatientIdAndStatus(String patientId, MedicalAlert.AlertStatus status);
    
    @Query("SELECT COUNT(a) FROM MedicalAlert a WHERE a.status = 'ACTIVE' AND a.severity = 'CRITICAL'")
    long countActiveCriticalAlerts();
}