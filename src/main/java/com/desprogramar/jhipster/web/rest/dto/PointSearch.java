package com.desprogramar.jhipster.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class PointSearch implements Serializable{

	private static final long serialVersionUID = -3492236308703582447L;

    private LocalDate date;
    private String notes;
    
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public boolean isNull() {
		return date == null && notes == null;
	}
	
	
	
	@Override
	public String toString() {
		return "PointSearch [date=" + date + ", notes=" + notes + "]";
	}

}
