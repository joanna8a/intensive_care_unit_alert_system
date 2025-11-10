package com.medical.alerts.controller;

import com.medical.alerts.model.PatientVitalSigns;
import com.medical.alerts.model.dto.VitalSignsRequest;
import com.medical.alerts.service.VitalSignsService;
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
@RequestMapping("/api/vitals")
@RequiredArgsConstructor
@Tag(name = "Vital Signs", description = "APIs for managing patient vital signs data")
public class VitalSignsController {
    
    private final VitalSignsService vitalSignsService;

    @Operation(
        summary = "Submit vital signs",
        description = "Submit new vital signs data for a patient. This will trigger medical rule evaluation."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vital signs submitted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid vital signs data"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PostMapping("/{patientId}")
    public ResponseEntity<PatientVitalSigns> submitVitalSigns(
            @Parameter(description = "Patient ID", example = "PT-001", required = true)
            @PathVariable String patientId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Vital signs data",
                required = true,
                content = @Content(schema = @Schema(implementation = VitalSignsRequest.class))
            )
            @RequestBody VitalSignsRequest vitalSignsRequest) {
        vitalSignsRequest.setPatientId(patientId);
        PatientVitalSigns savedVitalSigns = vitalSignsService.submitVitalSigns(vitalSignsRequest);
        return ResponseEntity.ok(savedVitalSigns);
    }

    @Operation(
        summary = "Get patient vital signs",
        description = "Retrieve vital signs history for a specific patient"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved vital signs"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientVitalSigns>> getPatientVitalSigns(
            @Parameter(description = "Patient ID", example = "PT-001", required = true)
            @PathVariable String patientId) {
        return ResponseEntity.ok(vitalSignsService.getPatientVitalSigns(patientId));
    }

    @Operation(
        summary = "Get recent vital signs",
        description = "Retrieve recent vital signs for a patient (last 24 hours by default)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved recent vital signs"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patient/{patientId}/recent")
    public ResponseEntity<List<PatientVitalSigns>> getRecentVitalSigns(
            @Parameter(description = "Patient ID", example = "PT-001", required = true)
            @PathVariable String patientId,
            @Parameter(description = "Hours to look back", example = "24")
            @RequestParam(defaultValue = "24") int hours) {
        return ResponseEntity.ok(vitalSignsService.getRecentVitalSigns(patientId, hours));
    }

    @Operation(
        summary = "Simulate IoT device data",
        description = "Simulate IoT device sending vital signs data (for testing)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "IoT data simulated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid simulation data")
    })
    @PostMapping("/simulate/iot")
    public ResponseEntity<String> simulateIotData(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "IoT simulation data",
                required = true,
                content = @Content(schema = @Schema(implementation = VitalSignsRequest.class))
            )
            @RequestBody VitalSignsRequest iotData) {
        vitalSignsService.simulateIotData(iotData);
        return ResponseEntity.ok("IoT data simulation completed");
    }
}