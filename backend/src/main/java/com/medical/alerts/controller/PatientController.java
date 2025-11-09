package com.medical.alerts.controller;

import com.medical.alerts.model.Patient;
import com.medical.alerts.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    
    private final PatientService patientService;
    
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Patient>> getActivePatients() {
        return ResponseEntity.ok(patientService.getActivePatients());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        return patientService.getPatientById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/mrn/{medicalRecordNumber}")
    public ResponseEntity<Patient> getPatientByMrn(@PathVariable String medicalRecordNumber) {
        return patientService.getPatientByMedicalRecordNumber(medicalRecordNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}