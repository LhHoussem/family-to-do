package com.mycompany.familytodo.service.mapper;

import com.mycompany.familytodo.domain.Task;
import com.mycompany.familytodo.domain.ToDoList;
import com.mycompany.familytodo.service.dto.TaskDTO;
import com.mycompany.familytodo.service.dto.ToDoListDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "toDos", source = "toDos", qualifiedByName = "toDoListIdSet")
    TaskDTO toDto(Task s);

    @Mapping(target = "removeToDos", ignore = true)
    Task toEntity(TaskDTO taskDTO);

    @Named("toDoListId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ToDoListDTO toDtoToDoListId(ToDoList toDoList);

    @Named("toDoListIdSet")
    default Set<ToDoListDTO> toDtoToDoListIdSet(Set<ToDoList> toDoList) {
        return toDoList.stream().map(this::toDtoToDoListId).collect(Collectors.toSet());
    }
}
