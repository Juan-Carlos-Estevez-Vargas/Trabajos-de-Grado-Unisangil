package dev.juan.estevez.app.service.impl;

import dev.juan.estevez.app.domain.Tutor;
import dev.juan.estevez.app.repository.TutorRepository;
import dev.juan.estevez.app.service.TutorService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tutor}.
 */
@Service
@Transactional
public class TutorServiceImpl implements TutorService {

    private final Logger log = LoggerFactory.getLogger(TutorServiceImpl.class);

    private final TutorRepository tutorRepository;

    public TutorServiceImpl(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    @Override
    public Tutor save(Tutor tutor) {
        log.debug("Request to save Tutor : {}", tutor);
        return tutorRepository.save(tutor);
    }

    @Override
    public Tutor update(Tutor tutor) {
        log.debug("Request to update Tutor : {}", tutor);
        return tutorRepository.save(tutor);
    }

    @Override
    public Optional<Tutor> partialUpdate(Tutor tutor) {
        log.debug("Request to partially update Tutor : {}", tutor);

        return tutorRepository
            .findById(tutor.getId())
            .map(existingTutor -> {
                if (tutor.getFirstName() != null) {
                    existingTutor.setFirstName(tutor.getFirstName());
                }
                if (tutor.getLastName() != null) {
                    existingTutor.setLastName(tutor.getLastName());
                }
                if (tutor.getEmail() != null) {
                    existingTutor.setEmail(tutor.getEmail());
                }
                if (tutor.getPhoneNumber() != null) {
                    existingTutor.setPhoneNumber(tutor.getPhoneNumber());
                }
                if (tutor.getHireDate() != null) {
                    existingTutor.setHireDate(tutor.getHireDate());
                }

                return existingTutor;
            })
            .map(tutorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tutor> findAll(Pageable pageable) {
        log.debug("Request to get all Tutors");
        return tutorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tutor> findOne(Long id) {
        log.debug("Request to get Tutor : {}", id);
        return tutorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tutor : {}", id);
        tutorRepository.deleteById(id);
    }
}
