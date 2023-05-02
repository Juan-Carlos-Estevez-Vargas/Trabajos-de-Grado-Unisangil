package dev.juan.estevez.app.repository;

import dev.juan.estevez.app.domain.Modality;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Modality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModalityRepository extends JpaRepository<Modality, Long> {}
