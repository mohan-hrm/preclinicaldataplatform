package com.preclinical.platform.preclinicaldataplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    @NotBlank
    private String patientCode;
    
    @Column(nullable = false)
    @Min(18)
    @Max(100)
    private Integer age;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal weight;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal height;
    
    @Enumerated(EnumType.STRING)
    private PatientStatus status = PatientStatus.ENROLLED;
    
    private LocalDate enrollmentDate;
    
    private LocalDate completionDate;
    
    @Column(length = 500)
    private String medicalHistory;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdverseEvent> adverseEvents = new ArrayList<>();
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EfficacyMeasurement> efficacyMeasurements = new ArrayList<>();
    
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    public enum PatientStatus {
        ENROLLED, ACTIVE, COMPLETED, WITHDRAWN, DISCONTINUED
    }
}
