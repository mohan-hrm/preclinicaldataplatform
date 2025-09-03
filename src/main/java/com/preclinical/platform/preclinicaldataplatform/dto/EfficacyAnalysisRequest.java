package com.preclinical.platform.preclinicaldataplatform.dto;

import java.time.LocalDate;

import com.preclinical.platform.preclinicaldataplatform.entity.EfficacyMeasurement;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EfficacyAnalysisRequest {
    @NotNull(message = "Study ID is required")
    private Long studyId;
    
    @NotNull(message = "Measurement type is required")
    private EfficacyMeasurement.MeasurementType measurementType;
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    @Min(value = 1, message = "Minimum study day must be at least 1")
    private Integer minStudyDay;
    
    @Max(value = 365, message = "Maximum study day cannot exceed 365")
    private Integer maxStudyDay;
}
