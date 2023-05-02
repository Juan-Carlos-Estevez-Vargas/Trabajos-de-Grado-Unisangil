package dev.juan.estevez.app.service.mapper;

import dev.juan.estevez.app.domain.Proposal;
import dev.juan.estevez.app.service.dto.ProposalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proposal} and its DTO {@link ProposalDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProposalMapper extends EntityMapper<ProposalDTO, Proposal> {}
