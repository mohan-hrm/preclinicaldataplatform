package com.preclinical.platform.preclinicaldataplatform.dto;

import java.time.LocalDate;

import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdverseEventRequest {
    @NotBlank(message = "Event term is required")
    @Size(min = 3, max = 100, message = "Event term must be between 3 and 100 characters")
    private String eventTerm;
    
    @NotNull(message = "Severity is required")
    private AdverseEvent.Severity severity;
    
    @NotNull(message = "Causality assessment is required")
    private AdverseEvent.Causality causality;
    
    @NotNull(message = "Onset date is required")
    @PastOrPresent(message = "Onset date cannot be in the future")
    private LocalDate onsetDate;
    
    @PastOrPresent(message = "Resolution date cannot be in the future")
    private LocalDate resolutionDate;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Serious flag is required")
    private Boolean serious = false;
    
    @AssertTrue(message = "Resolution date must be after onset date")
    public boolean isResolutionDateValid() {
        if (onsetDate == null || resolutionDate == null) {
            return true;
        }
        return resolutionDate.isAfter(onsetDate) || resolutionDate.isEqual(onsetDate);
    }
}