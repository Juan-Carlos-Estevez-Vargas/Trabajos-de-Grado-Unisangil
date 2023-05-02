package dev.juan.estevez.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.juan.estevez.app.IntegrationTest;
import dev.juan.estevez.app.domain.Proposal;
import dev.juan.estevez.app.domain.enumeration.State;
import dev.juan.estevez.app.repository.ProposalRepository;
import dev.juan.estevez.app.service.dto.ProposalDTO;
import dev.juan.estevez.app.service.mapper.ProposalMapper;
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
 * Integration tests for the {@link ProposalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProposalResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ARCHIVE = "AAAAAAAAAA";
    private static final String UPDATED_ARCHIVE = "BBBBBBBBBB";

    private static final State DEFAULT_STATE = State.RECHAZADA;
    private static final State UPDATED_STATE = State.APROBADA;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_STUDENT = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/proposals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private ProposalMapper proposalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProposalMockMvc;

    private Proposal proposal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createEntity(EntityManager em) {
        Proposal proposal = new Proposal()
            .title(DEFAULT_TITLE)
            .archive(DEFAULT_ARCHIVE)
            .state(DEFAULT_STATE)
            .comments(DEFAULT_COMMENTS)
            .student(DEFAULT_STUDENT);
        return proposal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createUpdatedEntity(EntityManager em) {
        Proposal proposal = new Proposal()
            .title(UPDATED_TITLE)
            .archive(UPDATED_ARCHIVE)
            .state(UPDATED_STATE)
            .comments(UPDATED_COMMENTS)
            .student(UPDATED_STUDENT);
        return proposal;
    }

    @BeforeEach
    public void initTest() {
        proposal = createEntity(em);
    }

    @Test
    @Transactional
    void createProposal() throws Exception {
        int databaseSizeBeforeCreate = proposalRepository.findAll().size();
        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);
        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isCreated());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate + 1);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProposal.getArchive()).isEqualTo(DEFAULT_ARCHIVE);
        assertThat(testProposal.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testProposal.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testProposal.getStudent()).isEqualTo(DEFAULT_STUDENT);
    }

    @Test
    @Transactional
    void createProposalWithExistingId() throws Exception {
        // Create the Proposal with an existing ID
        proposal.setId(1L);
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        int databaseSizeBeforeCreate = proposalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProposals() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList
        restProposalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].archive").value(hasItem(DEFAULT_ARCHIVE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].student").value(hasItem(DEFAULT_STUDENT)));
    }

    @Test
    @Transactional
    void getProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get the proposal
        restProposalMockMvc
            .perform(get(ENTITY_API_URL_ID, proposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposal.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.archive").value(DEFAULT_ARCHIVE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.student").value(DEFAULT_STUDENT));
    }

    @Test
    @Transactional
    void getNonExistingProposal() throws Exception {
        // Get the proposal
        restProposalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal
        Proposal updatedProposal = proposalRepository.findById(proposal.getId()).get();
        // Disconnect from session so that the updates on updatedProposal are not directly saved in db
        em.detach(updatedProposal);
        updatedProposal
            .title(UPDATED_TITLE)
            .archive(UPDATED_ARCHIVE)
            .state(UPDATED_STATE)
            .comments(UPDATED_COMMENTS)
            .student(UPDATED_STUDENT);
        ProposalDTO proposalDTO = proposalMapper.toDto(updatedProposal);

        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProposal.getArchive()).isEqualTo(UPDATED_ARCHIVE);
        assertThat(testProposal.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testProposal.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testProposal.getStudent()).isEqualTo(UPDATED_STUDENT);
    }

    @Test
    @Transactional
    void putNonExistingProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProposalWithPatch() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal using partial update
        Proposal partialUpdatedProposal = new Proposal();
        partialUpdatedProposal.setId(proposal.getId());

        partialUpdatedProposal.state(UPDATED_STATE).comments(UPDATED_COMMENTS).student(UPDATED_STUDENT);

        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposal))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProposal.getArchive()).isEqualTo(DEFAULT_ARCHIVE);
        assertThat(testProposal.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testProposal.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testProposal.getStudent()).isEqualTo(UPDATED_STUDENT);
    }

    @Test
    @Transactional
    void fullUpdateProposalWithPatch() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal using partial update
        Proposal partialUpdatedProposal = new Proposal();
        partialUpdatedProposal.setId(proposal.getId());

        partialUpdatedProposal
            .title(UPDATED_TITLE)
            .archive(UPDATED_ARCHIVE)
            .state(UPDATED_STATE)
            .comments(UPDATED_COMMENTS)
            .student(UPDATED_STUDENT);

        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposal))
            )
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProposal.getArchive()).isEqualTo(UPDATED_ARCHIVE);
        assertThat(testProposal.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testProposal.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testProposal.getStudent()).isEqualTo(UPDATED_STUDENT);
    }

    @Test
    @Transactional
    void patchNonExistingProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proposalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();
        proposal.setId(count.incrementAndGet());

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.toDto(proposal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProposalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(proposalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        int databaseSizeBeforeDelete = proposalRepository.findAll().size();

        // Delete the proposal
        restProposalMockMvc
            .perform(delete(ENTITY_API_URL_ID, proposal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
