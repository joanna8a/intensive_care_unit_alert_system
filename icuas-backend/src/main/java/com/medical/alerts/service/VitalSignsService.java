package com.medical.alerts.service;

import com.medical.alerts.model.PatientVitalSigns;
import com.medical.alerts.model.dto.VitalSignsRequest;
import com.medical.alerts.repository.VitalSignsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VitalSignsService {
    
    private final VitalSignsRepository vitalSignsRepository;
    private final MedicalAlertService medicalAlertService;
    private final PatientService patientService;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public PatientVitalSigns submitVitalSigns(VitalSignsRequest request) {
        log.info("Submitting vital signs for patient: {}", request.getPatientId());
        
        // Validate patient exists
        if (!patientService.patientExists(request.getPatientId())) {
            throw new RuntimeException("Patient not found: " + request.getPatientId());
        }

        // Validate vital signs data
        validateVitalSigns(request);

        PatientVitalSigns vitalSigns = request.toEntity();
        PatientVitalSigns savedVitalSigns = vitalSignsRepository.save(vitalSigns);
        
        log.info("Vital signs submitted successfully for patient: {}", request.getPatientId());
        
        // Send to Kafka for stream processing
        kafkaProducerService.sendVitalSigns(savedVitalSigns);
        
        // Evaluate for medical alerts
        String patientCondition = getPatientCondition(request.getPatientId());
        medicalAlertService.evaluateVitalSigns(savedVitalSigns, patientCondition);
        
        return savedVitalSigns;
    }

    public List<PatientVitalSigns> getPatientVitalSigns(String patientId) {
        log.debug("Retrieving vital signs for patient: {}", patientId);
        return vitalSignsRepository.findByPatientIdOrderByTimestampDesc(patientId);
    }

    public List<PatientVitalSigns> getRecentVitalSigns(String patientId, int hours) {
        log.debug("Retrieving recent vital signs for patient: {} (last {} hours)", patientId, hours);
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return vitalSignsRepository.findRecentVitalSigns(patientId, since);
    }

    public List<PatientVitalSigns> getLatestVitalSigns(String patientId, int limit) {
        log.debug("Retrieving latest {} vital signs for patient: {}", limit, patientId);
        Pageable pageable = PageRequest.of(0, limit);
        return vitalSignsRepository.findLatestVitalSigns(patientId, pageable);
    }

    public void simulateIotData(VitalSignsRequest iotData) {
        log.info("Simulating IoT data for patient: {}", iotData.getPatientId());
        
        // Validate patient exists
        if (!patientService.patientExists(iotData.getPatientId())) {
            throw new RuntimeException("Patient not found: " + iotData.getPatientId());
        }

        // Send IoT data to Kafka for processing
        kafkaProducerService.sendIotDeviceData(iotData);
        log.info("IoT data simulation completed for patient: {}", iotData.getPatientId());
    }

    private void validateVitalSigns(VitalSignsRequest request) {
        if (request.getHeartRate() != null && request.getHeartRate().compareTo(new BigDecimal("30")) < 0) {
            throw new IllegalArgumentException("Heart rate cannot be less than 30 bpm");
        }
        
        if (request.getHeartRate() != null && request.getHeartRate().compareTo(new BigDecimal("250")) > 0) {
            throw new IllegalArgumentException("Heart rate cannot be greater than 250 bpm");
        }
        
        if (request.getOxygenSaturation() != null && 
            (request.getOxygenSaturation().compareTo(new BigDecimal("70")) < 0 || 
             request.getOxygenSaturation().compareTo(new BigDecimal("100")) > 0)) {
            throw new IllegalArgumentException("Oxygen saturation must be between 70% and 100%");
        }
        
        if (request.getTemperature() != null && 
            (request.getTemperature().compareTo(new BigDecimal("32")) < 0 || 
             request.getTemperature().compareTo(new BigDecimal("45")) > 0)) {
            throw new IllegalArgumentException("Temperature must be between 32°C and 45°C");
        }
        
        if (request.getRespiratoryRate() != null && 
            (request.getRespiratoryRate().compareTo(new BigDecimal("8")) < 0 || 
             request.getRespiratoryRate().compareTo(new BigDecimal("60")) > 0)) {
            throw new IllegalArgumentException("Respiratory rate must be between 8 and 60 breaths per minute");
        }
    }

    private String getPatientCondition(String patientId) {
        // In a real system, you would get this from the patient record
        // For now, return a default condition
        return "ADULT_RESTING";
    }

    public long getVitalSignsCount(String patientId) {
        return vitalSignsRepository.findByPatientIdOrderByTimestampDesc(patientId).size();
    }
}