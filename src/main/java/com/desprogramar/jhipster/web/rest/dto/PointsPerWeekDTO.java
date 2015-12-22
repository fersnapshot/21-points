package com.desprogramar.jhipster.web.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by fer on 18/12/15.
 */
public class PointsPerWeekDTO implements Serializable {

	private static final long serialVersionUID = 6820640450964014159L;

	private LocalDate week;
    private Integer points;

    public PointsPerWeekDTO(LocalDate week, Integer points) {
        this.week = week;
        this.points = points;
    }

    public LocalDate getWeek() {
        return week;
    }

    public void setWeek(LocalDate week) {
        this.week = week;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PointsPerWeekDTO{" +
            "week=" + week +
            ", points=" + points +
            '}';
    }
}
