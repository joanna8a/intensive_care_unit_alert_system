package com.medical.alerts.strategy;

import com.medical.alerts.model.MedicalAlert;
import com.medical.alerts.model.PatientVitalSigns;
import java.util.Optional;

public interface MedicalRuleStrategy {
    
    Optional<MedicalAlert> evaluate(PatientVitalSigns vitalSigns, String patientCondition);
    
    boolean supports(String alertType);
    
    int getPriority();
    
    String getDescription();
}