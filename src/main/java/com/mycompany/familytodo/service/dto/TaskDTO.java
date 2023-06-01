package com.mycompany.familytodo.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.familytodo.domain.Task} entity.
 */
@Schema(description = "Task entity.\n@author Houssem.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskDTO implements Serializable {

    private String id;

    private Boolean isDone;

    private String label;

    private ToDoListDTO toDoList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ToDoListDTO getToDoList() {
        return toDoList;
    }

    public void setToDoList(ToDoListDTO toDoList) {
        this.toDoList = toDoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDTO{" +
            "id='" + getId() + "'" +
            ", isDone='" + getIsDone() + "'" +
            ", label='" + getLabel() + "'" +
            ", toDoList=" + getToDoList() +
            "}";
    }
}
