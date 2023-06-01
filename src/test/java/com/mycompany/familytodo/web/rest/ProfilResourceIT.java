package com.mycompany.familytodo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.familytodo.IntegrationTest;
import com.mycompany.familytodo.domain.Profil;
import com.mycompany.familytodo.domain.enumeration.ProfilRole;
import com.mycompany.familytodo.repository.ProfilRepository;
import com.mycompany.familytodo.service.dto.ProfilDTO;
import com.mycompany.familytodo.service.mapper.ProfilMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ProfilResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfilResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final ProfilRole DEFAULT_ROLE = ProfilRole.ACCOUNT_OWNER;
    private static final ProfilRole UPDATED_ROLE = ProfilRole.MEMBER;

    private static final String ENTITY_API_URL = "/api/profils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private ProfilMapper profilMapper;

    @Autowired
    private MockMvc restProfilMockMvc;

    private Profil profil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createEntity() {
        Profil profil = new Profil().name(DEFAULT_NAME).imageUrl(DEFAULT_IMAGE_URL).role(DEFAULT_ROLE);
        return profil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createUpdatedEntity() {
        Profil profil = new Profil().name(UPDATED_NAME).imageUrl(UPDATED_IMAGE_URL).role(UPDATED_ROLE);
        return profil;
    }

    @BeforeEach
    public void initTest() {
        profilRepository.deleteAll();
        profil = createEntity();
    }

    @Test
    void createProfil() throws Exception {
        int databaseSizeBeforeCreate = profilRepository.findAll().size();
        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);
        restProfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profilDTO)))
            .andExpect(status().isCreated());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeCreate + 1);
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProfil.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testProfil.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    void createProfilWithExistingId() throws Exception {
        // Create the Profil with an existing ID
        profil.setId("existing_id");
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        int databaseSizeBeforeCreate = profilRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProfils() throws Exception {
        // Initialize the database
        profilRepository.save(profil);

        // Get all the profilList
        restProfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profil.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    void getProfil() throws Exception {
        // Initialize the database
        profilRepository.save(profil);

        // Get the profil
        restProfilMockMvc
            .perform(get(ENTITY_API_URL_ID, profil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profil.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    void getNonExistingProfil() throws Exception {
        // Get the profil
        restProfilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingProfil() throws Exception {
        // Initialize the database
        profilRepository.save(profil);

        int databaseSizeBeforeUpdate = profilRepository.findAll().size();

        // Update the profil
        Profil updatedProfil = profilRepository.findById(profil.getId()).get();
        updatedProfil.name(UPDATED_NAME).imageUrl(UPDATED_IMAGE_URL).role(UPDATED_ROLE);
        ProfilDTO profilDTO = profilMapper.toDto(updatedProfil);

        restProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(profilDTO))
            )
            .andExpect(status().isOk());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfil.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProfil.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    void putNonExistingProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().size();
        profil.setId(UUID.randomUUID().toString());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().size();
        profil.setId(UUID.randomUUID().toString());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().size();
        profil.setId(UUID.randomUUID().toString());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProfilWithPatch() throws Exception {
        // Initialize the database
        profilRepository.save(profil);

        int databaseSizeBeforeUpdate = profilRepository.findAll().size();

        // Update the profil using partial update
        Profil partialUpdatedProfil = new Profil();
        partialUpdatedProfil.setId(profil.getId());

        partialUpdatedProfil.imageUrl(UPDATED_IMAGE_URL);

        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfil))
            )
            .andExpect(status().isOk());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProfil.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProfil.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    void fullUpdateProfilWithPatch() throws Exception {
        // Initialize the database
        profilRepository.save(profil);

        int databaseSizeBeforeUpdate = profilRepository.findAll().size();

        // Update the profil using partial update
        Profil partialUpdatedProfil = new Profil();
        partialUpdatedProfil.setId(profil.getId());

        partialUpdatedProfil.name(UPDATED_NAME).imageUrl(UPDATED_IMAGE_URL).role(UPDATED_ROLE);

        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfil))
            )
            .andExpect(status().isOk());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfil.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProfil.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    void patchNonExistingProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().size();
        profil.setId(UUID.randomUUID().toString());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().size();
        profil.setId(UUID.randomUUID().toString());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().size();
        profil.setId(UUID.randomUUID().toString());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(profilDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProfil() throws Exception {
        // Initialize the database
        profilRepository.save(profil);

        int databaseSizeBeforeDelete = profilRepository.findAll().size();

        // Delete the profil
        restProfilMockMvc
            .perform(delete(ENTITY_API_URL_ID, profil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Profil> profilList = profilRepository.findAll();
        assertThat(profilList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
