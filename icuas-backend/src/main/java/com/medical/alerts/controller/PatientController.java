package com.medical.alerts.controller;

import com.medical.alerts.model.Patient;
import com.medical.alerts.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "APIs for managing patient records and information")
public class PatientController {
    
    private final PatientService patientService;

    @Operation(
        summary = "Get all patients",
        description = "Retrieve a list of all patients in the system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved patients",
            content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = Patient.class))
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @Operation(
        summary = "Get active patients",
        description = "Retrieve only patients with ACTIVE status"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved active patients"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<List<Patient>> getActivePatients() {
        return ResponseEntity.ok(patientService.getActivePatients());
    }

    @Operation(
        summary = "Get patient by ID",
        description = "Retrieve a specific patient using their unique ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient found"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(
            @Parameter(description = "Patient ID", example = "PT-001", required = true)
            @PathVariable String id) {
        return patientService.getPatientById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get patient by Medical Record Number",
        description = "Retrieve a patient using their Medical Record Number (MRN)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient found"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/mrn/{medicalRecordNumber}")
    public ResponseEntity<Patient> getPatientByMrn(
            @Parameter(description = "Medical Record Number", example = "MRN-2024-001", required = true)
            @PathVariable String medicalRecordNumber) {
        return patientService.getPatientByMedicalRecordNumber(medicalRecordNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Create new patient",
        description = "Add a new patient to the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid patient data")
    })
    @PostMapping
    public ResponseEntity<Patient> createPatient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Patient object to create",
                required = true,
                content = @Content(schema = @Schema(implementation = Patient.class))
            )
            @RequestBody Patient patient) {
        Patient savedPatient = patientService.createPatient(patient);
        return ResponseEntity.ok(savedPatient);
    }
}