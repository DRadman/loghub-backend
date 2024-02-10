package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.LogSessionDto;
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
}