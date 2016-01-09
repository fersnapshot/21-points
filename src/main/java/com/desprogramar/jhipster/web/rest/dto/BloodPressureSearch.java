package com.desprogramar.jhipster.web.rest.dto;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class BloodPressureSearch implements Serializable {

	private static final long serialVersionUID = -5547734328905686233L;
	
    private ZonedDateTime timestamp;
    private Integer systolic;
    private Integer diastolic;
    
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

	public Integer getSystolic() {
		return systolic;
	}

	public void setSystolic(Integer systolic) {
		this.systolic = systolic;
	}

	public Integer getDiastolic() {
		return diastolic;
	}

	public void setDiastolic(Integer diastolic) {
		this.diastolic = diastolic;
	}

	@Override
	public String toString() {
		return "BloodPressureSearch [timestamp=" + timestamp + ", systolic=" + systolic + ", diastolic=" + diastolic
				+ "]";
	}

}
