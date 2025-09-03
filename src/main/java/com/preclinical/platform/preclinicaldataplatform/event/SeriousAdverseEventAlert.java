package com.preclinical.platform.preclinicaldataplatform.event;

import com.preclinical.platform.preclinicaldataplatform.entity.AdverseEvent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeriousAdverseEventAlert {
	private AdverseEvent adverseEvent;
}
