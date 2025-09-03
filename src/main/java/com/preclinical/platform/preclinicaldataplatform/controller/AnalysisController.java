package com.preclinical.platform.preclinicaldataplatform.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.preclinical.platform.preclinicaldataplatform.dto.AnalysisReport;
import com.preclinical.platform.preclinicaldataplatform.dto.EfficacyAnalysisRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.SafetyAnalysisReport;
import com.preclinical.platform.preclinicaldataplatform.entity.EfficacyMeasurement;
import com.preclinical.platform.preclinicaldataplatform.service.DataAnalysisService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/analysis")
@Validated
@Slf4j
public class AnalysisController {
    
    private final DataAnalysisService dataAnalysisService;
    
    public AnalysisController(DataAnalysisService dataAnalysisService) {
        this.dataAnalysisService = dataAnalysisService;
    }
    
    @GetMapping("/study/{studyId}/safety")
    public ResponseEntity<SafetyAnalysisReport> getStudySafetyAnalysis(@PathVariable Long studyId) {
        SafetyAnalysisReport report = dataAnalysisService.generateSafetyAnalysis(studyId);
        return ResponseEntity.ok(report);
    }
    
    @PostMapping("/efficacy")
    public ResponseEntity<AnalysisReport> analyzeEfficacy(@Valid @RequestBody EfficacyAnalysisRequest request) {
        AnalysisReport report = dataAnalysisService.analyzeEfficacy(request);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/study/{studyId}/enrollment-trends")
    public ResponseEntity<Map<String, Object>> getEnrollmentTrends(@PathVariable Long studyId) {
        Map<String, Object> trends = dataAnalysisService.getEnrollmentTrends(studyId);
        return ResponseEntity.ok(trends);
    }
    
    @GetMapping("/adverse-events/summary")
    public ResponseEntity<Map<String, Object>> getAdverseEventsSummary(
            @RequestParam(required = false) Long studyId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        Map<String, Object> summary = dataAnalysisService.getAdverseEventsSummary(studyId, startDate, endDate);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/measurements/{patientId}/trend")
    public ResponseEntity<Map<String, Object>> getPatientMeasurementTrends(
            @PathVariable Long patientId,
            @RequestParam EfficacyMeasurement.MeasurementType type) {
        Map<String, Object> trends = dataAnalysisService.getPatientMeasurementTrends(patientId, type);
        return ResponseEntity.ok(trends);
    }
}