package com.preclinical.platform.preclinicaldataplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.preclinical.platform.preclinicaldataplatform.dto.CreateAdverseEventRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.CreateEfficacyMeasurementRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.CreatePatientRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.UpdatePatientRequest;
import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;
import com.preclinical.platform.preclinicaldataplatform.entity.EfficacyMeasurement;
import com.preclinical.platform.preclinicaldataplatform.entity.Patient;
import com.preclinical.platform.preclinicaldataplatform.service.EfficacyMeasurementService;
import com.preclinical.platform.preclinicaldataplatform.service.StudyManagementService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/patients")
@Validated
@Slf4j
public class PatientController {
    
    private final StudyManagementService studyManagementService;
    private final EfficacyMeasurementService efficacyMeasurementService;
    
    public PatientController(StudyManagementService studyManagementService,
                           EfficacyMeasurementService efficacyMeasurementService) {
        this.studyManagementService = studyManagementService;
        this.efficacyMeasurementService = efficacyMeasurementService;
    }
    
    @PostMapping("/enroll/{studyId}")
    public ResponseEntity<Patient> enrollPatient(
            @PathVariable Long studyId,
            @Valid @RequestBody CreatePatientRequest request) {
        Patient patient = studyManagementService.enrollPatient(studyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = studyManagementService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }
    
    @GetMapping("/code/{patientCode}")
    public ResponseEntity<Patient> getPatientByCode(@PathVariable String patientCode) {
        Patient patient = studyManagementService.getPatientByCode(patientCode);
        return ResponseEntity.ok(patient);
    }
    
    @GetMapping("/study/{studyId}")
    public ResponseEntity<List<Patient>> getPatientsByStudy(@PathVariable Long studyId) {
        List<Patient> patients = studyManagementService.getPatientsByStudy(studyId);
        return ResponseEntity.ok(patients);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatientRequest request) {
        Patient patient = studyManagementService.updatePatient(id, request);
        return ResponseEntity.ok(patient);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updatePatientStatus(
            @PathVariable Long id,
            @RequestParam Patient.PatientStatus status) {
        studyManagementService.updatePatientStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/adverse-events")
    public ResponseEntity<AdverseEvent> recordAdverseEvent(
            @PathVariable Long id,
            @Valid @RequestBody CreateAdverseEventRequest request) {
        AdverseEvent adverseEvent = studyManagementService.recordAdverseEvent(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(adverseEvent);
    }
    
    @GetMapping("/{id}/adverse-events")
    public ResponseEntity<List<AdverseEvent>> getPatientAdverseEvents(@PathVariable Long id) {
        List<AdverseEvent> events = studyManagementService.getPatientAdverseEvents(id);
        return ResponseEntity.ok(events);
    }
    
    @PostMapping("/{id}/measurements")
    public ResponseEntity<EfficacyMeasurement> recordMeasurement(
            @PathVariable Long id,
            @Valid @RequestBody CreateEfficacyMeasurementRequest request) {
        EfficacyMeasurement measurement = efficacyMeasurementService.recordMeasurement(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(measurement);
    }
    
    @GetMapping("/{id}/measurements")
    public ResponseEntity<List<EfficacyMeasurement>> getPatientMeasurements(@PathVariable Long id) {
        List<EfficacyMeasurement> measurements = efficacyMeasurementService.getPatientMeasurements(id);
        return ResponseEntity.ok(measurements);
    }
}