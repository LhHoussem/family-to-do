package com.mycompany.familytodo.service.mapper;

import com.mycompany.familytodo.domain.Profil;
import com.mycompany.familytodo.service.dto.ProfilDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profil} and its DTO {@link ProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfilMapper extends EntityMapper<ProfilDTO, Profil> {}
