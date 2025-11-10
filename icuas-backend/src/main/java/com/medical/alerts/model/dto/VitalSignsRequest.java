package com.medical.alerts.model.dto;

import com.medical.alerts.model.PatientVitalSigns;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VitalSignsRequest {
    private String patientId;
    private BigDecimal heartRate;
    private BigDecimal oxygenSaturation;
    private BigDecimal systolicBP;
    private BigDecimal diastolicBP;
    private BigDecimal temperature;
    private BigDecimal respiratoryRate;
    private PatientVitalSigns.DataSource source = PatientVitalSigns.DataSource.MONITOR;
    
    public PatientVitalSigns toEntity() {
        PatientVitalSigns vitalSigns = new PatientVitalSigns();
        vitalSigns.setPatientId(this.patientId);
        vitalSigns.setHeartRate(this.heartRate);
        vitalSigns.setOxygenSaturation(this.oxygenSaturation);
        vitalSigns.setSystolicBP(this.systolicBP);
        vitalSigns.setDiastolicBP(this.diastolicBP);
        vitalSigns.setTemperature(this.temperature);
        vitalSigns.setRespiratoryRate(this.respiratoryRate);
        vitalSigns.setSource(this.source);
        return vitalSigns;
    }
}