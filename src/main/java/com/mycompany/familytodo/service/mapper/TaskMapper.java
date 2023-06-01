package com.mycompany.familytodo.service.mapper;

import com.mycompany.familytodo.domain.Task;
import com.mycompany.familytodo.domain.ToDoList;
import com.mycompany.familytodo.service.dto.TaskDTO;
import com.mycompany.familytodo.service.dto.ToDoListDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "toDoList", source = "toDoList", qualifiedByName = "toDoListId")
    TaskDTO toDto(Task s);

    @Named("toDoListId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ToDoListDTO toDtoToDoListId(ToDoList toDoList);
}
