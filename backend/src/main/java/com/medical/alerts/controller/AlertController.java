package com.medical.alerts.controller;

import com.medical.alerts.model.MedicalAlert;
import com.medical.alerts.service.MedicalAlertService;
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
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Tag(name = "Alert Management", description = "APIs for managing medical alerts and notifications")
public class AlertController {
    
    private final MedicalAlertService alertService;

    @Operation(
        summary = "Get all alerts",
        description = "Retrieve all medical alerts in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved alerts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<MedicalAlert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }

    @Operation(
        summary = "Get active alerts",
        description = "Retrieve only active medical alerts (not acknowledged or resolved)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved active alerts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<List<MedicalAlert>> getActiveAlerts() {
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }

    @Operation(
        summary = "Get patient alerts",
        description = "Retrieve all alerts for a specific patient"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved patient alerts"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalAlert>> getPatientAlerts(
            @Parameter(description = "Patient ID", example = "PT-001", required = true)
            @PathVariable String patientId) {
        return ResponseEntity.ok(alertService.getPatientAlerts(patientId));
    }

    @Operation(
        summary = "Acknowledge alert",
        description = "Mark a medical alert as acknowledged by medical staff"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alert acknowledged successfully"),
        @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    @PutMapping("/{alertId}/acknowledge")
    public ResponseEntity<MedicalAlert> acknowledgeAlert(
            @Parameter(description = "Alert ID", example = "ALERT-001", required = true)
            @PathVariable String alertId,
            @Parameter(description = "Staff member who acknowledged", example = "dr.smith", required = true)
            @RequestParam String acknowledgedBy) {
        try {
            MedicalAlert acknowledgedAlert = alertService.acknowledgeAlert(alertId, acknowledgedBy);
            return ResponseEntity.ok(acknowledgedAlert);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Get critical alerts count",
        description = "Get the count of active critical alerts in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/stats/critical-count")
    public ResponseEntity<Long> getCriticalAlertsCount() {
        return ResponseEntity.ok(alertService.getActiveCriticalAlertsCount());
    }

    @Operation(
        summary = "Get alert by ID",
        description = "Retrieve a specific medical alert by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alert found"),
        @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    @GetMapping("/{alertId}")
    public ResponseEntity<MedicalAlert> getAlertById(
            @Parameter(description = "Alert ID", example = "ALERT-001", required = true)
            @PathVariable String alertId) {
        return alertService.getAlertById(alertId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}