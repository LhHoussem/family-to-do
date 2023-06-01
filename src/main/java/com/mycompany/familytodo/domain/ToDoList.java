package com.mycompany.familytodo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.familytodo.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * ToDoList entity.\n@author Houssem.
 */
@Document(collection = "to_do_list")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ToDoList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("label")
    private String label;

    @Field("status")
    private Status status;

    @Field("creation_timestamp")
    private Instant creationTimestamp;

    @Field("last_modification_timestamp")
    private Instant lastModificationTimestamp;

    @DBRef
    @Field("affectedTos")
    @JsonIgnoreProperties(value = { "toDoLists", "affecteds" }, allowSetters = true)
    private Set<Profil> affectedTos = new HashSet<>();

    @DBRef
    @Field("owner")
    @JsonIgnoreProperties(value = { "toDoLists", "affecteds" }, allowSetters = true)
    private Profil owner;

    @DBRef
    @Field("tasks")
    @JsonIgnoreProperties(value = { "toDos" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ToDoList id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public ToDoList label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Status getStatus() {
        return this.status;
    }

    public ToDoList status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreationTimestamp() {
        return this.creationTimestamp;
    }

    public ToDoList creationTimestamp(Instant creationTimestamp) {
        this.setCreationTimestamp(creationTimestamp);
        return this;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Instant getLastModificationTimestamp() {
        return this.lastModificationTimestamp;
    }

    public ToDoList lastModificationTimestamp(Instant lastModificationTimestamp) {
        this.setLastModificationTimestamp(lastModificationTimestamp);
        return this;
    }

    public void setLastModificationTimestamp(Instant lastModificationTimestamp) {
        this.lastModificationTimestamp = lastModificationTimestamp;
    }

    public Set<Profil> getAffectedTos() {
        return this.affectedTos;
    }

    public void setAffectedTos(Set<Profil> profils) {
        this.affectedTos = profils;
    }

    public ToDoList affectedTos(Set<Profil> profils) {
        this.setAffectedTos(profils);
        return this;
    }

    public ToDoList addAffectedTo(Profil profil) {
        this.affectedTos.add(profil);
        profil.getAffecteds().add(this);
        return this;
    }

    public ToDoList removeAffectedTo(Profil profil) {
        this.affectedTos.remove(profil);
        profil.getAffecteds().remove(this);
        return this;
    }

    public Profil getOwner() {
        return this.owner;
    }

    public void setOwner(Profil profil) {
        this.owner = profil;
    }

    public ToDoList owner(Profil profil) {
        this.setOwner(profil);
        return this;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.removeToDos(this));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.addToDos(this));
        }
        this.tasks = tasks;
    }

    public ToDoList tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public ToDoList addTasks(Task task) {
        this.tasks.add(task);
        task.getToDos().add(this);
        return this;
    }

    public ToDoList removeTasks(Task task) {
        this.tasks.remove(task);
        task.getToDos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToDoList)) {
            return false;
        }
        return id != null && id.equals(((ToDoList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToDoList{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", status='" + getStatus() + "'" +
            ", creationTimestamp='" + getCreationTimestamp() + "'" +
            ", lastModificationTimestamp='" + getLastModificationTimestamp() + "'" +
            "}";
    }
}
