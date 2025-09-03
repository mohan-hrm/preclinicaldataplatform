package com.preclinical.platform.preclinicaldataplatform.event;

import com.preclinical.platform.preclinicaldataplatform.entity.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientStatusChangedEvent {
    private Patient patient;
    private Patient.PatientStatus oldStatus;
    private Patient.PatientStatus newStatus;
}
