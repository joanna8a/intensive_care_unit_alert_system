package com.medical.alerts.service;

import com.medical.alerts.model.PatientVitalSigns;
import com.medical.alerts.model.dto.VitalSignsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    
    private final VitalSignsService vitalSignsService;
    private final MedicalAlertService medicalAlertService;

    @KafkaListener(
        topics = "${kafka.topics.vital-signs:vital-signs-topic}",
        groupId = "${spring.kafka.consumer.group-id:medical-alerts-group}",
        concurrency = "3"
    )
    public void consumeVitalSigns(ConsumerRecord<String, PatientVitalSigns> record, Acknowledgment ack) {
        try {
            PatientVitalSigns vitalSigns = record.value();
            String patientId = record.key();
            
            log.debug("Received vital signs from Kafka for patient: {}", patientId);
            
            // Process the vital signs - in this case, they're already saved to database
            // but we might want to do additional processing
            String patientCondition = "ADULT_RESTING"; // Get from patient service
            medicalAlertService.evaluateVitalSigns(vitalSigns, patientCondition);
            
            // Acknowledge the message
            ack.acknowledge();
            log.debug("Successfully processed vital signs for patient: {}", patientId);
            
        } catch (Exception e) {
            log.error("Error processing vital signs from Kafka: {}", e.getMessage(), e);
            // In production, you might want to send to a dead letter topic
        }
    }

    @KafkaListener(
        topics = "${kafka.topics.iot-device-data:iot-device-topic}",
        groupId = "${spring.kafka.consumer.group-id:medical-alerts-group}"
    )
    public void consumeIotDeviceData(ConsumerRecord<String, VitalSignsRequest> record, Acknowledgment ack) {
        try {
            VitalSignsRequest iotData = record.value();
            String patientId = record.key();
            
            log.info("Received IoT device data from Kafka for patient: {}", patientId);
            
            // Process IoT data - convert to vital signs and save
            PatientVitalSigns vitalSigns = iotData.toEntity();
            vitalSigns.setPatientId(patientId);
            
            // Save to database
            // Note: In a real scenario, you might use a different service method for IoT data
            String patientCondition = "ADULT_RESTING";
            medicalAlertService.evaluateVitalSigns(vitalSigns, patientCondition);
            
            // Acknowledge the message
            ack.acknowledge();
            log.info("Successfully processed IoT device data for patient: {}", patientId);
            
        } catch (Exception e) {
            log.error("Error processing IoT device data from Kafka: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(
        topics = "${kafka.topics.medical-alerts:medical-alerts-topic}",
        groupId = "${spring.kafka.consumer.group-id:medical-alerts-group}"
    )
    public void consumeMedicalAlerts(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        try {
            Object alertData = record.value();
            String patientId = record.key();
            
            log.info("Received medical alert from Kafka for patient: {}", patientId);
            
            // Process medical alert - you might want to send notifications, 
            // update dashboards, or trigger other actions
            
            // For now, just log it
            log.info("Medical alert processed for patient {}: {}", patientId, alertData);
            
            // Acknowledge the message
            ack.acknowledge();
            
        } catch (Exception e) {
            log.error("Error processing medical alert from Kafka: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(
        topics = "${kafka.topics.patient-events:patient-events-topic}",
        groupId = "${spring.kafka.consumer.group-id:medical-alerts-group}"
    )
    public void consumePatientEvents(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        try {
            Object patientEvent = record.value();
            String patientId = record.key();
            
            log.debug("Received patient event from Kafka for patient: {}", patientId);
            
            // Process patient events (admission, discharge, transfer, etc.)
            // This could trigger various system actions
            
            log.debug("Patient event processed for patient {}: {}", patientId, patientEvent);
            
            // Acknowledge the message
            ack.acknowledge();
            
        } catch (Exception e) {
            log.error("Error processing patient event from Kafka: {}", e.getMessage(), e);
        }
    }
}