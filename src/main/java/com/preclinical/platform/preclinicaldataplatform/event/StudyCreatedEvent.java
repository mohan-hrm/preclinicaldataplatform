package com.preclinical.platform.preclinicaldataplatform.event;

import com.preclinical.platform.preclinicaldataplatform.entity.Study;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudyCreatedEvent {
	private Study study;
}
