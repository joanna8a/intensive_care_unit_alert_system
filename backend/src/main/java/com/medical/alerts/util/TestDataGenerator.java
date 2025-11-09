package com.medical.alerts.util;

import com.medical.alerts.model.Patient;
import com.medical.alerts.model.dto.VitalSignsRequest;
import com.medical.alerts.model.PatientVitalSigns;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestDataGenerator {
    
    private static final Random random = new Random();
    
    public static List<Patient> generateSamplePatients() {
        return Arrays.asList(
            createPatient("PT-001", "MRN-2024-001", "John", "Doe", LocalDate.of(1960, 5, 15), Patient.Gender.MALE),
            createPatient("PT-002", "MRN-2024-002", "Maria", "Garcia", LocalDate.of(1955, 8, 22), Patient.Gender.FEMALE),
            createPatient("PT-003", "MRN-2024-003", "Robert", "Johnson", LocalDate.of(1972, 12, 10), Patient.Gender.MALE),
            createPatient("PT-004", "MRN-2024-004", "Susan", "Williams", LocalDate.of(1948, 3, 30), Patient.Gender.FEMALE),
            createPatient("PT-005", "MRN-2024-005", "David", "Miller", LocalDate.of(1985, 7, 14), Patient.Gender.MALE)
        );
    }
    
    private static Patient createPatient(String id, String mrn, String firstName, String lastName, 
                                       LocalDate dob, Patient.Gender gender) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setMedicalRecordNumber(mrn);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDateOfBirth(dob);
        patient.setGender(gender);
        patient.setConditionType("ADULT_RESTING");
        patient.setRoomNumber("30" + random.nextInt(10) + "A");
        return patient;
    }
    
    public static VitalSignsRequest generateNormalVitalSigns(String patientId) {
        VitalSignsRequest request = new VitalSignsRequest();
        request.setPatientId(patientId);
        request.setHeartRate(new BigDecimal("75.0"));
        request.setOxygenSaturation(new BigDecimal("98.5"));
        request.setSystolicBP(new BigDecimal("120.0"));
        request.setDiastolicBP(new BigDecimal("80.0"));
        request.setTemperature(new BigDecimal("36.8"));
        request.setRespiratoryRate(new BigDecimal("16.0"));
        return request;
    }
    
    public static VitalSignsRequest generateCriticalVitalSigns(String patientId) {
        VitalSignsRequest request = new VitalSignsRequest();
        request.setPatientId(patientId);
        request.setHeartRate(new BigDecimal("135.0")); // Critical high
        request.setOxygenSaturation(new BigDecimal("89.5")); // Critical low
        request.setSystolicBP(new BigDecimal("185.0")); // Critical high
        request.setDiastolicBP(new BigDecimal("55.0")); // Critical low
        request.setTemperature(new BigDecimal("39.8")); // Critical high
        request.setRespiratoryRate(new BigDecimal("28.0")); // Critical high
        return request;
    }
    
    public static VitalSignsRequest generateWarningVitalSigns(String patientId) {
        VitalSignsRequest request = new VitalSignsRequest();
        request.setPatientId(patientId);
        request.setHeartRate(new BigDecimal("105.0")); // Warning high
        request.setOxygenSaturation(new BigDecimal("93.5")); // Warning low
        request.setSystolicBP(new BigDecimal("145.0")); // Warning high
        request.setDiastolicBP(new BigDecimal("95.0")); // Warning high
        request.setTemperature(new BigDecimal("37.8")); // Warning high
        request.setRespiratoryRate(new BigDecimal("22.0")); // Warning high
        return request;
    }
}