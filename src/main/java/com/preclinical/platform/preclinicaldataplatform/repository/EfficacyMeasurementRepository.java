package com.preclinical.platform.preclinicaldataplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.preclinical.platform.preclinicaldataplatform.entity.EfficacyMeasurement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EfficacyMeasurementRepository extends JpaRepository<EfficacyMeasurement, Long> {

    List<EfficacyMeasurement> findByPatientId(Long patientId);

    List<EfficacyMeasurement> findByStudyId(Long studyId);

    List<EfficacyMeasurement> findByPatientIdAndMeasurementType(
            Long patientId,
            EfficacyMeasurement.MeasurementType measurementType);

    @Query("SELECT em FROM EfficacyMeasurement em WHERE em.measurementDate BETWEEN :startDate AND :endDate")
    List<EfficacyMeasurement> findMeasurementsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT em FROM EfficacyMeasurement em WHERE em.patient.id = :patientId ORDER BY em.studyDay ASC")
    List<EfficacyMeasurement> findPatientMeasurementsOrderedByStudyDay(@Param("patientId") Long patientId);

    @Query(value = "SELECT * FROM efficacy_measurements WHERE patient_id = :patientId AND measurement_type = :type ORDER BY study_day ASC LIMIT 1", nativeQuery = true)
    Optional<EfficacyMeasurement> findBaselineMeasurement(
            @Param("patientId") Long patientId,
            @Param("type") String type);

    @Query("SELECT AVG(em.measurementValue) FROM EfficacyMeasurement em WHERE em.study.id = :studyId AND em.measurementType = :type")
    Double calculateAverageValueByStudyAndType(
            @Param("studyId") Long studyId,
            @Param("type") EfficacyMeasurement.MeasurementType type);

    @Query("SELECT COUNT(em) FROM EfficacyMeasurement em WHERE em.study.id = :studyId AND em.measurementValue IS NOT NULL AND em.normalRangeLow IS NOT NULL AND em.normalRangeHigh IS NOT NULL AND (em.measurementValue < em.normalRangeLow OR em.measurementValue > em.normalRangeHigh)")
    long countAbnormalMeasurements(@Param("studyId") Long studyId);
}