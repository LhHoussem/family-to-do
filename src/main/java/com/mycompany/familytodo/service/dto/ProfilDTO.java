package com.mycompany.familytodo.service.dto;

import com.mycompany.familytodo.domain.enumeration.ProfilRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.familytodo.domain.Profil} entity.
 */
@Schema(description = "Profil entity.\n@author Houssem.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfilDTO implements Serializable {

    private String id;

    private String name;

    private String imageUrl;

    private ProfilRole role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProfilRole getRole() {
        return role;
    }

    public void setRole(ProfilRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfilDTO)) {
            return false;
        }

        ProfilDTO profilDTO = (ProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfilDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
