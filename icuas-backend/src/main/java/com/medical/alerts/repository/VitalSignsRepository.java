package com.medical.alerts.repository;

import com.medical.alerts.model.PatientVitalSigns;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VitalSignsRepository extends JpaRepository<PatientVitalSigns, String> {
    
    List<PatientVitalSigns> findByPatientIdOrderByTimestampDesc(String patientId);
    
    @Query("SELECT v FROM PatientVitalSigns v WHERE v.patientId = :patientId AND v.timestamp >= :since ORDER BY v.timestamp DESC")
    List<PatientVitalSigns> findRecentVitalSigns(String patientId, LocalDateTime since);
    
    @Query("SELECT v FROM PatientVitalSigns v WHERE v.patientId = :patientId ORDER BY v.timestamp DESC")
    List<PatientVitalSigns> findLatestVitalSigns(@Param("patientId") String patientId, Pageable pageable);
}