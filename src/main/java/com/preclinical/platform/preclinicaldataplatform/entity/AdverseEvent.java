package com.preclinical.platform.preclinicaldataplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "adverse_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdverseEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank
    private String eventTerm;
    
    @Enumerated(EnumType.STRING)
    private Severity severity;
    
    @Enumerated(EnumType.STRING)
    private Causality causality;
    
    @Column(nullable = false)
    private LocalDate onsetDate;
    
    private LocalDate resolutionDate;
    
    @Column(length = 1000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    private Outcome outcome;
    
    @Column(nullable = false)
    private Boolean serious = false;
    
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
    
    public enum Severity {
        MILD, MODERATE, SEVERE
    }
    
    public enum Causality {
        UNRELATED, UNLIKELY, POSSIBLE, PROBABLE, DEFINITE
    }
    
    public enum Outcome {
        RECOVERED, RECOVERING, NOT_RECOVERED, FATAL, UNKNOWN
    }
}
