package com.desprogramar.jhipster.web.rest.dto;

import java.io.Serializable;
import java.util.List;

import com.desprogramar.jhipster.domain.BloodPressure;

public class BloodPressureByPeriod implements Serializable {

	private static final long serialVersionUID = -979207707528203161L;
	
	private Integer period;
	private List<BloodPressure> readings;
	
	public BloodPressureByPeriod() {
		super();
	}
	
	public BloodPressureByPeriod(Integer period, List<BloodPressure> readings) {
		this();
		this.period = period;
		this.readings = readings;
	}

	public Integer getPeriod() {
		return period;
	}

	public List<BloodPressure> getReadings() {
		return readings;
	}
	
}
