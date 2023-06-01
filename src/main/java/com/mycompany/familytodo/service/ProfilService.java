package com.mycompany.familytodo.service;

import com.mycompany.familytodo.service.dto.ProfilDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.familytodo.domain.Profil}.
 */
public interface ProfilService {
    /**
     * Save a profil.
     *
     * @param profilDTO the entity to save.
     * @return the persisted entity.
     */
    ProfilDTO save(ProfilDTO profilDTO);

    /**
     * Updates a profil.
     *
     * @param profilDTO the entity to update.
     * @return the persisted entity.
     */
    ProfilDTO update(ProfilDTO profilDTO);

    /**
     * Partially updates a profil.
     *
     * @param profilDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProfilDTO> partialUpdate(ProfilDTO profilDTO);

    /**
     * Get all the profils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProfilDTO> findAll(Pageable pageable);

    /**
     * Get the "id" profil.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProfilDTO> findOne(String id);

    /**
     * Delete the "id" profil.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
