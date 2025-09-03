package com.preclinical.platform.preclinicaldataplatform.controller;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.preclinical.platform.preclinicaldataplatform.entity.Study;
import com.preclinical.platform.preclinicaldataplatform.repository.StudyRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudyRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private StudyRepository studyRepository;
    
    @Test
    void shouldFindStudyByCode() {
        // Given
        Study study = Study.builder()
                .studyCode("REPO-TEST-001")
                .title("Repository Test Study")
                .phase(Study.StudyPhase.PRECLINICAL)
                .status(Study.StudyStatus.PLANNED)
                .startDate(LocalDate.now().plusDays(10))
                .build();
        
        entityManager.persistAndFlush(study);
        
        // When
        Optional<Study> found = studyRepository.findByStudyCode("REPO-TEST-001");
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Repository Test Study");
        assertThat(found.get().getPhase()).isEqualTo(Study.StudyPhase.PRECLINICAL);
    }
    
    @Test
    void shouldFindStudiesByPhaseAndStatus() {
        // Given
        Study study1 = Study.builder()
                .studyCode("PHASE-TEST-001")
                .title("Phase II Study 1")
                .phase(Study.StudyPhase.PHASE_II)
                .status(Study.StudyStatus.ACTIVE)
                .startDate(LocalDate.now())
                .build();
        
        Study study2 = Study.builder()
                .studyCode("PHASE-TEST-002")
                .title("Phase II Study 2")
                .phase(Study.StudyPhase.PHASE_II)
                .status(Study.StudyStatus.ACTIVE)
                .startDate(LocalDate.now())
                .build();
        
        Study study3 = Study.builder()
                .studyCode("PHASE-TEST-003")
                .title("Phase I Study")
                .phase(Study.StudyPhase.PHASE_I)
                .status(Study.StudyStatus.ACTIVE)
                .startDate(LocalDate.now())
                .build();
        
        entityManager.persist(study1);
        entityManager.persist(study2);
        entityManager.persist(study3);
        entityManager.flush();
        
        // When
        List<Study> phaseIIStudies = studyRepository.findByPhaseAndStatus(
                Study.StudyPhase.PHASE_II, Study.StudyStatus.ACTIVE);
        
        // Then
        assertThat(phaseIIStudies).hasSize(2);
        assertThat(phaseIIStudies)
                .extracting(Study::getStudyCode)
                .containsExactlyInAnyOrder("PHASE-TEST-001", "PHASE-TEST-002");
    }
}