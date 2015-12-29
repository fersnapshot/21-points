package com.desprogramar.jhipster.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.desprogramar.jhipster.domain.enumeration.Units;

/**
 * A DTO for the Preference entity.
 */
public class PreferenceDTO implements Serializable {

	private static final long serialVersionUID = -547900858488314L;

	private Long id;

    @NotNull
    @Min(value = 10)
    @Max(value = 21)
    private Integer weeklyGoal;

    @NotNull
    private Units weightUnits;

    public PreferenceDTO() {
		super();
	}

    public PreferenceDTO(Long id, Integer weeklyGoal, Units weightUnits) {
		this();
		this.id = id;
		this.weeklyGoal = weeklyGoal;
		this.weightUnits = weightUnits;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeeklyGoal() {
        return weeklyGoal;
    }

    public void setWeeklyGoal(Integer weeklyGoal) {
        this.weeklyGoal = weeklyGoal;
    }

    public Units getWeightUnits() {
        return weightUnits;
    }

    public void setWeightUnits(Units weightUnits) {
        this.weightUnits = weightUnits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PreferenceDTO preferenceDTO = (PreferenceDTO) o;

        if ( ! Objects.equals(id, preferenceDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PreferenceDTO{" +
            "id=" + id +
            ", weeklyGoal='" + weeklyGoal + "'" +
            ", weightUnits='" + weightUnits + "'" +
            '}';
    }
}
