package com.desprogramar.jhipster.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by fer on 18/12/15.
 */
public class PointPerWeek implements Serializable {

	private static final long serialVersionUID = 6820640450964014159L;

	private LocalDate date;
    private Integer points;

    public PointPerWeek(LocalDate week, Integer points) {
        this.date = week;
        this.points = points;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate week) {
        this.date = week;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PointPerWeek{" +
            "date=" + date +
            ", points=" + points +
            '}';
    }
}
