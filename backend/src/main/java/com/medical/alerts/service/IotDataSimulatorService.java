package com.medical.alerts.service;

import com.medical.alerts.model.dto.VitalSignsRequest;
import com.medical.alerts.model.PatientVitalSigns;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class IotDataSimulatorService {
    
    private final VitalSignsService vitalSignsService;
    private final PatientService patientService;
    private final KafkaProducerService kafkaProducerService;
    
    private final Random random = new Random();
    private final List<String> samplePatientIds = List.of("PT-001", "PT-002", "PT-003", "PT-004", "PT-005");
    
    // Normal ranges for simulation
    private static final BigDecimal NORMAL_HEART_RATE_MIN = new BigDecimal("60");
    private static final BigDecimal NORMAL_HEART_RATE_MAX = new BigDecimal("100");
    private static final BigDecimal NORMAL_OXYGEN_MIN = new BigDecimal("95");
    private static final BigDecimal NORMAL_OXYGEN_MAX = new BigDecimal("100");
    private static final BigDecimal NORMAL_SYSTOLIC_MIN = new BigDecimal("100");
    private static final BigDecimal NORMAL_SYSTOLIC_MAX = new BigDecimal("140");
    private static final BigDecimal NORMAL_DIASTOLIC_MIN = new BigDecimal("60");
    private static final BigDecimal NORMAL_DIASTOLIC_MAX = new BigDecimal("90");
    private static final BigDecimal NORMAL_TEMP_MIN = new BigDecimal("36.0");
    private static final BigDecimal NORMAL_TEMP_MAX = new BigDecimal("37.5");
    private static final BigDecimal NORMAL_RESP_RATE_MIN = new BigDecimal("12");
    private static final BigDecimal NORMAL_RESP_RATE_MAX = new BigDecimal("20");

    /**
     * Simulate normal vital signs for random patients every 30 seconds
     */
    @Scheduled(fixedRate = 30000) // 30 seconds
    public void simulateNormalVitalSigns() {
        if (random.nextDouble() < 0.7) { // 70% chance to run each time
            String patientId = getRandomPatientId();
            VitalSignsRequest normalData = generateNormalVitalSigns(patientId);
            
            log.info("Simulating NORMAL vital signs for patient: {}", patientId);
            vitalSignsService.simulateIotData(normalData);
        }
    }

    /**
     * Simulate critical vital signs occasionally (5% chance every 30 seconds)
     */
    @Scheduled(fixedRate = 30000)
    public void simulateCriticalVitalSigns() {
        if (random.nextDouble() < 0.05) { // 5% chance to run each time
            String patientId = getRandomPatientId();
            VitalSignsRequest criticalData = generateCriticalVitalSigns(patientId);
            
            log.warn("Simulating CRITICAL vital signs for patient: {}", patientId);
            vitalSignsService.simulateIotData(criticalData);
        }
    }

    /**
     * Simulate warning vital signs occasionally (15% chance every 30 seconds)
     */
    @Scheduled(fixedRate = 30000)
    public void simulateWarningVitalSigns() {
        if (random.nextDouble() < 0.15) { // 15% chance to run each time
            String patientId = getRandomPatientId();
            VitalSignsRequest warningData = generateWarningVitalSigns(patientId);
            
            log.info("Simulating WARNING vital signs for patient: {}", patientId);
            vitalSignsService.simulateIotData(warningData);
        }
    }

    /**
     * Generate normal vital signs within healthy ranges
     */
    public VitalSignsRequest generateNormalVitalSigns(String patientId) {
        VitalSignsRequest request = new VitalSignsRequest();
        request.setPatientId(patientId);
        request.setHeartRate(generateRandomInRange(NORMAL_HEART_RATE_MIN, NORMAL_HEART_RATE_MAX));
        request.setOxygenSaturation(generateRandomInRange(NORMAL_OXYGEN_MIN, NORMAL_OXYGEN_MAX));
        request.setSystolicBP(generateRandomInRange(NORMAL_SYSTOLIC_MIN, NORMAL_SYSTOLIC_MAX));
        request.setDiastolicBP(generateRandomInRange(NORMAL_DIASTOLIC_MIN, NORMAL_DIASTOLIC_MAX));
        request.setTemperature(generateRandomInRange(NORMAL_TEMP_MIN, NORMAL_TEMP_MAX));
        request.setRespiratoryRate(generateRandomInRange(NORMAL_RESP_RATE_MIN, NORMAL_RESP_RATE_MAX));
        return request;
    }

    /**
     * Generate critical vital signs that will trigger alerts
     */
    public VitalSignsRequest generateCriticalVitalSigns(String patientId) {
        VitalSignsRequest request = new VitalSignsRequest();
        request.setPatientId(patientId);
        
        // Randomly choose which vital sign to make critical
        int criticalType = random.nextInt(5);
        
        switch (criticalType) {
            case 0 -> {
                // Critical low oxygen
                request.setOxygenSaturation(generateRandomInRange(new BigDecimal("85"), new BigDecimal("91")));
                request.setHeartRate(generateRandomInRange(new BigDecimal("110"), new BigDecimal("140"))); // Tachycardia
                request.setSystolicBP(generateRandomInRange(NORMAL_SYSTOLIC_MIN, NORMAL_SYSTOLIC_MAX));
                request.setDiastolicBP(generateRandomInRange(NORMAL_DIASTOLIC_MIN, NORMAL_DIASTOLIC_MAX));
                request.setTemperature(generateRandomInRange(NORMAL_TEMP_MIN, NORMAL_TEMP_MAX));
                request.setRespiratoryRate(generateRandomInRange(new BigDecimal("22"), new BigDecimal("30")));
            }
            case 1 -> {
                // Critical heart rate
                request.setHeartRate(generateRandomInRange(new BigDecimal("130"), new BigDecimal("160")));
                request.setOxygenSaturation(generateRandomInRange(new BigDecimal("92"), new BigDecimal("96")));
                request.setSystolicBP(generateRandomInRange(NORMAL_SYSTOLIC_MIN, NORMAL_SYSTOLIC_MAX));
                request.setDiastolicBP(generateRandomInRange(NORMAL_DIASTOLIC_MIN, NORMAL_DIASTOLIC_MAX));
                request.setTemperature(generateRandomInRange(NORMAL_TEMP_MIN, NORMAL_TEMP_MAX));
                request.setRespiratoryRate(generateRandomInRange(new BigDecimal("20"), new BigDecimal("28")));
            }
            case 2 -> {
                // Critical blood pressure
                request.setSystolicBP(generateRandomInRange(new BigDecimal("180"), new BigDecimal("220")));
                request.setDiastolicBP(generateRandomInRange(new BigDecimal("110"), new BigDecimal("130")));
                request.setHeartRate(generateRandomInRange(NORMAL_HEART_RATE_MIN, NORMAL_HEART_RATE_MAX));
                request.setOxygenSaturation(generateRandomInRange(NORMAL_OXYGEN_MIN, NORMAL_OXYGEN_MAX));
                request.setTemperature(generateRandomInRange(NORMAL_TEMP_MIN, NORMAL_TEMP_MAX));
                request.setRespiratoryRate(generateRandomInRange(NORMAL_RESP_RATE_MIN, NORMAL_RESP_RATE_MAX));
            }
            case 3 -> {
                // Critical temperature (fever)
                request.setTemperature(generateRandomInRange(new BigDecimal("39.5"), new BigDecimal("41.0")));
                request.setHeartRate(generateRandomInRange(new BigDecimal("100"), new BigDecimal("130")));
                request.setOxygenSaturation(generateRandomInRange(NORMAL_OXYGEN_MIN, NORMAL_OXYGEN_MAX));
                request.setSystolicBP(generateRandomInRange(NORMAL_SYSTOLIC_MIN, NORMAL_SYSTOLIC_MAX));
                request.setDiastolicBP(generateRandomInRange(NORMAL_DIASTOLIC_MIN, NORMAL_DIASTOLIC_MAX));
                request.setRespiratoryRate(generateRandomInRange(new BigDecimal("18"), new BigDecimal("25")));
            }
            case 4 -> {
                // Critical respiratory rate
                request.setRespiratoryRate(generateRandomInRange(new BigDecimal("30"), new BigDecimal("40")));
                request.setHeartRate(generateRandomInRange(new BigDecimal("110"), new BigDecimal("140")));
                request.setOxygenSaturation(generateRandomInRange(new BigDecimal("88"), new BigDecimal("94")));
                request.setSystolicBP(generateRandomInRange(NORMAL_SYSTOLIC_MIN, NORMAL_SYSTOLIC_MAX));
                request.setDiastolicBP(generateRandomInRange(NORMAL_DIASTOLIC_MIN, NORMAL_DIASTOLIC_MAX));
                request.setTemperature(generateRandomInRange(NORMAL_TEMP_MIN, NORMAL_TEMP_MAX));
            }
        }
        
        return request;
    }

    /**
     * Generate warning vital signs that are borderline
     */
    public VitalSignsRequest generateWarningVitalSigns(String patientId) {
        VitalSignsRequest request = new VitalSignsRequest();
        request.setPatientId(patientId);
        
        // Randomly choose which vital sign to make warning
        int warningType = random.nextInt(5);
        
        switch (warningType) {
            case 0 -> {
                // Warning oxygen
                request.setOxygenSaturation(generateRandomInRange(new BigDecimal("92"), new BigDecimal("94")));
                request.setHeartRate(generateRandomInRange(NORMAL_HEART_RATE_MIN, NORMAL_HEART_RATE_MAX));
                request.setSystolicBP(generateRandomInRange(NORMAL_SYSTOLIC_MIN, NORMAL_SYSTOLIC_MAX));
                request.setDiastolicBP(generateRandomInRange(NORMAL_DIASTOLIC_MIN, NORMAL_DIASTOLIC_MAX));
                request.setTemperature(generateRandomInRange(NORMAL_TEMP_MIN, NORMAL_TEMP_MAX));
                request.setRespiratoryRate(generateRandomInRange(NORMAL_RESP_RATE_MIN, NORMAL_RESP_RATE_MAX));
            }
            case 1 -> {
                // Warning heart rate
                request.setHeartRate(generateRandomInRange(new BigDecimal("101"), new BigDecimal("120")));
                request.setOxygenSaturation(generateRandomInRange(NORMAL_OXYGEN_MIN, NORMAL_OXYGEN_MAX));
                request.setSystolicBP(generateRandomInRange(NORMAL_SYSTOLIC_MIN, NORMAL_SYSTOLIC_MAX));
                request.setDiastolicBP(generateRandomInRange(NORMAL_DIASTOLIC_MIN, NORMAL_DIASTOLIC_MAX));
                request.setTemperature(generateRandomInRange(NORMAL_TEMP_MIN, NORMAL_TEMP_MAX));
                request.setRespiratoryRate(generateRandomInRange(NORMAL_RESP_RATE_MIN, NORMAL_RESP_RATE_MAX));
            }
            case 2 -> {
                // Warning blood pressure
                request.setSystolicBP(generateRandomInRange(new BigDecimal("141"), new BigDecimal("160")));
                request.setDiastolicBP(generateRandomInRange(new BigDecimal("91"), new BigDecimal("100")));
                request.setHeartRate(generateRandomInRange(NORMAL_HEART_RATE_MIN, NORMAL_HEART_RATE_MAX));
                request.setOxygenSaturation(generateRandomInRange(NORMAL_OXYGEN_MIN, NORMAL_OXYGEN_MAX));
                request.setTemperature(generateRandomInRange(NORMAL_TEMP_MIN, NORMAL_TEMP_MAX));
                request.setRespiratoryRate(generateRandomInRange(NORMAL_RESP_RATE_MIN, NORMAL_RESP_RATE_MAX));
            }
            case 3 -> {
                // Warning temperature
                request.setTemperature(generateRandomInRange(new BigDecimal("37.6"), new BigDecimal("38.5")));
                request.setHeartRate(generateRandomInRange(NORMAL_HEART_RATE_MIN, NORMAL_HEART_RATE_MAX));
                request.setOxygenSaturation(generateRandomInRange(NORMAL_OXYGEN_MIN, NORMAL_OXYGEN_MAX));
                request.setSystolicBP(generateRandomInRange(NORMAL_SYSTOLIC_MIN, NORMAL_SYSTOLIC_MAX));
                request.setDiastolicBP(generateRandomInRange(NORMAL_DIASTOLIC_MIN, NORMAL_DIASTOLIC_MAX));
                request.setRespiratoryRate(generateRandomInRange(NORMAL_RESP_RATE_MIN, NORMAL_RESP_RATE_MAX));
            }
            case 4 -> {
                // Warning respiratory rate
                request.setRespiratoryRate(generateRandomInRange(new BigDecimal("21"), new BigDecimal("25")));
                request.setHeartRate(generateRandomInRange(NORMAL_HEART_RATE_MIN, NORMAL_HEART_RATE_MAX));
                request.setOxygenSaturation(generateRandomInRange(NORMAL_OXYGEN_MIN, NORMAL_OXYGEN_MAX));
                request.setSystolicBP(generateRandomInRange(NORMAL_SYSTOLIC_MIN, NORMAL_SYSTOLIC_MAX));
                request.setDiastolicBP(generateRandomInRange(NORMAL_DIASTOLIC_MIN, NORMAL_DIASTOLIC_MAX));
                request.setTemperature(generateRandomInRange(NORMAL_TEMP_MIN, NORMAL_TEMP_MAX));
            }
        }
        
        return request;
    }

    /**
     * Generate random value within a range
     */
    private BigDecimal generateRandomInRange(BigDecimal min, BigDecimal max) {
        double range = max.doubleValue() - min.doubleValue();
        double value = min.doubleValue() + random.nextDouble() * range;
        return new BigDecimal(value).setScale(1, RoundingMode.HALF_UP);
    }

    /**
     * Get a random patient ID from the sample list
     */
    private String getRandomPatientId() {
        return samplePatientIds.get(random.nextInt(samplePatientIds.size()));
    }

    /**
     * Manual simulation method for testing
     */
    public void simulateSpecificScenario(String patientId, String scenario) {
        VitalSignsRequest data;
        
        switch (scenario.toUpperCase()) {
            case "CRITICAL_OXYGEN" -> {
                data = generateNormalVitalSigns(patientId);
                data.setOxygenSaturation(new BigDecimal("89.5"));
            }
            case "CRITICAL_HEART" -> {
                data = generateNormalVitalSigns(patientId);
                data.setHeartRate(new BigDecimal("135.0"));
            }
            case "CRITICAL_BP" -> {
                data = generateNormalVitalSigns(patientId);
                data.setSystolicBP(new BigDecimal("185.0"));
                data.setDiastolicBP(new BigDecimal("55.0"));
            }
            case "CRITICAL_TEMP" -> {
                data = generateNormalVitalSigns(patientId);
                data.setTemperature(new BigDecimal("40.2"));
            }
            case "NORMAL" -> data = generateNormalVitalSigns(patientId);
            case "WARNING" -> data = generateWarningVitalSigns(patientId);
            default -> data = generateCriticalVitalSigns(patientId);
        }
        
        log.info("Simulating {} scenario for patient: {}", scenario, patientId);
        vitalSignsService.simulateIotData(data);
    }
}