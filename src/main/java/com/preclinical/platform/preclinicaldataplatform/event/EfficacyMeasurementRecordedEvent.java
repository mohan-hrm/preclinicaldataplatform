package com.preclinical.platform.preclinicaldataplatform.event;

import com.preclinical.platform.preclinicaldataplatform.entity.EfficacyMeasurement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EfficacyMeasurementRecordedEvent {
	private EfficacyMeasurement measurement;
}