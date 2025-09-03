package com.preclinical.platform.preclinicaldataplatform.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.preclinical.platform.preclinicaldataplatform.dto.AnalysisReport;
import com.preclinical.platform.preclinicaldataplatform.dto.EfficacyAnalysisRequest;
import com.preclinical.platform.preclinicaldataplatform.dto.SafetyAnalysisReport;
import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;
import com.preclinical.platform.preclinicaldataplatform.entity.EfficacyMeasurement;
import com.preclinical.platform.preclinicaldataplatform.entity.Patient;
import com.preclinical.platform.preclinicaldataplatform.entity.Study;
import com.preclinical.platform.preclinicaldataplatform.repository.AdverseEventRepository;
import com.preclinical.platform.preclinicaldataplatform.repository.EfficacyMeasurementRepository;
import com.preclinical.platform.preclinicaldataplatform.repository.PatientRepository;
import com.preclinical.platform.preclinicaldataplatform.repository.StudyRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataAnalysisService {
    
    private final StudyRepository studyRepository;
    private final PatientRepository patientRepository;
    private final AdverseEventRepository adverseEventRepository;
    private final EfficacyMeasurementRepository efficacyMeasurementRepository;
    
    public DataAnalysisService(StudyRepository studyRepository,
                              PatientRepository patientRepository,
                              AdverseEventRepository adverseEventRepository,
                              EfficacyMeasurementRepository efficacyMeasurementRepository) {
        this.studyRepository = studyRepository;
        this.patientRepository = patientRepository;
        this.adverseEventRepository = adverseEventRepository;
        this.efficacyMeasurementRepository = efficacyMeasurementRepository;
    }
    
    @Cacheable(value = "safetyReports", key = "#studyId")
    public SafetyAnalysisReport generateSafetyAnalysis(Long studyId) {
        log.info("Generating safety analysis for study: {}", studyId);
        
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("Study not found"));
        
        List<AdverseEvent> adverseEvents = adverseEventRepository.findByStudyId(studyId);
        long totalPatients = patientRepository.countByStudyId(studyId);
        
        // Group events by severity
        Map<AdverseEvent.Severity, Long> eventsBySeverity = adverseEvents.stream()
                .collect(Collectors.groupingBy(
                    AdverseEvent::getSeverity,
                    Collectors.counting()
                ));
        
        // Group events by causality
        Map<AdverseEvent.Causality, Long> eventsByCausality = adverseEvents.stream()
                .collect(Collectors.groupingBy(
                    AdverseEvent::getCausality,
                    Collectors.counting()
                ));
        
        // Find most frequent events
        List<String> frequentEvents = adverseEvents.stream()
                .collect(Collectors.groupingBy(
                    AdverseEvent::getEventTerm,
                    Collectors.counting()
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        // Calculate serious event rate
        long seriousEvents = adverseEvents.stream()
                .mapToLong(ae -> ae.getSerious() ? 1 : 0)
                .sum();
        
        double seriousEventRate = totalPatients > 0 ? 
                (double) seriousEvents / totalPatients * 100 : 0.0;
        
        // Identify safety signals
        List<String> safetySignals = identifySafetySignals(adverseEvents, totalPatients);
        
        return SafetyAnalysisReport.builder()
                .studyId(studyId)
                .studyCode(study.getStudyCode())
                .eventsBySeverity(eventsBySeverity)
                .eventsByCausality(eventsByCausality)
                .frequentEvents(frequentEvents)
                .seriousEventRate(seriousEventRate)
                .safetySignals(safetySignals)
                .analysisDate(LocalDateTime.now())
                .build();
    }
    
    @Cacheable(value = "efficacyAnalysis", key = "#request.studyId + '_' + #request.measurementType")
    public AnalysisReport analyzeEfficacy(EfficacyAnalysisRequest request) {
        log.info("Analyzing efficacy for study: {} and measurement type: {}", 
                request.getStudyId(), request.getMeasurementType());
        
        List<EfficacyMeasurement> measurements = efficacyMeasurementRepository.findByStudyId(request.getStudyId())
                .stream()
                .filter(m -> m.getMeasurementType() == request.getMeasurementType())
                .collect(Collectors.toList());
        
        if (measurements.isEmpty()) {
            return AnalysisReport.builder()
                    .analysisType("EFFICACY_ANALYSIS")
                    .results(Map.of("error", "No measurements found"))
                    .insights(List.of("No data available for analysis"))
                    .generatedAt(LocalDateTime.now())
                    .build();
        }
        
        // Calculate basic statistics
        Map<String, Object> results = new HashMap<>();
        
        double meanValue = measurements.stream()
                .mapToDouble(m -> m.getMeasurementValue().doubleValue())
                .average()
                .orElse(0.0);
        
        double minValue = measurements.stream()
                .mapToDouble(m -> m.getMeasurementValue().doubleValue())
                .min()
                .orElse(0.0);
        
        double maxValue = measurements.stream()
                .mapToDouble(m -> m.getMeasurementValue().doubleValue())
                .max()
                .orElse(0.0);
        
        results.put("meanValue", meanValue);
        results.put("minValue", minValue);
        results.put("maxValue", maxValue);
        results.put("totalMeasurements", measurements.size());
        results.put("uniquePatients", measurements.stream()
                .map(m -> m.getPatient().getId())
                .distinct()
                .count());
        
        // Generate insights
        List<String> insights = generateEfficacyInsights(measurements, meanValue);
        
        return AnalysisReport.builder()
                .analysisType("EFFICACY_ANALYSIS")
                .results(results)
                .insights(insights)
                .confidenceLevel(new BigDecimal("95.0"))
                .generatedAt(LocalDateTime.now())
                .methodology("Descriptive statistics with trend analysis")
                .build();
    }
    
    public Map<String, Object> getEnrollmentTrends(Long studyId) {
        log.debug("Getting enrollment trends for study: {}", studyId);
        
        List<Patient> patients = patientRepository.findByStudyId(studyId);
        
        Map<String, Object> trends = new HashMap<>();
        
        // Group patients by enrollment month
        Map<String, Long> enrollmentByMonth = patients.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getEnrollmentDate().toString().substring(0, 7), // YYYY-MM
                    Collectors.counting()
                ));
        
        // Calculate enrollment velocity (patients per week)
        long totalPatients = patients.size();
        LocalDate firstEnrollment = patients.stream()
                .map(Patient::getEnrollmentDate)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());
        
        long weeksSinceStart = java.time.temporal.ChronoUnit.WEEKS.between(firstEnrollment, LocalDate.now());
        double enrollmentVelocity = weeksSinceStart > 0 ? (double) totalPatients / weeksSinceStart : 0;
        
        trends.put("enrollmentByMonth", enrollmentByMonth);
        trends.put("totalEnrolled", totalPatients);
        trends.put("enrollmentVelocity", Math.round(enrollmentVelocity * 100.0) / 100.0);
        trends.put("firstEnrollmentDate", firstEnrollment);
        trends.put("lastEnrollmentDate", patients.stream()
                .map(Patient::getEnrollmentDate)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now()));
        
        return trends;
    }
    
    public Map<String, Object> getAdverseEventsSummary(Long studyId, LocalDate startDate, LocalDate endDate) {
        log.debug("Getting adverse events summary for study: {}", studyId);
        
        List<AdverseEvent> events;
        if (studyId != null) {
            events = adverseEventRepository.findByStudyId(studyId);
        } else {
            events = adverseEventRepository.findAll();
        }
        
        // Filter by date range if provided
        if (startDate != null && endDate != null) {
            events = events.stream()
                    .filter(ae -> !ae.getOnsetDate().isBefore(startDate) && 
                                 !ae.getOnsetDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }
        
        Map<String, Object> summary = new HashMap<>();
        
        summary.put("totalEvents", events.size());
        summary.put("seriousEvents", events.stream().mapToLong(ae -> ae.getSerious() ? 1 : 0).sum());
        
        // Events by severity
        Map<String, Long> severityBreakdown = events.stream()
                .collect(Collectors.groupingBy(
                    ae -> ae.getSeverity().name(),
                    Collectors.counting()
                ));
        
        summary.put("eventsBySeverity", severityBreakdown);
        
        // Most common events
        Map<String, Long> commonEvents = events.stream()
                .collect(Collectors.groupingBy(
                    AdverseEvent::getEventTerm,
                    Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
        
        summary.put("topEvents", commonEvents);
        
        return summary;
    }
    
    public Map<String, Object> getPatientMeasurementTrends(Long patientId, EfficacyMeasurement.MeasurementType type) {
        log.debug("Getting measurement trends for patient: {} and type: {}", patientId, type);
        
        List<EfficacyMeasurement> measurements = efficacyMeasurementRepository
                .findByPatientIdAndMeasurementType(patientId, type);
        
        Map<String, Object> trends = new HashMap<>();
        
        if (measurements.isEmpty()) {
            trends.put("error", "No measurements found");
            return trends;
        }
        
        // Sort by study day
        measurements.sort(Comparator.comparing(EfficacyMeasurement::getStudyDay));
        
        List<Map<String, Object>> dataPoints = measurements.stream()
                .map(m -> {
                    Map<String, Object> point = new HashMap<>();
                    point.put("studyDay", m.getStudyDay());
                    point.put("value", m.getMeasurementValue());
                    point.put("date", m.getMeasurementDate());
                    point.put("withinNormalRange", m.isWithinNormalRange());
                    return point;
                })
                .collect(Collectors.toList());
        
        trends.put("dataPoints", dataPoints);
        trends.put("totalMeasurements", measurements.size());
        
        // Calculate trend (simple linear)
        if (measurements.size() >= 2) {
            EfficacyMeasurement first = measurements.get(0);
            EfficacyMeasurement last = measurements.get(measurements.size() - 1);
            
            double percentChange = first.calculatePercentageChange(last.getMeasurementValue()).doubleValue();
            trends.put("percentChangeFromBaseline", Math.round(percentChange * 100.0) / 100.0);
            
            String trendDirection = percentChange > 5 ? "IMPROVING" : 
                                   percentChange < -5 ? "DECLINING" : "STABLE";
            trends.put("trendDirection", trendDirection);
        }
        
        return trends;
    }
    
    private List<String> identifySafetySignals(List<AdverseEvent> events, long totalPatients) {
        List<String> signals = new ArrayList<>();
        
        // Signal 1: High serious event rate
        long seriousEvents = events.stream().mapToLong(ae -> ae.getSerious() ? 1 : 0).sum();
        double seriousRate = totalPatients > 0 ? (double) seriousEvents / totalPatients : 0;
        
        if (seriousRate > 0.1) { // More than 10% serious event rate
            signals.add("HIGH_SERIOUS_EVENT_RATE: " + Math.round(seriousRate * 1000.0) / 10.0 + "%");
        }
        
        // Signal 2: Clustering of specific events
        Map<String, Long> eventCounts = events.stream()
                .collect(Collectors.groupingBy(AdverseEvent::getEventTerm, Collectors.counting()));
        
        eventCounts.entrySet().stream()
                .filter(entry -> entry.getValue() >= Math.max(3, totalPatients * 0.05))
                .forEach(entry -> signals.add("FREQUENT_EVENT: " + entry.getKey() + 
                        " (" + entry.getValue() + " cases)"));
        
        // Signal 3: High causality events
        long probableOrDefiniteEvents = events.stream()
                .mapToLong(ae -> (ae.getCausality() == AdverseEvent.Causality.PROBABLE || 
                                ae.getCausality() == AdverseEvent.Causality.DEFINITE) ? 1 : 0)
                .sum();
        
        if (probableOrDefiniteEvents > totalPatients * 0.15) {
            signals.add("HIGH_DRUG_RELATED_EVENTS: " + probableOrDefiniteEvents + " events");
        }
        
        return signals;
    }
    
    private List<String> generateEfficacyInsights(List<EfficacyMeasurement> measurements, double meanValue) {
        List<String> insights = new ArrayList<>();
        
        // Basic statistics insight
        insights.add("Average measurement value: " + Math.round(meanValue * 100.0) / 100.0);
        
        // Trend analysis
        if (measurements.size() >= 10) {
            insights.add("Sufficient data points for trend analysis (" + measurements.size() + " measurements)");
        } else {
            insights.add("Limited data points - more measurements needed for robust analysis");
        }
        
        // Normal range analysis
        long abnormalMeasurements = measurements.stream()
                .mapToLong(m -> m.isWithinNormalRange() ? 0 : 1)
                .sum();
        
        double abnormalRate = (double) abnormalMeasurements / measurements.size() * 100;
        
        if (abnormalRate > 20) {
            insights.add("HIGH ALERT: " + Math.round(abnormalRate) + "% of measurements outside normal range");
        } else if (abnormalRate > 10) {
            insights.add("CAUTION: " + Math.round(abnormalRate) + "% of measurements outside normal range");
        } else {
            insights.add("Most measurements within expected normal ranges");
        }
        
        return insights;
    }
}