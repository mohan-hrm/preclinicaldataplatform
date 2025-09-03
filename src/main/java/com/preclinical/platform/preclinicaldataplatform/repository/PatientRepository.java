package com.preclinical.platform.preclinicaldataplatform.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.preclinical.platform.preclinicaldataplatform.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    Optional<Patient> findByPatientCode(String patientCode);
    
    List<Patient> findByStudyId(Long studyId);
    
    // Add this missing method
    long countByStudyId(Long studyId);
    
    List<Patient> findByStatusAndEnrollmentDateAfter(Patient.PatientStatus status, LocalDate date);
    
    @Query("SELECT p FROM Patient p WHERE p.age BETWEEN :minAge AND :maxAge")
    List<Patient> findPatientsByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
    
    @Query("SELECT COUNT(p) FROM Patient p WHERE p.study.id = :studyId AND p.status = 'COMPLETED'")
    long countCompletedPatientsByStudy(@Param("studyId") Long studyId);
    
    // Additional useful count methods you might need:
    @Query("SELECT COUNT(p) FROM Patient p WHERE p.study.id = :studyId AND p.status = :status")
    long countByStudyIdAndStatus(@Param("studyId") Long studyId, @Param("status") Patient.PatientStatus status);
    
    @Query("SELECT COUNT(p) FROM Patient p WHERE p.study.id = :studyId AND p.status = 'ENROLLED'")
    long countEnrolledPatientsByStudy(@Param("studyId") Long studyId);
    
    @Query("SELECT COUNT(p) FROM Patient p WHERE p.study.id = :studyId AND p.status = 'ACTIVE'")
    long countActivePatientsByStudy(@Param("studyId") Long studyId);
    
    @Query("SELECT COUNT(p) FROM Patient p WHERE p.study.id = :studyId AND p.status = 'WITHDRAWN'")
    long countWithdrawnPatientsByStudy(@Param("studyId") Long studyId);
}