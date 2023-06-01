package com.mycompany.familytodo.service.mapper;

import com.mycompany.familytodo.domain.Profil;
import com.mycompany.familytodo.domain.ToDoList;
import com.mycompany.familytodo.service.dto.ProfilDTO;
import com.mycompany.familytodo.service.dto.ToDoListDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ToDoList} and its DTO {@link ToDoListDTO}.
 */
@Mapper(componentModel = "spring")
public interface ToDoListMapper extends EntityMapper<ToDoListDTO, ToDoList> {
    @Mapping(target = "affectedTos", source = "affectedTos", qualifiedByName = "profilIdSet")
    ToDoListDTO toDto(ToDoList s);

    @Mapping(target = "removeAffectedTo", ignore = true)
    ToDoList toEntity(ToDoListDTO toDoListDTO);

    @Named("profilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfilDTO toDtoProfilId(Profil profil);

    @Named("profilIdSet")
    default Set<ProfilDTO> toDtoProfilIdSet(Set<Profil> profil) {
        return profil.stream().map(this::toDtoProfilId).collect(Collectors.toSet());
    }
}
