package com.desprogramar.jhipster.web.rest.dto;

import java.io.Serializable;
import java.util.List;

import com.desprogramar.jhipster.domain.Weight;

public class WeightByPeriod implements Serializable {

	private static final long serialVersionUID = -7153389493865311887L;
	
	private Integer period;
	private List<Weight> readings;
	
	public WeightByPeriod() {
		super();
	}
	
	public WeightByPeriod(Integer period, List<Weight> readings) {
		this();
		this.period = period;
		this.readings = readings;
	}

	public Integer getPeriod() {
		return period;
	}

	public List<Weight> getReadings() {
		return readings;
	}
	
}
