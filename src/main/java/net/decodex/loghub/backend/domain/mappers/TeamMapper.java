package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.requests.TeamRequestDto;
import net.decodex.loghub.backend.domain.dto.TeamDto;
import net.decodex.loghub.backend.domain.models.Team;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamMapper {
    Team toEntity(TeamDto teamDto);

    TeamDto toDto(Team team);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Team partialUpdate(TeamDto teamDto, @MappingTarget Team team);

    Team toEntity(TeamRequestDto teamRequestDto);

    TeamRequestDto toDto1(Team team);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Team partialUpdate(TeamRequestDto teamRequestDto, @MappingTarget Team team);
}