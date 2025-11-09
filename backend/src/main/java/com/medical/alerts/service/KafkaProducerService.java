package com.medical.alerts.service;

import com.medical.alerts.model.MedicalAlert;
import com.medical.alerts.model.PatientVitalSigns;
import com.medical.alerts.model.dto.VitalSignsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String VITAL_SIGNS_TOPIC = "vital-signs-topic";
    private static final String MEDICAL_ALERTS_TOPIC = "medical-alerts-topic";
    private static final String IOT_DEVICE_TOPIC = "iot-device-topic";
    
    public void sendVitalSigns(PatientVitalSigns vitalSigns) {
        try {
            kafkaTemplate.send(VITAL_SIGNS_TOPIC, vitalSigns.getPatientId(), vitalSigns);
            log.debug("Sent vital signs to Kafka for patient: {}", vitalSigns.getPatientId());
        } catch (Exception e) {
            log.error("Failed to send vital signs to Kafka for patient: {}", vitalSigns.getPatientId(), e);
        }
    }
    
    public void sendMedicalAlert(MedicalAlert alert) {
        try {
            kafkaTemplate.send(MEDICAL_ALERTS_TOPIC, alert.getPatientId(), alert);
            log.info("Sent medical alert to Kafka: {} for patient: {}", alert.getAlertType(), alert.getPatientId());
        } catch (Exception e) {
            log.error("Failed to send medical alert to Kafka: {}", alert.getId(), e);
        }
    }
    
    public void sendIotDeviceData(VitalSignsRequest iotData) {
        try {
            kafkaTemplate.send(IOT_DEVICE_TOPIC, iotData.getPatientId(), iotData);
            log.debug("Sent IoT device data to Kafka for patient: {}", iotData.getPatientId());
        } catch (Exception e) {
            log.error("Failed to send IoT device data to Kafka for patient: {}", iotData.getPatientId(), e);
        }
    }
}