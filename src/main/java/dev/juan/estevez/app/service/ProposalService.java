package dev.juan.estevez.app.service;

import dev.juan.estevez.app.domain.Proposal;
import dev.juan.estevez.app.repository.ProposalRepository;
import dev.juan.estevez.app.service.dto.ProposalDTO;
import dev.juan.estevez.app.service.mapper.ProposalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Proposal}.
 */
@Service
@Transactional
public class ProposalService {

    private final Logger log = LoggerFactory.getLogger(ProposalService.class);

    private final ProposalRepository proposalRepository;

    private final ProposalMapper proposalMapper;

    public ProposalService(ProposalRepository proposalRepository, ProposalMapper proposalMapper) {
        this.proposalRepository = proposalRepository;
        this.proposalMapper = proposalMapper;
    }

    /**
     * Save a proposal.
     *
     * @param proposalDTO the entity to save.
     * @return the persisted entity.
     */
    public ProposalDTO save(ProposalDTO proposalDTO) {
        log.debug("Request to save Proposal : {}", proposalDTO);
        Proposal proposal = proposalMapper.toEntity(proposalDTO);
        proposal = proposalRepository.save(proposal);
        return proposalMapper.toDto(proposal);
    }

    /**
     * Update a proposal.
     *
     * @param proposalDTO the entity to save.
     * @return the persisted entity.
     */
    public ProposalDTO update(ProposalDTO proposalDTO) {
        log.debug("Request to update Proposal : {}", proposalDTO);
        Proposal proposal = proposalMapper.toEntity(proposalDTO);
        proposal = proposalRepository.save(proposal);
        return proposalMapper.toDto(proposal);
    }

    /**
     * Partially update a proposal.
     *
     * @param proposalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProposalDTO> partialUpdate(ProposalDTO proposalDTO) {
        log.debug("Request to partially update Proposal : {}", proposalDTO);

        return proposalRepository
            .findById(proposalDTO.getId())
            .map(existingProposal -> {
                proposalMapper.partialUpdate(existingProposal, proposalDTO);

                return existingProposal;
            })
            .map(proposalRepository::save)
            .map(proposalMapper::toDto);
    }

    /**
     * Get all the proposals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProposalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Proposals");
        return proposalRepository.findAll(pageable).map(proposalMapper::toDto);
    }

    /**
     * Get one proposal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProposalDTO> findOne(Long id) {
        log.debug("Request to get Proposal : {}", id);
        return proposalRepository.findById(id).map(proposalMapper::toDto);
    }

    /**
     * Delete the proposal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Proposal : {}", id);
        proposalRepository.deleteById(id);
    }
}
