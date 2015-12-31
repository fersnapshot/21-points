package com.desprogramar.jhipster.web.rest.mapper;

import com.desprogramar.jhipster.domain.*;
import com.desprogramar.jhipster.repository.UserRepository;
import com.desprogramar.jhipster.web.rest.dto.PreferenceDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity Preference and its DTO PreferenceDTO.
 */
@Component
public class PreferenceMapper {

    @Autowired
    UserRepository userRepository;

    public PreferenceDTO preferenceToPreferenceDTO(Preference p) {
    	if (p == null)  return null;
    	return new PreferenceDTO(p.getId(), p.getWeeklyGoal(), p.getWeightUnits(), p.getDays());
    }

    public Preference preferenceDTOToPreference(PreferenceDTO preferenceDTO) {
    	if (preferenceDTO == null) return null;
        Preference preference = new Preference();
        preference.setUser(userRepository.findOneByUserIsCurrentUser().get());
        preference.setId(preferenceDTO.getId());
        preference.setWeeklyGoal(preferenceDTO.getWeeklyGoal());
        preference.setWeightUnits(preferenceDTO.getWeightUnits());
        preference.setDays(preferenceDTO.getDays());
        return preference;
    }

}
