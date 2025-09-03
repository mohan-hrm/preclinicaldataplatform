package com.preclinical.platform.preclinicaldataplatform.event;

import com.preclinical.platform.preclinicaldataplatform.entity.Study;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudyStatusChangedEvent {
	private Study study;
	private Study.StudyStatus oldStatus;
	private Study.StudyStatus newStatus;
}
