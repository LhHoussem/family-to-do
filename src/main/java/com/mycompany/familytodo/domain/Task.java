package com.mycompany.familytodo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Task entity.\n@author Houssem.
 */
@Document(collection = "task")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("is_done")
    private Boolean isDone;

    @Field("label")
    private String label;

    @DBRef
    @Field("toDos")
    @JsonIgnoreProperties(value = { "affectedTos", "owner", "tasks" }, allowSetters = true)
    private Set<ToDoList> toDos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Task id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsDone() {
        return this.isDone;
    }

    public Task isDone(Boolean isDone) {
        this.setIsDone(isDone);
        return this;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public String getLabel() {
        return this.label;
    }

    public Task label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<ToDoList> getToDos() {
        return this.toDos;
    }

    public void setToDos(Set<ToDoList> toDoLists) {
        this.toDos = toDoLists;
    }

    public Task toDos(Set<ToDoList> toDoLists) {
        this.setToDos(toDoLists);
        return this;
    }

    public Task addToDos(ToDoList toDoList) {
        this.toDos.add(toDoList);
        toDoList.getTasks().add(this);
        return this;
    }

    public Task removeToDos(ToDoList toDoList) {
        this.toDos.remove(toDoList);
        toDoList.getTasks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", isDone='" + getIsDone() + "'" +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
