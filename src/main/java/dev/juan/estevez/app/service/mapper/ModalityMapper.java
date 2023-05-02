package dev.juan.estevez.app.service.mapper;

import dev.juan.estevez.app.domain.Modality;
import dev.juan.estevez.app.service.dto.ModalityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Modality} and its DTO {@link ModalityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModalityMapper extends EntityMapper<ModalityDTO, Modality> {}
