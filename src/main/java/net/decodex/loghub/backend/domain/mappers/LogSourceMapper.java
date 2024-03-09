package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.LogSourceDto;
import net.decodex.loghub.backend.domain.dto.LogSourceWithOnlineStatusDto;
import net.decodex.loghub.backend.domain.dto.requests.CreateLogSourceDto;
import net.decodex.loghub.backend.domain.models.LogSource;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LogSourceMapper {
    LogSource toEntity(LogSourceDto logSourceDto);

    LogSourceDto toDto(LogSource logSource);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LogSource partialUpdate(LogSourceDto logSourceDto, @MappingTarget LogSource logSource);

    LogSource toEntity(CreateLogSourceDto createLogSourceDto);

    CreateLogSourceDto toDto1(LogSource logSource);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LogSource partialUpdate(CreateLogSourceDto createLogSourceDto, @MappingTarget LogSource logSource);

    LogSourceWithOnlineStatusDto toDtoWithOnlineStatus(LogSource logSource);
}