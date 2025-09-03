package com.preclinical.platform.preclinicaldataplatform.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafetyAnalysisReport {
    private Long studyId;
    private String studyCode;
    private Map<AdverseEvent.Severity, Long> eventsBySeverity;
    private Map<AdverseEvent.Causality, Long> eventsByCausality;
    private List<String> frequentEvents;
    private Double seriousEventRate;
    private List<String> safetySignals;
    private LocalDateTime analysisDate;
}
