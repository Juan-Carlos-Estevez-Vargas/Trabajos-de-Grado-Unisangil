package dev.juan.estevez.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.juan.estevez.app.IntegrationTest;
import dev.juan.estevez.app.domain.Modality;
import dev.juan.estevez.app.repository.ModalityRepository;
import dev.juan.estevez.app.service.dto.ModalityDTO;
import dev.juan.estevez.app.service.mapper.ModalityMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ModalityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModalityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/modalities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModalityRepository modalityRepository;

    @Autowired
    private ModalityMapper modalityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModalityMockMvc;

    private Modality modality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modality createEntity(EntityManager em) {
        Modality modality = new Modality().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).document(DEFAULT_DOCUMENT);
        return modality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modality createUpdatedEntity(EntityManager em) {
        Modality modality = new Modality().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).document(UPDATED_DOCUMENT);
        return modality;
    }

    @BeforeEach
    public void initTest() {
        modality = createEntity(em);
    }

    @Test
    @Transactional
    void createModality() throws Exception {
        int databaseSizeBeforeCreate = modalityRepository.findAll().size();
        // Create the Modality
        ModalityDTO modalityDTO = modalityMapper.toDto(modality);
        restModalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modalityDTO)))
            .andExpect(status().isCreated());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeCreate + 1);
        Modality testModality = modalityList.get(modalityList.size() - 1);
        assertThat(testModality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModality.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testModality.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
    }

    @Test
    @Transactional
    void createModalityWithExistingId() throws Exception {
        // Create the Modality with an existing ID
        modality.setId(1L);
        ModalityDTO modalityDTO = modalityMapper.toDto(modality);

        int databaseSizeBeforeCreate = modalityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modalityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllModalities() throws Exception {
        // Initialize the database
        modalityRepository.saveAndFlush(modality);

        // Get all the modalityList
        restModalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modality.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(DEFAULT_DOCUMENT)));
    }

    @Test
    @Transactional
    void getModality() throws Exception {
        // Initialize the database
        modalityRepository.saveAndFlush(modality);

        // Get the modality
        restModalityMockMvc
            .perform(get(ENTITY_API_URL_ID, modality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modality.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.document").value(DEFAULT_DOCUMENT));
    }

    @Test
    @Transactional
    void getNonExistingModality() throws Exception {
        // Get the modality
        restModalityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModality() throws Exception {
        // Initialize the database
        modalityRepository.saveAndFlush(modality);

        int databaseSizeBeforeUpdate = modalityRepository.findAll().size();

        // Update the modality
        Modality updatedModality = modalityRepository.findById(modality.getId()).get();
        // Disconnect from session so that the updates on updatedModality are not directly saved in db
        em.detach(updatedModality);
        updatedModality.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).document(UPDATED_DOCUMENT);
        ModalityDTO modalityDTO = modalityMapper.toDto(updatedModality);

        restModalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modalityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeUpdate);
        Modality testModality = modalityList.get(modalityList.size() - 1);
        assertThat(testModality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModality.getDocument()).isEqualTo(UPDATED_DOCUMENT);
    }

    @Test
    @Transactional
    void putNonExistingModality() throws Exception {
        int databaseSizeBeforeUpdate = modalityRepository.findAll().size();
        modality.setId(count.incrementAndGet());

        // Create the Modality
        ModalityDTO modalityDTO = modalityMapper.toDto(modality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModality() throws Exception {
        int databaseSizeBeforeUpdate = modalityRepository.findAll().size();
        modality.setId(count.incrementAndGet());

        // Create the Modality
        ModalityDTO modalityDTO = modalityMapper.toDto(modality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModality() throws Exception {
        int databaseSizeBeforeUpdate = modalityRepository.findAll().size();
        modality.setId(count.incrementAndGet());

        // Create the Modality
        ModalityDTO modalityDTO = modalityMapper.toDto(modality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModalityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modalityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModalityWithPatch() throws Exception {
        // Initialize the database
        modalityRepository.saveAndFlush(modality);

        int databaseSizeBeforeUpdate = modalityRepository.findAll().size();

        // Update the modality using partial update
        Modality partialUpdatedModality = new Modality();
        partialUpdatedModality.setId(modality.getId());

        partialUpdatedModality.description(UPDATED_DESCRIPTION);

        restModalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModality))
            )
            .andExpect(status().isOk());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeUpdate);
        Modality testModality = modalityList.get(modalityList.size() - 1);
        assertThat(testModality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModality.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
    }

    @Test
    @Transactional
    void fullUpdateModalityWithPatch() throws Exception {
        // Initialize the database
        modalityRepository.saveAndFlush(modality);

        int databaseSizeBeforeUpdate = modalityRepository.findAll().size();

        // Update the modality using partial update
        Modality partialUpdatedModality = new Modality();
        partialUpdatedModality.setId(modality.getId());

        partialUpdatedModality.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).document(UPDATED_DOCUMENT);

        restModalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModality))
            )
            .andExpect(status().isOk());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeUpdate);
        Modality testModality = modalityList.get(modalityList.size() - 1);
        assertThat(testModality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModality.getDocument()).isEqualTo(UPDATED_DOCUMENT);
    }

    @Test
    @Transactional
    void patchNonExistingModality() throws Exception {
        int databaseSizeBeforeUpdate = modalityRepository.findAll().size();
        modality.setId(count.incrementAndGet());

        // Create the Modality
        ModalityDTO modalityDTO = modalityMapper.toDto(modality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, modalityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(modalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModality() throws Exception {
        int databaseSizeBeforeUpdate = modalityRepository.findAll().size();
        modality.setId(count.incrementAndGet());

        // Create the Modality
        ModalityDTO modalityDTO = modalityMapper.toDto(modality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(modalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModality() throws Exception {
        int databaseSizeBeforeUpdate = modalityRepository.findAll().size();
        modality.setId(count.incrementAndGet());

        // Create the Modality
        ModalityDTO modalityDTO = modalityMapper.toDto(modality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModalityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(modalityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Modality in the database
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModality() throws Exception {
        // Initialize the database
        modalityRepository.saveAndFlush(modality);

        int databaseSizeBeforeDelete = modalityRepository.findAll().size();

        // Delete the modality
        restModalityMockMvc
            .perform(delete(ENTITY_API_URL_ID, modality.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Modality> modalityList = modalityRepository.findAll();
        assertThat(modalityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
