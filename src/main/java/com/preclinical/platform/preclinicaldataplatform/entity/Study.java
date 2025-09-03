package com.preclinical.platform.preclinicaldataplatform.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "studies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    @NotBlank
    private String studyCode;
    
    @Column(nullable = false)
    @NotBlank
    @Size(min = 10, max = 200)
    private String title;
    
    @Enumerated(EnumType.STRING)
    private StudyStatus status = StudyStatus.PLANNED;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @Column(length = 1000)
    private String objective;
    
    @Enumerated(EnumType.STRING)
    private StudyPhase phase;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Patient> patients = new ArrayList<>();
    
    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdverseEvent> adverseEvents = new ArrayList<>();
    
    public enum StudyStatus {
        PLANNED, ACTIVE, COMPLETED, TERMINATED, SUSPENDED
    }
    
    public enum StudyPhase {
        PRECLINICAL, PHASE_I, PHASE_II, PHASE_III, PHASE_IV
    }
}
