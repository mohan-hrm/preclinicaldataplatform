package com.preclinical.platform.preclinicaldataplatform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisReport {
    private String analysisType;
    private Map<String, Object> results;
    private List<String> insights;
    private BigDecimal confidenceLevel;
    private LocalDateTime generatedAt;
    private String methodology;
}