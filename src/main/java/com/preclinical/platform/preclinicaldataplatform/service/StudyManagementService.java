package com.preclinical.platform.preclinicaldataplatform.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.preclinical.platform.preclinicaldataplatform.dto.CreateAdverseEventRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.CreatePatientRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.CreateStudyRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.StudyStatisticsReport;
import com.preclinical.platform.preclinicaldataplatform.dto.UpdatePatientRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.UpdateStudyRequest;
import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;
import com.preclinical.platform.preclinicaldataplatform.entity.Patient;
import com.preclinical.platform.preclinicaldataplatform.entity.Study;
import com.preclinical.platform.preclinicaldataplatform.event.PatientEnrolledEvent;
import com.preclinical.platform.preclinicaldataplatform.event.PatientStatusChangedEvent;
import com.preclinical.platform.preclinicaldataplatform.event.PatientUpdatedEvent;
import com.preclinical.platform.preclinicaldataplatform.event.SeriousAdverseEventAlert;
import com.preclinical.platform.preclinicaldataplatform.event.StudyCreatedEvent;
import com.preclinical.platform.preclinicaldataplatform.event.StudyStatusChangedEvent;
import com.preclinical.platform.preclinicaldataplatform.excception.PatientNotFoundException;
import com.preclinical.platform.preclinicaldataplatform.excception.StudyNotFoundException;
import com.preclinical.platform.preclinicaldataplatform.repository.AdverseEventRepository;
import com.preclinical.platform.preclinicaldataplatform.repository.PatientRepository;
import com.preclinical.platform.preclinicaldataplatform.repository.StudyRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class StudyManagementService {
    
    private final StudyRepository studyRepository;
    private final PatientRepository patientRepository;
    private final AdverseEventRepository adverseEventRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public StudyManagementService(StudyRepository studyRepository, 
                                PatientRepository patientRepository,
                                AdverseEventRepository adverseEventRepository,
                                ApplicationEventPublisher eventPublisher) {
        this.studyRepository = studyRepository;
        this.patientRepository = patientRepository;
        this.adverseEventRepository = adverseEventRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Cacheable(value = "studies", key = "#id")
    public Study getStudyById(Long id) {
        log.debug("Fetching study with id: {}", id);
        return studyRepository.findById(id)
                .orElseThrow(() -> new StudyNotFoundException("Study not found with id: " + id));
    }
    
    @CacheEvict(value = "studies", key = "#result.id")
    public Study createStudy(CreateStudyRequest request) {
        Study study = Study.builder()
                .studyCode(request.getStudyCode())
                .title(request.getTitle())
                .objective(request.getObjective())
                .phase(request.getPhase())
                .startDate(request.getStartDate())
                .status(Study.StudyStatus.PLANNED)
                .build();
        
        Study savedStudy = studyRepository.save(study);
        eventPublisher.publishEvent(new StudyCreatedEvent(savedStudy));
        return savedStudy;
    }
    
    public Patient enrollPatient(Long studyId, CreatePatientRequest request) {
        Study study = getStudyById(studyId);
        
        if (study.getStatus() != Study.StudyStatus.ACTIVE) {
            throw new IllegalStateException("Cannot enroll patient in non-active study");
        }
        
        Patient patient = Patient.builder()
                .patientCode(request.getPatientCode())
                .age(request.getAge())
                .gender(request.getGender())
                .weight(request.getWeight())
                .height(request.getHeight())
                .medicalHistory(request.getMedicalHistory())
                .enrollmentDate(LocalDate.now())
                .status(Patient.PatientStatus.ENROLLED)
                .study(study)
                .build();
        
        Patient savedPatient = patientRepository.save(patient);
        eventPublisher.publishEvent(new PatientEnrolledEvent(savedPatient));
        return savedPatient;
    }
    
    public AdverseEvent recordAdverseEvent(Long patientId, CreateAdverseEventRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        
        AdverseEvent adverseEvent = AdverseEvent.builder()
                .eventTerm(request.getEventTerm())
                .severity(request.getSeverity())
                .causality(request.getCausality())
                .onsetDate(request.getOnsetDate())
                .description(request.getDescription())
                .serious(request.getSerious())
                .patient(patient)
                .study(patient.getStudy())
                .outcome(AdverseEvent.Outcome.UNKNOWN)
                .build();
        
        AdverseEvent savedEvent = adverseEventRepository.save(adverseEvent);
        
        // Check for serious adverse event alert
        if (savedEvent.getSerious()) {
            eventPublisher.publishEvent(new SeriousAdverseEventAlert(savedEvent));
        }
        
        return savedEvent;
    }
    
    @Retryable(value = {Exception.class}, maxAttempts = 3)
    public List<Study> getActiveStudies() {
        return studyRepository.findByStatus(Study.StudyStatus.ACTIVE);
    }
    
    public StudyStatisticsReport generateStudyStatistics(Long studyId) {
        Study study = getStudyById(studyId);
        
        long totalPatients = patientRepository.countByStudyId(studyId);
        long completedPatients = patientRepository.countCompletedPatientsByStudy(studyId);
        List<AdverseEvent> adverseEvents = adverseEventRepository.findByStudyId(studyId);
        
        return StudyStatisticsReport.builder()
                .studyId(studyId)
                .studyCode(study.getStudyCode())
                .totalPatients(totalPatients)
                .completedPatients(completedPatients)
                .totalAdverseEvents((long)adverseEvents.size())
                .seriousAdverseEvents((long)adverseEvents.stream()
                        .mapToInt(ae -> ae.getSerious() ? 1 : 0).sum())
                .build();
    }
    
 // Add these methods to your StudyManagementService class

    @Cacheable(value = "patients", key = "#id")
    public Patient getPatientById(Long id) {
        log.debug("Fetching patient with id: {}", id);
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
    }

    @Cacheable(value = "patients", key = "#patientCode")
    public Patient getPatientByCode(String patientCode) {
        log.debug("Fetching patient with code: {}", patientCode);
        return patientRepository.findByPatientCode(patientCode)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with code: " + patientCode));
    }

    public List<Patient> getPatientsByStudy(Long studyId) {
        log.debug("Fetching patients for study: {}", studyId);
        // Verify study exists
        getStudyById(studyId);
        return patientRepository.findByStudyId(studyId);
    }

    @CachePut(value = "patients", key = "#id")
    public Patient updatePatient(Long id, UpdatePatientRequest request) {
        log.debug("Updating patient with id: {}", id);
        
        Patient patient = getPatientById(id);
        
        // Update fields if provided
        if (request.getWeight() != null) {
            patient.setWeight(request.getWeight());
        }
        if (request.getHeight() != null) {
            patient.setHeight(request.getHeight());
        }
        if (request.getMedicalHistory() != null) {
            patient.setMedicalHistory(request.getMedicalHistory());
        }
        
        Patient updatedPatient = patientRepository.save(patient);
        eventPublisher.publishEvent(new PatientUpdatedEvent(updatedPatient));
        
        return updatedPatient;
    }

    @CacheEvict(value = "patients", key = "#id")
    public void updatePatientStatus(Long id, Patient.PatientStatus status) {
        log.debug("Updating patient status for id: {} to: {}", id, status);
        
        Patient patient = getPatientById(id);
        Patient.PatientStatus oldStatus = patient.getStatus();
        patient.setStatus(status);
        
        // Set completion date if patient is being completed
        if (status == Patient.PatientStatus.COMPLETED && patient.getCompletionDate() == null) {
            patient.setCompletionDate(LocalDate.now());
        }
        
        patientRepository.save(patient);
        eventPublisher.publishEvent(new PatientStatusChangedEvent(patient, oldStatus, status));
    }

    public List<AdverseEvent> getPatientAdverseEvents(Long patientId) {
        log.debug("Fetching adverse events for patient: {}", patientId);
        // Verify patient exists
        getPatientById(patientId);
        return adverseEventRepository.findByPatientId(patientId);
    }

    // Additional helper methods you might need:

    public Study getStudyByCode(String studyCode) {
        log.debug("Fetching study with code: {}", studyCode);
        return studyRepository.findByStudyCode(studyCode)
                .orElseThrow(() -> new StudyNotFoundException("Study not found with code: " + studyCode));
    }

    public List<Study> getStudiesByPhase(Study.StudyPhase phase) {
        log.debug("Fetching studies for phase: {}", phase);
        return studyRepository.findByPhaseAndStatus(phase, Study.StudyStatus.ACTIVE);
    }

    public Study updateStudy(Long id, UpdateStudyRequest request) {
        log.debug("Updating study with id: {}", id);
        
        Study study = getStudyById(id);
        
        if (request.getTitle() != null) {
            study.setTitle(request.getTitle());
        }
        if (request.getObjective() != null) {
            study.setObjective(request.getObjective());
        }
        if (request.getEndDate() != null) {
            study.setEndDate(request.getEndDate());
        }
        
        return studyRepository.save(study);
    }

    @CacheEvict(value = "studies", key = "#id")
    public void updateStudyStatus(Long id, Study.StudyStatus status) {
        log.debug("Updating study status for id: {} to: {}", id, status);
        
        Study study = getStudyById(id);
        Study.StudyStatus oldStatus = study.getStatus();
        study.setStatus(status);
        
        studyRepository.save(study);
        eventPublisher.publishEvent(new StudyStatusChangedEvent(study, oldStatus, status));
    }
}
