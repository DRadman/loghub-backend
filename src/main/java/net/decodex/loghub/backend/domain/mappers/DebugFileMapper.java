package net.decodex.loghub.backend.domain.mappers;

import net.decodex.loghub.backend.domain.dto.DebugFileDto;
import net.decodex.loghub.backend.domain.models.DebugFile;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DebugFileMapper {
    DebugFile toEntity(DebugFileDto debugFileDto);

    DebugFileDto toDto(DebugFile debugFile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DebugFile partialUpdate(DebugFileDto debugFileDto, @MappingTarget DebugFile debugFile);
}