package com.mycompany.familytodo.service.mapper;

import com.mycompany.familytodo.domain.Profil;
import com.mycompany.familytodo.domain.ToDoList;
import com.mycompany.familytodo.service.dto.ProfilDTO;
import com.mycompany.familytodo.service.dto.ToDoListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profil} and its DTO {@link ProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfilMapper extends EntityMapper<ProfilDTO, Profil> {
    @Mapping(target = "toDoList", source = "toDoList", qualifiedByName = "toDoListId")
    ProfilDTO toDto(Profil s);

    @Named("toDoListId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ToDoListDTO toDtoToDoListId(ToDoList toDoList);
}
