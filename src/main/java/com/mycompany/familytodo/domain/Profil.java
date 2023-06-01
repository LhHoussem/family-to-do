package com.mycompany.familytodo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.familytodo.domain.enumeration.ProfilRole;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Profil entity.\n@author Houssem.
 */
@Document(collection = "profil")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Profil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("image_url")
    private String imageUrl;

    @Field("role")
    private ProfilRole role;

    @DBRef
    @Field("toDoList")
    @JsonIgnoreProperties(value = { "affectedTos", "owners", "tasks" }, allowSetters = true)
    private ToDoList toDoList;

    @DBRef
    @Field("affecteds")
    @JsonIgnoreProperties(value = { "affectedTos", "owners", "tasks" }, allowSetters = true)
    private Set<ToDoList> affecteds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Profil id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Profil name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Profil imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProfilRole getRole() {
        return this.role;
    }

    public Profil role(ProfilRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(ProfilRole role) {
        this.role = role;
    }

    public ToDoList getToDoList() {
        return this.toDoList;
    }

    public void setToDoList(ToDoList toDoList) {
        this.toDoList = toDoList;
    }

    public Profil toDoList(ToDoList toDoList) {
        this.setToDoList(toDoList);
        return this;
    }

    public Set<ToDoList> getAffecteds() {
        return this.affecteds;
    }

    public void setAffecteds(Set<ToDoList> toDoLists) {
        if (this.affecteds != null) {
            this.affecteds.forEach(i -> i.removeAffectedTo(this));
        }
        if (toDoLists != null) {
            toDoLists.forEach(i -> i.addAffectedTo(this));
        }
        this.affecteds = toDoLists;
    }

    public Profil affecteds(Set<ToDoList> toDoLists) {
        this.setAffecteds(toDoLists);
        return this;
    }

    public Profil addAffected(ToDoList toDoList) {
        this.affecteds.add(toDoList);
        toDoList.getAffectedTos().add(this);
        return this;
    }

    public Profil removeAffected(ToDoList toDoList) {
        this.affecteds.remove(toDoList);
        toDoList.getAffectedTos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profil)) {
            return false;
        }
        return id != null && id.equals(((Profil) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profil{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
