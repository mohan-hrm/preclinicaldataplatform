package com.preclinical.platform.preclinicaldataplatform.dto;

import java.time.LocalDate;

import com.preclinical.platform.preclinicaldataplatform.entity.Study;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudyRequest {
    @NotBlank(message = "Study code is required")
    @Size(min = 3, max = 20, message = "Study code must be between 3 and 20 characters")
    private String studyCode;
    
    @NotBlank(message = "Study title is required")
    @Size(min = 10, max = 200, message = "Title must be between 10 and 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Objective must not exceed 1000 characters")
    private String objective;
    
    @NotNull(message = "Study phase is required")
    private Study.StudyPhase phase;
    
    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @AssertTrue(message = "End date must be after start date")
    public boolean isEndDateValid() {
        if (startDate == null || endDate == null) {
            return true; // Let @NotNull handle null validation
        }
        return endDate.isAfter(startDate);
    }
}
