package com.preclinical.platform.preclinicaldataplatform.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import com.preclinical.platform.preclinicaldataplatform.dto.CreateAdverseEventRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.CreatePatientRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.CreateStudyRequest;
import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;
import com.preclinical.platform.preclinicaldataplatform.entity.Patient;
import com.preclinical.platform.preclinicaldataplatform.entity.Study;
import com.preclinical.platform.preclinicaldataplatform.event.PatientEnrolledEvent;
import com.preclinical.platform.preclinicaldataplatform.event.SeriousAdverseEventAlert;
import com.preclinical.platform.preclinicaldataplatform.event.StudyCreatedEvent;
import com.preclinical.platform.preclinicaldataplatform.repository.PatientRepository;
import com.preclinical.platform.preclinicaldataplatform.repository.StudyRepository;
import com.preclinical.platform.preclinicaldataplatform.service.StudyManagementService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
class StudyManagementServiceTest {
    
    @Autowired
    private StudyManagementService studyManagementService;
    
    @MockBean
    private StudyRepository studyRepository;
    
    @MockBean
    private PatientRepository patientRepository;
    
    @MockBean
    private ApplicationEventPublisher eventPublisher;
    
    @Test
    void shouldCreateStudySuccessfully() {
        // Given
        CreateStudyRequest request = CreateStudyRequest.builder()
                .studyCode("STUDY-001")
                .title("Phase II Efficacy Study of Drug X")
                .objective("Evaluate safety and efficacy of Drug X in target population")
                .phase(Study.StudyPhase.PHASE_II)
                .startDate(LocalDate.now().plusDays(30))
                .build();
        
        Study expectedStudy = Study.builder()
                .id(1L)
                .studyCode(request.getStudyCode())
                .title(request.getTitle())
                .objective(request.getObjective())
                .phase(request.getPhase())
                .startDate(request.getStartDate())
                .status(Study.StudyStatus.PLANNED)
                .build();
        
        when(studyRepository.save(any(Study.class))).thenReturn(expectedStudy);
        
        // When
        Study result = studyManagementService.createStudy(request);
        
        // Then
        assertThat(result.getStudyCode()).isEqualTo("STUDY-001");
        assertThat(result.getTitle()).isEqualTo("Phase II Efficacy Study of Drug X");
        assertThat(result.getPhase()).isEqualTo(Study.StudyPhase.PHASE_II);
        assertThat(result.getStatus()).isEqualTo(Study.StudyStatus.PLANNED);
        
        verify(studyRepository).save(any(Study.class));
        verify(eventPublisher).publishEvent(any(StudyCreatedEvent.class));
    }
    
    @Test
    void shouldEnrollPatientInActiveStudy() {
        // Given
        Long studyId = 1L;
        Study activeStudy = Study.builder()
                .id(studyId)
                .studyCode("STUDY-001")
                .status(Study.StudyStatus.ACTIVE)
                .build();
        
        CreatePatientRequest request = CreatePatientRequest.builder()
                .patientCode("PAT-001")
                .age(45)
                .gender(Patient.Gender.FEMALE)
                .weight(new BigDecimal("65.5"))
                .height(new BigDecimal("165.0"))
                .build();
        
        Patient expectedPatient = Patient.builder()
                .id(1L)
                .patientCode(request.getPatientCode())
                .age(request.getAge())
                .gender(request.getGender())
                .weight(request.getWeight())
                .height(request.getHeight())
                .status(Patient.PatientStatus.ENROLLED)
                .study(activeStudy)
                .enrollmentDate(LocalDate.now())
                .build();
        
        when(studyRepository.findById(studyId)).thenReturn(Optional.of(activeStudy));
        when(patientRepository.save(any(Patient.class))).thenReturn(expectedPatient);
        
        // When
        Patient result = studyManagementService.enrollPatient(studyId, request);
        
        // Then
        assertThat(result.getPatientCode()).isEqualTo("PAT-001");
        assertThat(result.getAge()).isEqualTo(45);
        assertThat(result.getStatus()).isEqualTo(Patient.PatientStatus.ENROLLED);
        assertThat(result.getStudy()).isEqualTo(activeStudy);
        
        verify(patientRepository).save(any(Patient.class));
        verify(eventPublisher).publishEvent(any(PatientEnrolledEvent.class));
    }
    
    @Test
    void shouldThrowExceptionWhenEnrollingPatientInInactiveStudy() {
        // Given
        Long studyId = 1L;
        Study plannedStudy = Study.builder()
                .id(studyId)
                .studyCode("STUDY-001")
                .status(Study.StudyStatus.PLANNED)
                .build();
        
        CreatePatientRequest request = CreatePatientRequest.builder()
                .patientCode("PAT-001")
                .age(45)
                .gender(Patient.Gender.FEMALE)
                .build();
        
        when(studyRepository.findById(studyId)).thenReturn(Optional.of(plannedStudy));
        
        // When & Then
        assertThatThrownBy(() -> studyManagementService.enrollPatient(studyId, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot enroll patient in non-active study");
        
        verify(patientRepository, never()).save(any(Patient.class));
    }
    
    @Test
    void shouldRecordSeriousAdverseEvent() {
        // Given
        Long patientId = 1L;
        Patient patient = Patient.builder()
                .id(patientId)
                .patientCode("PAT-001")
                .study(Study.builder().id(1L).studyCode("STUDY-001").build())
                .build();
        
        CreateAdverseEventRequest request = CreateAdverseEventRequest.builder()
                .eventTerm("Severe Headache")
                .severity(AdverseEvent.Severity.SEVERE)
                .causality(AdverseEvent.Causality.PROBABLE)
                .onsetDate(LocalDate.now().minusDays(1))
                .serious(true)
                .description("Patient reported severe headache with nausea")
                .build();
        
        AdverseEvent expectedEvent = AdverseEvent.builder()
                .id(1L)
                .eventTerm(request.getEventTerm())
                .severity(request.getSeverity())
                .causality(request.getCausality())
                .onsetDate(request.getOnsetDate())
                .serious(request.getSerious())
                .patient(patient)
                .study(patient.getStudy())
                .build();
        
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // When
        AdverseEvent result = studyManagementService.recordAdverseEvent(patientId, request);
        
        // Then
        assertThat(result.getEventTerm()).isEqualTo("Severe Headache");
        assertThat(result.getSeverity()).isEqualTo(AdverseEvent.Severity.SEVERE);
        assertThat(result.getSerious()).isTrue();
        
        verify(eventPublisher).publishEvent(any(SeriousAdverseEventAlert.class));
    }
}

