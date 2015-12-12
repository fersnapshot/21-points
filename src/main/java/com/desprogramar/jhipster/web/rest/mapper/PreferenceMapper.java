package com.desprogramar.jhipster.web.rest.mapper;

import com.desprogramar.jhipster.domain.*;
import com.desprogramar.jhipster.web.rest.dto.PreferenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Preference and its DTO PreferenceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PreferenceMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    PreferenceDTO preferenceToPreferenceDTO(Preference preference);

    @Mapping(source = "userId", target = "user")
    Preference preferenceDTOToPreference(PreferenceDTO preferenceDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
