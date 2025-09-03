package com.preclinical.platform.preclinicaldataplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;
import com.preclinical.platform.preclinicaldataplatform.entity.EfficacyMeasurement;
import com.preclinical.platform.preclinicaldataplatform.entity.Patient;
import com.preclinical.platform.preclinicaldataplatform.entity.Study;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditService {
	private static final Logger log = LoggerFactory.getLogger(AuditService.class);
	public void logStudyCreation(Study study) {
		log.info("AUDIT: Study created - ID: {}, Code: {}", study.getId(), study.getStudyCode());
	}

	public void logPatientEnrollment(Patient patient) {
		log.info("AUDIT: Patient enrolled - ID: {}, Code: {}, Study: {}", patient.getId(), patient.getPatientCode(),
				patient.getStudy().getStudyCode());
	}

	public void logSeriousAdverseEvent(AdverseEvent adverseEvent) {
		log.error("AUDIT: SAE reported - ID: {}, Term: {}, Patient: {}", adverseEvent.getId(),
				adverseEvent.getEventTerm(), adverseEvent.getPatient().getPatientCode());
	}

	public void logStudyStatusChange(Study study, Study.StudyStatus oldStatus, Study.StudyStatus newStatus) {
		log.info("AUDIT: Study status changed - Study: {}, From: {}, To: {}", study.getStudyCode(), oldStatus,
				newStatus);
	}

	public void logEfficacyMeasurement(EfficacyMeasurement measurement) {
		log.debug("AUDIT: Measurement recorded - Type: {}, Value: {}, Patient: {}", measurement.getMeasurementType(),
				measurement.getMeasurementValue(), measurement.getPatient().getPatientCode());
	}
	
	public void logPatientUpdate(Patient patient) {
	    try {
	        log.info("AUDIT: Patient updated - Code: {}, Study: {}", 
	                patient.getPatientCode(),
	                patient.getStudy() != null ? patient.getStudy().getStudyCode() : "unknown");
	    } catch (Exception e) {
	        log.info("AUDIT: Patient updated");
	    }
	}

	public void logPatientStatusChange(Patient patient, Patient.PatientStatus oldStatus, Patient.PatientStatus newStatus) {
	    try {
	        log.info("AUDIT: Patient status changed - Patient: {}, From: {}, To: {}", 
	                patient.getPatientCode(), oldStatus, newStatus);
	    } catch (Exception e) {
	        log.info("AUDIT: Patient status changed from {} to {}", oldStatus, newStatus);
	    }
	}
}