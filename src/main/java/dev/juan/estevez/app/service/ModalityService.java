package dev.juan.estevez.app.service;

import dev.juan.estevez.app.domain.Modality;
import dev.juan.estevez.app.repository.ModalityRepository;
import dev.juan.estevez.app.service.dto.ModalityDTO;
import dev.juan.estevez.app.service.mapper.ModalityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Modality}.
 */
@Service
@Transactional
public class ModalityService {

    private final Logger log = LoggerFactory.getLogger(ModalityService.class);

    private final ModalityRepository modalityRepository;

    private final ModalityMapper modalityMapper;

    public ModalityService(ModalityRepository modalityRepository, ModalityMapper modalityMapper) {
        this.modalityRepository = modalityRepository;
        this.modalityMapper = modalityMapper;
    }

    /**
     * Save a modality.
     *
     * @param modalityDTO the entity to save.
     * @return the persisted entity.
     */
    public ModalityDTO save(ModalityDTO modalityDTO) {
        log.debug("Request to save Modality : {}", modalityDTO);
        Modality modality = modalityMapper.toEntity(modalityDTO);
        modality = modalityRepository.save(modality);
        return modalityMapper.toDto(modality);
    }

    /**
     * Update a modality.
     *
     * @param modalityDTO the entity to save.
     * @return the persisted entity.
     */
    public ModalityDTO update(ModalityDTO modalityDTO) {
        log.debug("Request to update Modality : {}", modalityDTO);
        Modality modality = modalityMapper.toEntity(modalityDTO);
        modality = modalityRepository.save(modality);
        return modalityMapper.toDto(modality);
    }

    /**
     * Partially update a modality.
     *
     * @param modalityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModalityDTO> partialUpdate(ModalityDTO modalityDTO) {
        log.debug("Request to partially update Modality : {}", modalityDTO);

        return modalityRepository
            .findById(modalityDTO.getId())
            .map(existingModality -> {
                modalityMapper.partialUpdate(existingModality, modalityDTO);

                return existingModality;
            })
            .map(modalityRepository::save)
            .map(modalityMapper::toDto);
    }

    /**
     * Get all the modalities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ModalityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Modalities");
        return modalityRepository.findAll(pageable).map(modalityMapper::toDto);
    }

    /**
     * Get one modality by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModalityDTO> findOne(Long id) {
        log.debug("Request to get Modality : {}", id);
        return modalityRepository.findById(id).map(modalityMapper::toDto);
    }

    /**
     * Delete the modality by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Modality : {}", id);
        modalityRepository.deleteById(id);
    }
}
