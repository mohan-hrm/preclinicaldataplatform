package com.preclinical.platform.preclinicaldataplatform.dto;

import java.math.BigDecimal;

import com.preclinical.platform.preclinicaldataplatform.entity.Patient;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientRequest {
    @NotBlank(message = "Patient code is required")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Patient code must contain only uppercase letters, numbers, and hyphens")
    private String patientCode;
    
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Patient must be at least 18 years old")
    @Max(value = 100, message = "Patient age cannot exceed 100")
    private Integer age;
    
    @NotNull(message = "Gender is required")
    private Patient.Gender gender;
    
    @DecimalMin(value = "30.0", message = "Weight must be at least 30 kg")
    @DecimalMax(value = "300.0", message = "Weight cannot exceed 300 kg")
    private BigDecimal weight;
    
    @DecimalMin(value = "100.0", message = "Height must be at least 100 cm")
    @DecimalMax(value = "250.0", message = "Height cannot exceed 250 cm")
    private BigDecimal height;
    
    @Size(max = 500, message = "Medical history must not exceed 500 characters")
    private String medicalHistory;
}

