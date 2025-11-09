package com.medical.alerts.service;

import com.medical.alerts.model.MedicalAlert;
import com.medical.alerts.model.PatientVitalSigns;
import com.medical.alerts.repository.MedicalAlertRepository;
import com.medical.alerts.strategy.MedicalRuleStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalAlertService {
    
    private final MedicalAlertRepository alertRepository;
    private final List<MedicalRuleStrategy> ruleStrategies;
    private final KafkaProducerService kafkaProducerService;
    
    @Transactional
    public List<MedicalAlert> evaluateVitalSigns(PatientVitalSigns vitalSigns, String patientCondition) {
        log.debug("Evaluating vital signs for patient: {}", vitalSigns.getPatientId());
        
        List<MedicalAlert> newAlerts = ruleStrategies.stream()
            .map(strategy -> strategy.evaluate(vitalSigns, patientCondition))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
        
        if (!newAlerts.isEmpty()) {
            List<MedicalAlert> savedAlerts = alertRepository.saveAll(newAlerts);
            log.info("Generated {} new alerts for patient: {}", savedAlerts.size(), vitalSigns.getPatientId());
            
            // Send alerts to Kafka for real-time processing
            savedAlerts.forEach(kafkaProducerService::sendMedicalAlert);
            
            return savedAlerts;
        }
        
        return List.of();
    }
    
    public List<MedicalAlert> getActiveAlerts() {
        return alertRepository.findActiveAlerts();
    }
    
    public List<MedicalAlert> getPatientAlerts(String patientId) {
        return alertRepository.findByPatientId(patientId);
    }
    
    @Transactional
    public MedicalAlert acknowledgeAlert(String alertId, String acknowledgedBy) {
        return alertRepository.findById(alertId)
            .map(alert -> {
                alert.setStatus(MedicalAlert.AlertStatus.ACKNOWLEDGED);
                alert.setAcknowledgedAt(LocalDateTime.now());
                alert.setAcknowledgedBy(acknowledgedBy);
                alert.setUpdatedAt(LocalDateTime.now());
                MedicalAlert updatedAlert = alertRepository.save(alert);
                log.info("Alert {} acknowledged by {}", alertId, acknowledgedBy);
                return updatedAlert;
            })
            .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));
    }
    
    public long getActiveCriticalAlertsCount() {
        return alertRepository.countActiveCriticalAlerts();
    }
    
    public Optional<MedicalAlert> getAlertById(String alertId) {
        return alertRepository.findById(alertId);
    }
}