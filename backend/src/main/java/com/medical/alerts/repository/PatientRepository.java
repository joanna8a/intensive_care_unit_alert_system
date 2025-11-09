package com.medical.alerts.repository;

import com.medical.alerts.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    
    Optional<Patient> findByMedicalRecordNumber(String medicalRecordNumber);
    
    List<Patient> findByStatus(Patient.PatientStatus status);
    
    @Query("SELECT p FROM Patient p WHERE p.status = 'ACTIVE'")
    List<Patient> findActivePatients();
    
    boolean existsByMedicalRecordNumber(String medicalRecordNumber);
}