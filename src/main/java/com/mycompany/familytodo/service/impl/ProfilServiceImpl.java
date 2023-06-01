package com.mycompany.familytodo.service.impl;

import com.mycompany.familytodo.domain.Profil;
import com.mycompany.familytodo.repository.ProfilRepository;
import com.mycompany.familytodo.service.ProfilService;
import com.mycompany.familytodo.service.dto.ProfilDTO;
import com.mycompany.familytodo.service.mapper.ProfilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Profil}.
 */
@Service
public class ProfilServiceImpl implements ProfilService {

    private final Logger log = LoggerFactory.getLogger(ProfilServiceImpl.class);

    private final ProfilRepository profilRepository;

    private final ProfilMapper profilMapper;

    public ProfilServiceImpl(ProfilRepository profilRepository, ProfilMapper profilMapper) {
        this.profilRepository = profilRepository;
        this.profilMapper = profilMapper;
    }

    @Override
    public ProfilDTO save(ProfilDTO profilDTO) {
        log.debug("Request to save Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        return profilMapper.toDto(profil);
    }

    @Override
    public ProfilDTO update(ProfilDTO profilDTO) {
        log.debug("Request to update Profil : {}", profilDTO);
        Profil profil = profilMapper.toEntity(profilDTO);
        profil = profilRepository.save(profil);
        return profilMapper.toDto(profil);
    }

    @Override
    public Optional<ProfilDTO> partialUpdate(ProfilDTO profilDTO) {
        log.debug("Request to partially update Profil : {}", profilDTO);

        return profilRepository
            .findById(profilDTO.getId())
            .map(existingProfil -> {
                profilMapper.partialUpdate(existingProfil, profilDTO);

                return existingProfil;
            })
            .map(profilRepository::save)
            .map(profilMapper::toDto);
    }

    @Override
    public Page<ProfilDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Profils");
        return profilRepository.findAll(pageable).map(profilMapper::toDto);
    }

    @Override
    public Optional<ProfilDTO> findOne(String id) {
        log.debug("Request to get Profil : {}", id);
        return profilRepository.findById(id).map(profilMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Profil : {}", id);
        profilRepository.deleteById(id);
    }
}
