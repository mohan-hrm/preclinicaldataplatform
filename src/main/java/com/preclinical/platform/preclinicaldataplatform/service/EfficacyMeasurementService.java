package com.preclinical.platform.preclinicaldataplatform.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.preclinical.platform.preclinicaldataplatform.dto.CreateEfficacyMeasurementRequest;
import com.preclinical.platform.preclinicaldataplatform.entity.EfficacyMeasurement;
import com.preclinical.platform.preclinicaldataplatform.entity.Patient;
import com.preclinical.platform.preclinicaldataplatform.excception.PatientNotFoundException;
import com.preclinical.platform.preclinicaldataplatform.repository.EfficacyMeasurementRepository;
import com.preclinical.platform.preclinicaldataplatform.repository.PatientRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EfficacyMeasurementService {
    
    private final EfficacyMeasurementRepository efficacyMeasurementRepository;
    private final PatientRepository patientRepository;
    
    public EfficacyMeasurementService(EfficacyMeasurementRepository efficacyMeasurementRepository,
                                    PatientRepository patientRepository) {
        this.efficacyMeasurementRepository = efficacyMeasurementRepository;
        this.patientRepository = patientRepository;
    }
    
    public EfficacyMeasurement recordMeasurement(Long patientId, CreateEfficacyMeasurementRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + patientId));
        
        EfficacyMeasurement measurement = EfficacyMeasurement.builder()
                .measurementDate(request.getMeasurementDate())
                .studyDay(request.getStudyDay())
                .measurementType(request.getMeasurementType())
                .measurementValue(request.getValue())
                .unit(request.getUnit())
                .notes(request.getNotes())
                .normalRangeLow(request.getNormalRangeLow())
                .normalRangeHigh(request.getNormalRangeHigh())
                .patient(patient)
                .study(patient.getStudy())
                .status(EfficacyMeasurement.MeasurementStatus.RECORDED)
                .build();
        
        EfficacyMeasurement savedMeasurement = efficacyMeasurementRepository.save(measurement);
        
        log.info("Recorded {} measurement for patient {}: {} {}", 
                measurement.getMeasurementType(), 
                patient.getPatientCode(),
                measurement.getMeasurementValue(),
                measurement.getUnit());
        
        return savedMeasurement;
    }
    
    public List<EfficacyMeasurement> getPatientMeasurements(Long patientId) {
        return efficacyMeasurementRepository.findPatientMeasurementsOrderedByStudyDay(patientId);
    }
    
    public Map<EfficacyMeasurement.MeasurementType, List<EfficacyMeasurement>> getPatientMeasurementsByType(Long patientId) {
        List<EfficacyMeasurement> measurements = efficacyMeasurementRepository.findByPatientId(patientId);
        return measurements.stream()
                .collect(Collectors.groupingBy(EfficacyMeasurement::getMeasurementType));
    }
    
    public BigDecimal calculatePercentageChangeFromBaseline(Long patientId, EfficacyMeasurement.MeasurementType type) {
        Optional<EfficacyMeasurement> baseline = efficacyMeasurementRepository
                .findBaselineMeasurement(patientId, type.name());
        
        if (baseline.isEmpty()) {
            return null;
        }
        
        List<EfficacyMeasurement> measurements = efficacyMeasurementRepository
                .findByPatientIdAndMeasurementType(patientId, type);
        
        if (measurements.size() < 2) {
            return null;
        }
        
        // Get the most recent measurement
        EfficacyMeasurement latest = measurements.get(measurements.size() - 1);
        return latest.calculatePercentageChange(baseline.get().getMeasurementValue());
    }
    
    public Double getStudyAverageForMeasurementType(Long studyId, EfficacyMeasurement.MeasurementType type) {
        return efficacyMeasurementRepository.calculateAverageValueByStudyAndType(studyId, type);
    }
}
