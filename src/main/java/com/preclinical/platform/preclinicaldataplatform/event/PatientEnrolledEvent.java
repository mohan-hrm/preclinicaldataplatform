package com.preclinical.platform.preclinicaldataplatform.event;

import com.preclinical.platform.preclinicaldataplatform.entity.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientEnrolledEvent {
	private Patient patient;
}