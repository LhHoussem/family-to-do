package com.mycompany.familytodo.web.rest;

import com.mycompany.familytodo.repository.ToDoListRepository;
import com.mycompany.familytodo.service.ToDoListService;
import com.mycompany.familytodo.service.dto.ToDoListDTO;
import com.mycompany.familytodo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.familytodo.domain.ToDoList}.
 */
@RestController
@RequestMapping("/api")
public class ToDoListResource {

    private final Logger log = LoggerFactory.getLogger(ToDoListResource.class);

    private static final String ENTITY_NAME = "toDoList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToDoListService toDoListService;

    private final ToDoListRepository toDoListRepository;

    public ToDoListResource(ToDoListService toDoListService, ToDoListRepository toDoListRepository) {
        this.toDoListService = toDoListService;
        this.toDoListRepository = toDoListRepository;
    }

    /**
     * {@code POST  /to-do-lists} : Create a new toDoList.
     *
     * @param toDoListDTO the toDoListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toDoListDTO, or with status {@code 400 (Bad Request)} if the toDoList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/to-do-lists")
    public ResponseEntity<ToDoListDTO> createToDoList(@RequestBody ToDoListDTO toDoListDTO) throws URISyntaxException {
        log.debug("REST request to save ToDoList : {}", toDoListDTO);
        if (toDoListDTO.getId() != null) {
            throw new BadRequestAlertException("A new toDoList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToDoListDTO result = toDoListService.save(toDoListDTO);
        return ResponseEntity
            .created(new URI("/api/to-do-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /to-do-lists/:id} : Updates an existing toDoList.
     *
     * @param id the id of the toDoListDTO to save.
     * @param toDoListDTO the toDoListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toDoListDTO,
     * or with status {@code 400 (Bad Request)} if the toDoListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toDoListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/to-do-lists/{id}")
    public ResponseEntity<ToDoListDTO> updateToDoList(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ToDoListDTO toDoListDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ToDoList : {}, {}", id, toDoListDTO);
        if (toDoListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toDoListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toDoListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ToDoListDTO result = toDoListService.update(toDoListDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toDoListDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /to-do-lists/:id} : Partial updates given fields of an existing toDoList, field will ignore if it is null
     *
     * @param id the id of the toDoListDTO to save.
     * @param toDoListDTO the toDoListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toDoListDTO,
     * or with status {@code 400 (Bad Request)} if the toDoListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the toDoListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the toDoListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/to-do-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ToDoListDTO> partialUpdateToDoList(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ToDoListDTO toDoListDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ToDoList partially : {}, {}", id, toDoListDTO);
        if (toDoListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toDoListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toDoListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ToDoListDTO> result = toDoListService.partialUpdate(toDoListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toDoListDTO.getId())
        );
    }

    /**
     * {@code GET  /to-do-lists} : get all the toDoLists.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toDoLists in body.
     */
    @GetMapping("/to-do-lists")
    public ResponseEntity<List<ToDoListDTO>> getAllToDoLists(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of ToDoLists");
        Page<ToDoListDTO> page;
        if (eagerload) {
            page = toDoListService.findAllWithEagerRelationships(pageable);
        } else {
            page = toDoListService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /to-do-lists/:id} : get the "id" toDoList.
     *
     * @param id the id of the toDoListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toDoListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/to-do-lists/{id}")
    public ResponseEntity<ToDoListDTO> getToDoList(@PathVariable String id) {
        log.debug("REST request to get ToDoList : {}", id);
        Optional<ToDoListDTO> toDoListDTO = toDoListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toDoListDTO);
    }

    /**
     * {@code DELETE  /to-do-lists/:id} : delete the "id" toDoList.
     *
     * @param id the id of the toDoListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/to-do-lists/{id}")
    public ResponseEntity<Void> deleteToDoList(@PathVariable String id) {
        log.debug("REST request to delete ToDoList : {}", id);
        toDoListService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
