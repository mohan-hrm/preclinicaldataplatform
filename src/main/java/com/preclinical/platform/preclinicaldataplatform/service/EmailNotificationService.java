package com.preclinical.platform.preclinicaldataplatform.service;

import org.springframework.stereotype.Service;

import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;
import com.preclinical.platform.preclinicaldataplatform.entity.Patient;
import com.preclinical.platform.preclinicaldataplatform.entity.Study;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailNotificationService {
	public void sendStudyCreatedNotification(Study study) {
		log.info("Sending study creation notification for: {}", study.getStudyCode());
		// Implementation would send email to regulatory team
	}

	public void sendPatientEnrollmentNotification(Patient patient) {
		log.info("Sending enrollment notification for patient: {}", patient.getPatientCode());
		// Implementation would notify study site
	}

	public void sendSAEAlert(AdverseEvent adverseEvent) {
		log.error("Sending SAE alert for: {}", adverseEvent.getEventTerm());
		// Critical: immediate notification to safety team
	}

	public void sendStudyActivationNotification(Study study) {
		log.info("Sending activation notification for study: {}", study.getStudyCode());
	}

	public void sendStudyCompletionNotification(Study study) {
		log.info("Sending completion notification for study: {}", study.getStudyCode());
	}

	public void sendStudyTerminationNotification(Study study) {
		log.warn("Sending termination notification for study: {}", study.getStudyCode());
	}

	public void sendStudySuspensionNotification(Study study) {
		log.warn("Sending suspension notification for study: {}", study.getStudyCode());
	}
}