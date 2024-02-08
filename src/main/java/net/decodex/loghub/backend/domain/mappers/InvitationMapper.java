package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.InvitationDto;
import net.decodex.loghub.backend.domain.models.Invitation;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvitationMapper {
    Invitation toEntity(InvitationDto invitationDto);

    InvitationDto toDto(Invitation invitation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Invitation partialUpdate(InvitationDto invitationDto, @MappingTarget Invitation invitation);
}