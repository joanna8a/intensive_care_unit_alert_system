package com.medical.alerts.service;

import com.medical.alerts.model.Patient;
import com.medical.alerts.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {
    
    private final PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        log.info("Retrieving all patients");
        return patientRepository.findAll();
    }

    public List<Patient> getActivePatients() {
        log.info("Retrieving active patients");
        return patientRepository.findActivePatients();
    }

    public Optional<Patient> getPatientById(String id) {
        log.debug("Retrieving patient by ID: {}", id);
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientByMedicalRecordNumber(String medicalRecordNumber) {
        log.debug("Retrieving patient by MRN: {}", medicalRecordNumber);
        return patientRepository.findByMedicalRecordNumber(medicalRecordNumber);
    }

    public Patient createPatient(Patient patient) {
        log.info("Creating new patient with MRN: {}", patient.getMedicalRecordNumber());
        
        // Validate required fields
        if (patient.getMedicalRecordNumber() == null || patient.getMedicalRecordNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Medical Record Number is required");
        }
        
        if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        
        if (patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        
        if (patient.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        
        if (patient.getGender() == null) {
            throw new IllegalArgumentException("Gender is required");
        }
        
        // Check for duplicate MRN
        if (patientRepository.existsByMedicalRecordNumber(patient.getMedicalRecordNumber())) {
            throw new IllegalArgumentException("Patient with MRN already exists: " + patient.getMedicalRecordNumber());
        }
        
        // Set default values if not provided
        if (patient.getConditionType() == null) {
            patient.setConditionType("ADULT_RESTING");
        }
        
        if (patient.getStatus() == null) {
            patient.setStatus(Patient.PatientStatus.ACTIVE);
        }
        
        Patient savedPatient = patientRepository.save(patient);
        log.info("Successfully created patient with ID: {}", savedPatient.getId());
        
        return savedPatient;
    }

    public Patient updatePatient(String id, Patient patientDetails) {
        log.info("Updating patient with ID: {}", id);
        
        return patientRepository.findById(id)
            .map(patient -> {
                if (patientDetails.getFirstName() != null) {
                    patient.setFirstName(patientDetails.getFirstName());
                }
                if (patientDetails.getLastName() != null) {
                    patient.setLastName(patientDetails.getLastName());
                }
                if (patientDetails.getDateOfBirth() != null) {
                    patient.setDateOfBirth(patientDetails.getDateOfBirth());
                }
                if (patientDetails.getGender() != null) {
                    patient.setGender(patientDetails.getGender());
                }
                if (patientDetails.getConditionType() != null) {
                    patient.setConditionType(patientDetails.getConditionType());
                }
                if (patientDetails.getRoomNumber() != null) {
                    patient.setRoomNumber(patientDetails.getRoomNumber());
                }
                if (patientDetails.getStatus() != null) {
                    patient.setStatus(patientDetails.getStatus());
                }
                
                Patient updatedPatient = patientRepository.save(patient);
                log.info("Successfully updated patient with ID: {}", id);
                return updatedPatient;
            })
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    public void deletePatient(String id) {
        log.info("Deleting patient with ID: {}", id);
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            log.info("Successfully deleted patient with ID: {}", id);
        } else {
            throw new RuntimeException("Patient not found with id: " + id);
        }
    }

    public boolean patientExists(String id) {
        return patientRepository.existsById(id);
    }
}