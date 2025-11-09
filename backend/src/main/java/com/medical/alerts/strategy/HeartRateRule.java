package com.medical.alerts.strategy;

import com.medical.alerts.model.MedicalAlert;
import com.medical.alerts.model.PatientVitalSigns;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
public class HeartRateRule implements MedicalRuleStrategy {
    
    private static final BigDecimal CRITICAL_HIGH_THRESHOLD = new BigDecimal("130");
    private static final BigDecimal CRITICAL_LOW_THRESHOLD = new BigDecimal("50");
    private static final BigDecimal WARNING_HIGH_THRESHOLD = new BigDecimal("110");
    private static final BigDecimal WARNING_LOW_THRESHOLD = new BigDecimal("60");
    
    @Override
    public Optional<MedicalAlert> evaluate(PatientVitalSigns vitalSigns, String patientCondition) {
        if (vitalSigns.getHeartRate() == null) {
            return Optional.empty();
        }
        
        BigDecimal heartRate = vitalSigns.getHeartRate();
        MedicalAlert.AlertSeverity severity = null;
        String alertType = "HEART_RATE";
        
        if (heartRate.compareTo(CRITICAL_HIGH_THRESHOLD) > 0 || heartRate.compareTo(CRITICAL_LOW_THRESHOLD) < 0) {
            severity = MedicalAlert.AlertSeverity.CRITICAL;
            log.warn("Critical heart rate detected: {} bpm for patient {}", heartRate, vitalSigns.getPatientId());
        } else if (heartRate.compareTo(WARNING_HIGH_THRESHOLD) > 0 || heartRate.compareTo(WARNING_LOW_THRESHOLD) < 0) {
            severity = MedicalAlert.AlertSeverity.WARNING;
            log.info("Warning heart rate detected: {} bpm for patient {}", heartRate, vitalSigns.getPatientId());
        }
        
        if (severity != null) {
            MedicalAlert alert = new MedicalAlert();
            alert.setPatientId(vitalSigns.getPatientId());
            alert.setSeverity(severity);
            alert.setAlertType(alertType);
            alert.setMessageKey("alert." + severity.name().toLowerCase() + ".heart_rate." + 
                (heartRate.compareTo(new BigDecimal("100")) > 0 ? "high" : "low"));
            alert.setRequiresAcknowledgment(severity == MedicalAlert.AlertSeverity.CRITICAL);
            return Optional.of(alert);
        }
        
        return Optional.empty();
    }
    
    @Override
    public boolean supports(String alertType) {
        return "HEART_RATE".equals(alertType);
    }
    
    @Override
    public int getPriority() {
        return 9; // High priority - heart rate is critical
    }
    
    @Override
    public String getDescription() {
        return "Monitors heart rate for tachycardia and bradycardia conditions";
    }
}