package com.preclinical.platform.preclinicaldataplatform.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;

@Repository
public interface AdverseEventRepository extends JpaRepository<AdverseEvent, Long> {
    
    List<AdverseEvent> findByPatientId(Long patientId);
    
    List<AdverseEvent> findByStudyId(Long studyId);
    
    List<AdverseEvent> findBySeverityAndSerious(AdverseEvent.Severity severity, Boolean serious);
    
    @Query("SELECT ae FROM AdverseEvent ae WHERE ae.onsetDate BETWEEN :startDate AND :endDate")
    List<AdverseEvent> findEventsBetweenDates(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ae.severity, COUNT(ae) FROM AdverseEvent ae GROUP BY ae.severity")
    List<Object[]> countEventsBySeverity();
}
