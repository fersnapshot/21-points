package com.desprogramar.jhipster.web.rest.mapper;

import com.desprogramar.jhipster.domain.*;
import com.desprogramar.jhipster.repository.UserRepository;
import com.desprogramar.jhipster.security.SecurityUtils;
import com.desprogramar.jhipster.web.rest.dto.PreferenceDTO;

import org.mapstruct.*;

import javax.inject.Inject;

/**
 * Mapper for the entity Preference and its DTO PreferenceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class PreferenceMapper {

    @Inject
    UserRepository userRepository;

    public abstract PreferenceDTO preferenceToPreferenceDTO(Preference preference);

    public Preference preferenceDTOToPreference(PreferenceDTO preferenceDTO) {
        Preference preference = new Preference();
        preference.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        preference.setId(preferenceDTO.getId());
        preference.setWeeklyGoal(preferenceDTO.getWeeklyGoal());
        preference.setWeightUnits(preferenceDTO.getWeightUnits());
        return preference;
    }

}
