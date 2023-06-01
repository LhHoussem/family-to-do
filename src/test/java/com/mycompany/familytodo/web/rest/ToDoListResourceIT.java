package com.mycompany.familytodo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.familytodo.IntegrationTest;
import com.mycompany.familytodo.domain.ToDoList;
import com.mycompany.familytodo.domain.enumeration.Status;
import com.mycompany.familytodo.repository.ToDoListRepository;
import com.mycompany.familytodo.service.ToDoListService;
import com.mycompany.familytodo.service.dto.ToDoListDTO;
import com.mycompany.familytodo.service.mapper.ToDoListMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ToDoListResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ToDoListResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.IN_PROGRESS;
    private static final Status UPDATED_STATUS = Status.OPENED;

    private static final Instant DEFAULT_CREATION_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFICATION_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFICATION_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/to-do-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ToDoListRepository toDoListRepository;

    @Mock
    private ToDoListRepository toDoListRepositoryMock;

    @Autowired
    private ToDoListMapper toDoListMapper;

    @Mock
    private ToDoListService toDoListServiceMock;

    @Autowired
    private MockMvc restToDoListMockMvc;

    private ToDoList toDoList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDoList createEntity() {
        ToDoList toDoList = new ToDoList()
            .label(DEFAULT_LABEL)
            .status(DEFAULT_STATUS)
            .creationTimestamp(DEFAULT_CREATION_TIMESTAMP)
            .lastModificationTimestamp(DEFAULT_LAST_MODIFICATION_TIMESTAMP);
        return toDoList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDoList createUpdatedEntity() {
        ToDoList toDoList = new ToDoList()
            .label(UPDATED_LABEL)
            .status(UPDATED_STATUS)
            .creationTimestamp(UPDATED_CREATION_TIMESTAMP)
            .lastModificationTimestamp(UPDATED_LAST_MODIFICATION_TIMESTAMP);
        return toDoList;
    }

    @BeforeEach
    public void initTest() {
        toDoListRepository.deleteAll();
        toDoList = createEntity();
    }

    @Test
    void createToDoList() throws Exception {
        int databaseSizeBeforeCreate = toDoListRepository.findAll().size();
        // Create the ToDoList
        ToDoListDTO toDoListDTO = toDoListMapper.toDto(toDoList);
        restToDoListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(toDoListDTO)))
            .andExpect(status().isCreated());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeCreate + 1);
        ToDoList testToDoList = toDoListList.get(toDoListList.size() - 1);
        assertThat(testToDoList.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testToDoList.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testToDoList.getCreationTimestamp()).isEqualTo(DEFAULT_CREATION_TIMESTAMP);
        assertThat(testToDoList.getLastModificationTimestamp()).isEqualTo(DEFAULT_LAST_MODIFICATION_TIMESTAMP);
    }

    @Test
    void createToDoListWithExistingId() throws Exception {
        // Create the ToDoList with an existing ID
        toDoList.setId("existing_id");
        ToDoListDTO toDoListDTO = toDoListMapper.toDto(toDoList);

        int databaseSizeBeforeCreate = toDoListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restToDoListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(toDoListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllToDoLists() throws Exception {
        // Initialize the database
        toDoList.setId(UUID.randomUUID().toString());
        toDoListRepository.save(toDoList);

        // Get all the toDoListList
        restToDoListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toDoList.getId())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationTimestamp").value(hasItem(DEFAULT_CREATION_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].lastModificationTimestamp").value(hasItem(DEFAULT_LAST_MODIFICATION_TIMESTAMP.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllToDoListsWithEagerRelationshipsIsEnabled() throws Exception {
        when(toDoListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restToDoListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(toDoListServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllToDoListsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(toDoListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restToDoListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(toDoListRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getToDoList() throws Exception {
        // Initialize the database
        toDoList.setId(UUID.randomUUID().toString());
        toDoListRepository.save(toDoList);

        // Get the toDoList
        restToDoListMockMvc
            .perform(get(ENTITY_API_URL_ID, toDoList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(toDoList.getId()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.creationTimestamp").value(DEFAULT_CREATION_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.lastModificationTimestamp").value(DEFAULT_LAST_MODIFICATION_TIMESTAMP.toString()));
    }

    @Test
    void getNonExistingToDoList() throws Exception {
        // Get the toDoList
        restToDoListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingToDoList() throws Exception {
        // Initialize the database
        toDoList.setId(UUID.randomUUID().toString());
        toDoListRepository.save(toDoList);

        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();

        // Update the toDoList
        ToDoList updatedToDoList = toDoListRepository.findById(toDoList.getId()).get();
        updatedToDoList
            .label(UPDATED_LABEL)
            .status(UPDATED_STATUS)
            .creationTimestamp(UPDATED_CREATION_TIMESTAMP)
            .lastModificationTimestamp(UPDATED_LAST_MODIFICATION_TIMESTAMP);
        ToDoListDTO toDoListDTO = toDoListMapper.toDto(updatedToDoList);

        restToDoListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toDoListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toDoListDTO))
            )
            .andExpect(status().isOk());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
        ToDoList testToDoList = toDoListList.get(toDoListList.size() - 1);
        assertThat(testToDoList.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testToDoList.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testToDoList.getCreationTimestamp()).isEqualTo(UPDATED_CREATION_TIMESTAMP);
        assertThat(testToDoList.getLastModificationTimestamp()).isEqualTo(UPDATED_LAST_MODIFICATION_TIMESTAMP);
    }

    @Test
    void putNonExistingToDoList() throws Exception {
        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();
        toDoList.setId(UUID.randomUUID().toString());

        // Create the ToDoList
        ToDoListDTO toDoListDTO = toDoListMapper.toDto(toDoList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToDoListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toDoListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toDoListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchToDoList() throws Exception {
        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();
        toDoList.setId(UUID.randomUUID().toString());

        // Create the ToDoList
        ToDoListDTO toDoListDTO = toDoListMapper.toDto(toDoList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToDoListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toDoListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamToDoList() throws Exception {
        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();
        toDoList.setId(UUID.randomUUID().toString());

        // Create the ToDoList
        ToDoListDTO toDoListDTO = toDoListMapper.toDto(toDoList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToDoListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(toDoListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateToDoListWithPatch() throws Exception {
        // Initialize the database
        toDoList.setId(UUID.randomUUID().toString());
        toDoListRepository.save(toDoList);

        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();

        // Update the toDoList using partial update
        ToDoList partialUpdatedToDoList = new ToDoList();
        partialUpdatedToDoList.setId(toDoList.getId());

        partialUpdatedToDoList
            .label(UPDATED_LABEL)
            .creationTimestamp(UPDATED_CREATION_TIMESTAMP)
            .lastModificationTimestamp(UPDATED_LAST_MODIFICATION_TIMESTAMP);

        restToDoListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToDoList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedToDoList))
            )
            .andExpect(status().isOk());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
        ToDoList testToDoList = toDoListList.get(toDoListList.size() - 1);
        assertThat(testToDoList.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testToDoList.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testToDoList.getCreationTimestamp()).isEqualTo(UPDATED_CREATION_TIMESTAMP);
        assertThat(testToDoList.getLastModificationTimestamp()).isEqualTo(UPDATED_LAST_MODIFICATION_TIMESTAMP);
    }

    @Test
    void fullUpdateToDoListWithPatch() throws Exception {
        // Initialize the database
        toDoList.setId(UUID.randomUUID().toString());
        toDoListRepository.save(toDoList);

        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();

        // Update the toDoList using partial update
        ToDoList partialUpdatedToDoList = new ToDoList();
        partialUpdatedToDoList.setId(toDoList.getId());

        partialUpdatedToDoList
            .label(UPDATED_LABEL)
            .status(UPDATED_STATUS)
            .creationTimestamp(UPDATED_CREATION_TIMESTAMP)
            .lastModificationTimestamp(UPDATED_LAST_MODIFICATION_TIMESTAMP);

        restToDoListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedToDoList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedToDoList))
            )
            .andExpect(status().isOk());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
        ToDoList testToDoList = toDoListList.get(toDoListList.size() - 1);
        assertThat(testToDoList.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testToDoList.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testToDoList.getCreationTimestamp()).isEqualTo(UPDATED_CREATION_TIMESTAMP);
        assertThat(testToDoList.getLastModificationTimestamp()).isEqualTo(UPDATED_LAST_MODIFICATION_TIMESTAMP);
    }

    @Test
    void patchNonExistingToDoList() throws Exception {
        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();
        toDoList.setId(UUID.randomUUID().toString());

        // Create the ToDoList
        ToDoListDTO toDoListDTO = toDoListMapper.toDto(toDoList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToDoListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, toDoListDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toDoListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchToDoList() throws Exception {
        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();
        toDoList.setId(UUID.randomUUID().toString());

        // Create the ToDoList
        ToDoListDTO toDoListDTO = toDoListMapper.toDto(toDoList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToDoListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toDoListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamToDoList() throws Exception {
        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();
        toDoList.setId(UUID.randomUUID().toString());

        // Create the ToDoList
        ToDoListDTO toDoListDTO = toDoListMapper.toDto(toDoList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToDoListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(toDoListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteToDoList() throws Exception {
        // Initialize the database
        toDoList.setId(UUID.randomUUID().toString());
        toDoListRepository.save(toDoList);

        int databaseSizeBeforeDelete = toDoListRepository.findAll().size();

        // Delete the toDoList
        restToDoListMockMvc
            .perform(delete(ENTITY_API_URL_ID, toDoList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
