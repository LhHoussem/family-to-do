package com.mycompany.familytodo.service.dto;

import com.mycompany.familytodo.domain.enumeration.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.familytodo.domain.ToDoList} entity.
 */
@Schema(description = "ToDoList entity.\n@author Houssem.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ToDoListDTO implements Serializable {

    private String id;

    private String label;

    private Status status;

    private Instant creationTimestamp;

    private Instant lastModificationTimestamp;

    private Set<ProfilDTO> affectedTos = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Instant getLastModificationTimestamp() {
        return lastModificationTimestamp;
    }

    public void setLastModificationTimestamp(Instant lastModificationTimestamp) {
        this.lastModificationTimestamp = lastModificationTimestamp;
    }

    public Set<ProfilDTO> getAffectedTos() {
        return affectedTos;
    }

    public void setAffectedTos(Set<ProfilDTO> affectedTos) {
        this.affectedTos = affectedTos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToDoListDTO)) {
            return false;
        }

        ToDoListDTO toDoListDTO = (ToDoListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, toDoListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToDoListDTO{" +
            "id='" + getId() + "'" +
            ", label='" + getLabel() + "'" +
            ", status='" + getStatus() + "'" +
            ", creationTimestamp='" + getCreationTimestamp() + "'" +
            ", lastModificationTimestamp='" + getLastModificationTimestamp() + "'" +
            ", affectedTos=" + getAffectedTos() +
            "}";
    }
}
