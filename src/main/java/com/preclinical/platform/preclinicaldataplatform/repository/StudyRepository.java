package com.preclinical.platform.preclinicaldataplatform.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.preclinical.platform.preclinicaldataplatform.entity.Study;

import jakarta.transaction.Transactional;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    
    Optional<Study> findByStudyCode(String studyCode);
    
    List<Study> findByStatus(Study.StudyStatus status);
    
    @Query("SELECT s FROM Study s WHERE s.phase = :phase AND s.status = :status")
    List<Study> findByPhaseAndStatus(@Param("phase") Study.StudyPhase phase, 
                                    @Param("status") Study.StudyStatus status);
    
    @Query(value = "SELECT * FROM studies s WHERE s.start_date BETWEEN ?1 AND ?2", nativeQuery = true)
    List<Study> findStudiesBetweenDates(LocalDate startDate, LocalDate endDate);
    
    @Modifying
    @Transactional
    @Query("UPDATE Study s SET s.status = :status WHERE s.id = :id")
    int updateStudyStatus(@Param("id") Long id, @Param("status") Study.StudyStatus status);
    
    @Query("SELECT s FROM Study s WHERE SIZE(s.adverseEvents) > :threshold")
    List<Study> findStudiesWithHighAdverseEvents(@Param("threshold") int threshold);
}
