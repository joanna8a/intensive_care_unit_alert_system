package com.medical.alerts.strategy;

import com.medical.alerts.model.MedicalAlert;
import com.medical.alerts.model.PatientVitalSigns;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
public class OxygenSaturationRule implements MedicalRuleStrategy {
    
    private static final BigDecimal CRITICAL_THRESHOLD = new BigDecimal("92");
    private static final BigDecimal WARNING_THRESHOLD = new BigDecimal("94");
    
    @Override
    public Optional<MedicalAlert> evaluate(PatientVitalSigns vitalSigns, String patientCondition) {
        if (vitalSigns.getOxygenSaturation() == null) {
            return Optional.empty();
        }
        
        BigDecimal oxygenSaturation = vitalSigns.getOxygenSaturation();
        MedicalAlert.AlertSeverity severity = null;
        
        if (oxygenSaturation.compareTo(CRITICAL_THRESHOLD) < 0) {
            severity = MedicalAlert.AlertSeverity.CRITICAL;
            log.warn("Critical oxygen saturation detected: {}% for patient {}", oxygenSaturation, vitalSigns.getPatientId());
        } else if (oxygenSaturation.compareTo(WARNING_THRESHOLD) < 0) {
            severity = MedicalAlert.AlertSeverity.WARNING;
            log.info("Warning oxygen saturation detected: {}% for patient {}", oxygenSaturation, vitalSigns.getPatientId());
        }
        
        if (severity != null) {
            MedicalAlert alert = new MedicalAlert();
            alert.setPatientId(vitalSigns.getPatientId());
            alert.setSeverity(severity);
            alert.setAlertType("OXYGEN_SATURATION");
            alert.setMessageKey("alert." + severity.name().toLowerCase() + ".oxygen.saturation");
            alert.setRequiresAcknowledgment(severity == MedicalAlert.AlertSeverity.CRITICAL);
            return Optional.of(alert);
        }
        
        return Optional.empty();
    }
    
    @Override
    public boolean supports(String alertType) {
        return "OXYGEN_SATURATION".equals(alertType);
    }
    
    @Override
    public int getPriority() {
        return 10; // High priority - oxygen is critical
    }
    
    @Override
    public String getDescription() {
        return "Monitors oxygen saturation levels for critical and warning thresholds";
    }
}