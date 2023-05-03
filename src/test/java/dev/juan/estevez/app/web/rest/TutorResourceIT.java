package dev.juan.estevez.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.juan.estevez.app.IntegrationTest;
import dev.juan.estevez.app.domain.Tutor;
import dev.juan.estevez.app.repository.TutorRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TutorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TutorResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_HIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tutors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTutorMockMvc;

    private Tutor tutor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tutor createEntity(EntityManager em) {
        Tutor tutor = new Tutor()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .hireDate(DEFAULT_HIRE_DATE);
        return tutor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tutor createUpdatedEntity(EntityManager em) {
        Tutor tutor = new Tutor()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE);
        return tutor;
    }

    @BeforeEach
    public void initTest() {
        tutor = createEntity(em);
    }

    @Test
    @Transactional
    void createTutor() throws Exception {
        int databaseSizeBeforeCreate = tutorRepository.findAll().size();
        // Create the Tutor
        restTutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isCreated());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeCreate + 1);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTutor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTutor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTutor.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTutor.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
    }

    @Test
    @Transactional
    void createTutorWithExistingId() throws Exception {
        // Create the Tutor with an existing ID
        tutor.setId(1L);

        int databaseSizeBeforeCreate = tutorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTutors() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get all the tutorList
        restTutorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tutor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())));
    }

    @Test
    @Transactional
    void getTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        // Get the tutor
        restTutorMockMvc
            .perform(get(ENTITY_API_URL_ID, tutor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tutor.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTutor() throws Exception {
        // Get the tutor
        restTutorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Update the tutor
        Tutor updatedTutor = tutorRepository.findById(tutor.getId()).get();
        // Disconnect from session so that the updates on updatedTutor are not directly saved in db
        em.detach(updatedTutor);
        updatedTutor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE);

        restTutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTutor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTutor))
            )
            .andExpect(status().isOk());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTutor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTutor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTutor.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTutor.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tutor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tutor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tutor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTutorWithPatch() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Update the tutor using partial update
        Tutor partialUpdatedTutor = new Tutor();
        partialUpdatedTutor.setId(tutor.getId());

        partialUpdatedTutor
            .firstName(UPDATED_FIRST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE);

        restTutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTutor))
            )
            .andExpect(status().isOk());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTutor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTutor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTutor.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTutor.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTutorWithPatch() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();

        // Update the tutor using partial update
        Tutor partialUpdatedTutor = new Tutor();
        partialUpdatedTutor.setId(tutor.getId());

        partialUpdatedTutor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE);

        restTutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTutor))
            )
            .andExpect(status().isOk());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
        Tutor testTutor = tutorList.get(tutorList.size() - 1);
        assertThat(testTutor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTutor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTutor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTutor.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTutor.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tutor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tutor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTutor() throws Exception {
        int databaseSizeBeforeUpdate = tutorRepository.findAll().size();
        tutor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTutorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tutor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tutor in the database
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTutor() throws Exception {
        // Initialize the database
        tutorRepository.saveAndFlush(tutor);

        int databaseSizeBeforeDelete = tutorRepository.findAll().size();

        // Delete the tutor
        restTutorMockMvc
            .perform(delete(ENTITY_API_URL_ID, tutor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tutor> tutorList = tutorRepository.findAll();
        assertThat(tutorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
