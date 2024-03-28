package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.LogSessionDto;
import net.decodex.loghub.backend.domain.dto.LogSessionWithSourceIdDto;
import net.decodex.loghub.backend.domain.dto.requests.CreateLogSessionDto;
import net.decodex.loghub.backend.domain.models.LogSession;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LogSessionMapper {
    LogSession toEntity(LogSessionDto logSessionDto);

    LogSessionDto toDto(LogSession logSession);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LogSession partialUpdate(LogSessionDto logSessionDto, @MappingTarget LogSession logSession);

    LogSession toEntity(CreateLogSessionDto createLogSessionDto);

    CreateLogSessionDto toDto1(LogSession logSession);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LogSession partialUpdate(CreateLogSessionDto createLogSessionDto, @MappingTarget LogSession logSession);

    @Mapping(source = "projectId", target = "project.projectId")
    @Mapping(source = "logSourceId", target = "source.logSourceId")
    LogSession toEntity(LogSessionWithSourceIdDto logSessionWithSourceIdDto);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "source.logSourceId", target = "logSourceId")
    LogSessionWithSourceIdDto toDtoWithProjectId(LogSession logSession);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "projectId", target = "project.projectId")
    @Mapping(source = "logSourceId", target = "source.logSourceId")
    LogSession partialUpdate(LogSessionWithSourceIdDto logSessionWithSourceIdDto, @MappingTarget LogSession logSession);
}