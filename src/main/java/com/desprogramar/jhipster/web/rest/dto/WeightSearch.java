package com.desprogramar.jhipster.web.rest.dto;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class WeightSearch implements Serializable {

	private static final long serialVersionUID = -4822883826234672645L;

    private ZonedDateTime timestamp;
    private Double weight;
    
	public String getTimestampElastic() {
	    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));
	    return dateTimeFormatter.format(timestamp);
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "WeightSearch [timestamp=" + timestamp + ", weight=" + weight + "]";
	}

}
