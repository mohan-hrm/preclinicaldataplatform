package com.preclinical.platform.preclinicaldataplatform.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.preclinical.platform.preclinicaldataplatform.entity.EfficacyMeasurement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEfficacyMeasurementRequest {
    
    @NotNull(message = "Measurement date is required")
    private LocalDate measurementDate;
    
    @NotNull(message = "Study day is required")
    @Positive(message = "Study day must be positive")
    private Integer studyDay;
    
    @NotNull(message = "Measurement type is required")
    private EfficacyMeasurement.MeasurementType measurementType;
    
    @NotNull(message = "Value is required")
    private BigDecimal value;
    
    private String unit;
    
    private String notes;
    
    private BigDecimal normalRangeLow;
    
    private BigDecimal normalRangeHigh;
}
