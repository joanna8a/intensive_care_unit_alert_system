package com.medical.alerts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/system")
@Tag(name = "System", description = "System health and information endpoints")
public class SystemController {

    @Operation(
        summary = "Health check",
        description = "Check if the system is running and healthy"
    )
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Medical Alert System",
            "timestamp", java.time.Instant.now().toString()
        ));
    }

    @Operation(
        summary = "System info",
        description = "Get system information and version"
    )
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> systemInfo() {
        return ResponseEntity.ok(Map.of(
            "name", "Medical Alert System",
            "version", "1.0.0",
            "description", "Real-time Patient Monitoring and Alert System",
            "java.version", System.getProperty("java.version"),
            "timestamp", java.time.Instant.now().toString()
        ));
    }
}