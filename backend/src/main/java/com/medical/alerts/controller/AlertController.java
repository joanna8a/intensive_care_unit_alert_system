package com.medical.alerts.controller;

import com.medical.alerts.model.MedicalAlert;
import com.medical.alerts.service.MedicalAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    
    private final MedicalAlertService alertService;
    
    @GetMapping
    public ResponseEntity<List<MedicalAlert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<MedicalAlert>> getActiveAlerts() {
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalAlert>> getPatientAlerts(@PathVariable String patientId) {
        return ResponseEntity.ok(alertService.getPatientAlerts(patientId));
    }
    
    @PutMapping("/{alertId}/acknowledge")
    public ResponseEntity<MedicalAlert> acknowledgeAlert(
            @PathVariable String alertId,
            @RequestParam String acknowledgedBy) {
        try {
            MedicalAlert acknowledgedAlert = alertService.acknowledgeAlert(alertId, acknowledgedBy);
            return ResponseEntity.ok(acknowledgedAlert);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/stats/critical-count")
    public ResponseEntity<Long> getCriticalAlertsCount() {
        return ResponseEntity.ok(alertService.getActiveCriticalAlertsCount());
    }
}