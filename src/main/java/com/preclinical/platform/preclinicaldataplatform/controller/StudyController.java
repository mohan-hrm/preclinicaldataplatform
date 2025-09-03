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

import com.preclinical.platform.preclinicaldataplatform.dto.CreateStudyRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.StudyStatisticsReport;
import com.preclinical.platform.preclinicaldataplatform.dto.UpdateStudyRequest;
import com.preclinical.platform.preclinicaldataplatform.entity.Study;
import com.preclinical.platform.preclinicaldataplatform.service.StudyManagementService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/studies")
@Validated
@Slf4j
public class StudyController {
    
    private final StudyManagementService studyManagementService;
    
    public StudyController(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }
    
    @GetMapping
    public ResponseEntity<List<Study>> getAllStudies() {
        List<Study> studies = studyManagementService.getActiveStudies();
        return ResponseEntity.ok(studies);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Study> getStudyById(@PathVariable Long id) {
        Study study = studyManagementService.getStudyById(id);
        return ResponseEntity.ok(study);
    }
    
    @GetMapping("/code/{studyCode}")
    public ResponseEntity<Study> getStudyByCode(@PathVariable String studyCode) {
        Study study = studyManagementService.getStudyByCode(studyCode);
        return ResponseEntity.ok(study);
    }
    
    @PostMapping
    public ResponseEntity<Study> createStudy(@Valid @RequestBody CreateStudyRequest request) {
        Study study = studyManagementService.createStudy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(study);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Study> updateStudy(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateStudyRequest request) {
        Study study = studyManagementService.updateStudy(id, request);
        return ResponseEntity.ok(study);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStudyStatus(
            @PathVariable Long id, 
            @RequestParam Study.StudyStatus status) {
        studyManagementService.updateStudyStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/statistics")
    public ResponseEntity<StudyStatisticsReport> getStudyStatistics(@PathVariable Long id) {
        StudyStatisticsReport report = studyManagementService.generateStudyStatistics(id);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/phase/{phase}")
    public ResponseEntity<List<Study>> getStudiesByPhase(@PathVariable Study.StudyPhase phase) {
        List<Study> studies = studyManagementService.getStudiesByPhase(phase);
        return ResponseEntity.ok(studies);
    }
}
