package com.preclinical.platform.preclinicaldataplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "efficacy_measurements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EfficacyMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotNull
    private LocalDate measurementDate;
    
    @Column(nullable = false)
    @NotNull
    private Integer studyDay;
    
    @Enumerated(EnumType.STRING)
    private MeasurementType measurementType;

    @Column(name = "measurement_value", precision = 10, scale = 3)
    private BigDecimal measurementValue;
    
    @Column(length = 50)
    private String unit;
    
    @Column(length = 500)
    private String notes;
    
    @Enumerated(EnumType.STRING)
    private MeasurementStatus status = MeasurementStatus.RECORDED;
    
    // Reference ranges for the measurement
    @Column(precision = 10, scale = 3)
    private BigDecimal normalRangeLow;
    
    @Column(precision = 10, scale = 3)
    private BigDecimal normalRangeHigh;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;
    
    public enum MeasurementType {
        BLOOD_PRESSURE_SYSTOLIC,
        BLOOD_PRESSURE_DIASTOLIC,
        HEART_RATE,
        WEIGHT,
        PAIN_SCORE,
        TUMOR_SIZE,
        BIOMARKER_LEVEL,
        QUALITY_OF_LIFE_SCORE,
        EFFICACY_SCORE,
        LABORATORY_VALUE,
        IMAGING_MEASUREMENT,
        OTHER
    }
    
    public enum MeasurementStatus {
        RECORDED,
        VERIFIED,
        QUERY_PENDING,
        CORRECTED,
        INVALID
    }
    
    // Utility method to check if measurement is within normal range
    public boolean isWithinNormalRange() {
        if (measurementValue == null || normalRangeLow == null || normalRangeHigh == null) {
            return false;
        }
        return measurementValue.compareTo(normalRangeLow) >= 0 && measurementValue.compareTo(normalRangeHigh) <= 0;
    }
    
    // Calculate percentage change from baseline (if this is a follow-up measurement)
    public BigDecimal calculatePercentageChange(BigDecimal baselineValue) {
        if (baselineValue == null || measurementValue == null || baselineValue.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return measurementValue.subtract(baselineValue)
                .divide(baselineValue, 2, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }
}
