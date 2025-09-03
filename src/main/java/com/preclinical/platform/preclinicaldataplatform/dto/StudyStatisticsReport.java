package com.preclinical.platform.preclinicaldataplatform.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyStatisticsReport {
    private Long studyId;
    private String studyCode;
    private String studyTitle;
    private Long totalPatients;
    private Long enrolledPatients;
    private Long activePatients;
    private Long completedPatients;
    private Long withdrawnPatients;
    private Long totalAdverseEvents;
    private Long seriousAdverseEvents;
    private Double adverseEventRate;
    private LocalDateTime generatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}