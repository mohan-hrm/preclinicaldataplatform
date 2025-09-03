package com.preclinical.platform.preclinicaldataplatform.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudyRequest {
    @Size(min = 10, max = 200, message = "Title must be between 10 and 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Objective must not exceed 1000 characters")
    private String objective;
    
    private LocalDate endDate;
}
