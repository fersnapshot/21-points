package com.desprogramar.jhipster.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desprogramar.jhipster.domain.Preference;
import com.desprogramar.jhipster.domain.enumeration.Units;
import com.desprogramar.jhipster.repository.PreferenceRepository;
import com.desprogramar.jhipster.web.rest.dto.PreferenceDTO;
import com.desprogramar.jhipster.web.rest.mapper.PreferenceMapper;

@Service
public class PreferenceService {

	@Autowired
    private PreferenceRepository preferenceRepository;
	
	@Autowired
    private PreferenceMapper preferenceMapper;

    public PreferenceDTO getUserPreference() {
        Optional<Preference> preferenceOpt = preferenceRepository.findOneByUserIsCurrentUser();
        PreferenceDTO preferenceDTO;
        if (preferenceOpt.isPresent()) {
            preferenceDTO = preferenceMapper.preferenceToPreferenceDTO(preferenceOpt.get());
        } else {
            preferenceDTO = new PreferenceDTO(null, 10, Units.kg, 30);
        } 
        return preferenceDTO;
    }

}
