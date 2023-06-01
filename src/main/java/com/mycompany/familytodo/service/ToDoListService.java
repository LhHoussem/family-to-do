package com.mycompany.familytodo.service;

import com.mycompany.familytodo.service.dto.ToDoListDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.familytodo.domain.ToDoList}.
 */
public interface ToDoListService {
    /**
     * Save a toDoList.
     *
     * @param toDoListDTO the entity to save.
     * @return the persisted entity.
     */
    ToDoListDTO save(ToDoListDTO toDoListDTO);

    /**
     * Updates a toDoList.
     *
     * @param toDoListDTO the entity to update.
     * @return the persisted entity.
     */
    ToDoListDTO update(ToDoListDTO toDoListDTO);

    /**
     * Partially updates a toDoList.
     *
     * @param toDoListDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ToDoListDTO> partialUpdate(ToDoListDTO toDoListDTO);

    /**
     * Get all the toDoLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ToDoListDTO> findAll(Pageable pageable);

    /**
     * Get all the toDoLists with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ToDoListDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" toDoList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ToDoListDTO> findOne(String id);

    /**
     * Delete the "id" toDoList.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
